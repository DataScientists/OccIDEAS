package org.occideas.assessment.service;

import com.opencsv.CSVWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.config.ReportConfig;
import org.occideas.entity.*;
import org.occideas.exceptions.GenericException;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.interviewanswer.dao.InterviewAnswerDao;
import org.occideas.interviewquestion.dao.InterviewQuestionDao;
import org.occideas.mapper.RuleMapperImpl;
import org.occideas.question.dao.QuestionDao;
import org.occideas.reporthistory.dao.ReportHistoryDao;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.utilities.ProgressStatusEnum;
import org.occideas.utilities.ReportsEnum;
import org.occideas.utilities.ReportsStatusEnum;
import org.occideas.vo.ExportCSVVO;
import org.occideas.vo.FilterModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

@Transactional
@Service
public class AssessmentReportService {

    private final Logger log = LogManager.getLogger(this.getClass());

    private static final float INITIAL_DURATION_MIN = 60;

    @Autowired
    private ReportConfig reportConfig;
    @Autowired
    private ReportHistoryDao reportHistoryDao;
    @Autowired
    private InterviewDao interviewDao;
    @Autowired
    private SystemPropertyDao systemPropertyDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private InterviewAnswerDao interviewAnswerDao;
    @Autowired
    private InterviewQuestionDao interviewQuestionDao;

    private final DecimalFormat df = new DecimalFormat("#.0");
    private static final long MS_PER_MIN = 60 * 1000;

    public void exportAssessmentReport(FilterModuleVO filterModuleVO) {
        validateDirectory(reportConfig.getExportDir());
        log.info("Started export assessment with export directory {}", reportConfig.getExportDir());
        String exportFileCSV = createFileName(filterModuleVO.getFileName());

        ReportHistory reportHistory = insertToReportHistory(exportFileCSV, "", null,
                ReportsEnum.REPORT_ASSESSMENT_EXPORT.getValue());
        String fullPath = reportConfig.getExportDir() + "/" + reportHistory.getId() + "_" + exportFileCSV;
        log.info("Export Assessment full path {}", fullPath);
        reportHistory = insertToReportHistory(exportFileCSV, fullPath, reportHistory.getId(),
                ReportsEnum.REPORT_ASSESSMENT_EXPORT.getValue());

        Long count = interviewDao.getCountForModules(filterModuleVO.getFilterModule());
        log.info("Export Assessment is for number of modules {}", count);
        reportHistory.setRecordCount(count);

        updateProgress(reportHistory, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.11, new Date(), INITIAL_DURATION_MIN);

        long msPerInterview = getMsPerInterview(count.intValue(), reportHistory,
                ReportsEnum.REPORT_NOISE_ASSESSMENT_EXPORT.getValue());

        List<Interview> uniqueInterviews = interviewDao.getAllWithModules(filterModuleVO.getFilterModule());
        log.info("Export Assessment is for number of interviews {}", uniqueInterviews.size());

        ExportCSVVO csvVO = populateAssessmentCSV(uniqueInterviews, reportHistory, msPerInterview);

        writeReport(fullPath, reportHistory, csvVO);

        List<InterviewAnswer> allAnswers = new ArrayList<>();
        List<InterviewQuestion> allQuestions = new ArrayList<>();
        int iSize = uniqueInterviews.size();
        int iCount = 0;
        for (Interview interview : uniqueInterviews) {
            List<InterviewAnswer> answers = interviewAnswerDao.findByInterviewId(interview.getIdinterview());
            List<InterviewQuestion> questions = interviewQuestionDao.findByInterviewId(interview.getIdinterview());
            allAnswers.addAll(answers);
            allQuestions.addAll(questions);
            iCount++;
            if (iCount % (iSize * 0.1) == 0) {
                log.info("Assessment report processing {} of {}", iCount, iSize);
            }
        }

        Map<Long, Node> nodeList = new HashMap<>();
        String[] modules = filterModuleVO.getFilterModule();
        // Initialize map to prevent multiple re-queries
        for (String module : modules) {
            nodeList.put(Long.valueOf(module), getTopModuleByTopNodeId(Long.valueOf(module)));
        }
        try {
            writeLookupNew(fullPath, allAnswers, nodeList, allQuestions);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        log.info("Completed export assessment with export directory {}", reportConfig.getExportDir());
    }

    private void writeLookupNew(String fullPath, List<InterviewAnswer> allAnswers, Map<Long, Node> nodeVoList,
                                List<InterviewQuestion> questions) throws IOException {

        List<String> fullHeaderList = new ArrayList<>();
        for (InterviewAnswer ia : allAnswers) {
            String header = generateHeaderKeyLookUp(ia, nodeVoList, questions);
            if (!fullHeaderList.contains(header)) {
                fullHeaderList.add(header);
            }

        }

        ReportHistory lookup = new ReportHistory();
        lookup.setType("Lookup");
        lookup.setStartDt(new Date());

        // Write lookup file
        String pathname = fullPath.substring(0, fullPath.length() - 4) + "-Lookup.csv";
        File fileLookup = new File(pathname);
        CSVWriter lookupWriter = new CSVWriter(
                new OutputStreamWriter(new FileOutputStream(fileLookup), StandardCharsets.UTF_8),
                ',',
                CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END
        );

        String[] lookupHeader = new String[]{"Id", "Name"};
        lookupWriter.writeNext(lookupHeader);
        for (String lookupkeyvalue : fullHeaderList) {
            String[] line = lookupkeyvalue.split("<>");
            lookupWriter.writeNext(line);
        }

        lookupWriter.close();
        lookup.setRequestor(extractUserFromToken());
        lookup.setName(fileLookup.getName());
        lookup.setPath(fullPath);
        lookup.setEndDt(new Date());
        lookup.setDuration(lookup.getEndDt().getTime() - lookup.getStartDt().getTime());
        lookup.setStatus(ReportsStatusEnum.COMPLETED.getValue());
        lookup.setProgress("100.0%");
        log.info("Export Assessment lookup file has been written {}", pathname);
        reportHistoryDao.saveNewTransaction(lookup);
    }

    private String generateHeaderKeyLookUp(InterviewAnswer interviewAnswer, Map<Long, Node> nodeList,
                                           List<InterviewQuestion> questions) {
        String retValue;
        String key;
        String name;

        InterviewQuestion interviewQuestion = null;
        for (InterviewQuestion question : questions) {
            if (interviewAnswer.getInterviewQuestionId() == question.getId()) {
                interviewQuestion = question;
                break;
            }
        }
        if (interviewQuestion == null) {
            interviewQuestion = interviewQuestionDao.findIntQuestion(interviewAnswer.getIdInterview(),
                    interviewAnswer.getParentQuestionId());
        }
        long topNodeId = interviewAnswer.getTopNodeId();
        Node topModule = nodeList.get(topNodeId);
        if (topModule == null) {
            topModule = getTopModuleByTopNodeId(topNodeId);
            nodeList.put(topNodeId, topModule);
        }

        StringBuilder header = new StringBuilder();
        if ("Q_multiple".equals(interviewQuestion.getType())) {
            String headerName = topModule.getName().substring(0, 4);
            if (headerName.charAt(0) == '_') {
                headerName = headerName.replaceFirst("_", "");
            }
            header.append(headerName);
            header.append("_");
            // header.append(interviewQuestionVO.getNumber());
            // header.append("_");
            header.append(interviewAnswer.getNumber());
            key = header.toString();
            name = interviewQuestion.getName() + " " + interviewAnswer.getName();

        } else {
            String headerName = topModule.getName().substring(0, 4);
            if (headerName.charAt(0) == '_') {
                headerName = headerName.replaceFirst("_", "");
            }
            header.append(headerName);
            header.append("_");
            header.append(interviewQuestion.getNumber());
            key = header.toString();
            name = interviewQuestion.getName();

        }
        retValue = key + "<>" + name;
        return retValue;

    }

    private void writeReport(String fullPath, ReportHistory reportHistory, ExportCSVVO csvVO) {
        CSVWriter writer;
        try {
            File csvDirFile = new File(reportConfig.getExportDir());

            if (!csvDirFile.exists()) {
                csvDirFile.mkdir();
            }
            File file = new File(fullPath);
            writer = new CSVWriter(
                    new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8),
                    ',',
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END
            );
            // feed in your array (or convert your data to an array)
            String[] headers = Arrays.copyOf(csvVO.getHeaders().toArray(), csvVO.getHeaders().toArray().length,
                    String[].class);
            // write header
            writer.writeNext(headers);
            // write answers
            // iterate map
            for (Map.Entry<Interview, List<String>> interviewListEntry : csvVO.getAnswers().entrySet()) {

                List<String> value = interviewListEntry.getValue();
                String[] answers = Arrays.copyOf(value.toArray(), value.toArray().length, String[].class);
                writer.writeNext(answers);
            }
            writer.close();

            if (reportHistory.getType().equals(ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue())) {
                writeLookup(fullPath, csvVO, headers);
            }

            updateProgress(reportHistory, ReportsStatusEnum.COMPLETED.getValue(), 100);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            updateProgress(reportHistory, ReportsStatusEnum.FAILED.getValue(), 0);
        }
        log.info("Export Assessment completed writing to CSV {}", fullPath);

    }

    private void writeLookup(String fullPath, ExportCSVVO csvVO, String[] headers) throws IOException {

        String[] names = Arrays.copyOf(csvVO.getQuestionList().toArray(), csvVO.getQuestionList().toArray().length,
                String[].class);

        if (names.length != (headers.length - 3)) {
            System.out.println("Something wrong with the data: header count: " + headers.length + ", answer count: "
                    + names.length);
        }

        ReportHistory lookup = new ReportHistory();
        lookup.setType("Lookup");
        lookup.setStartDt(new Date());

        // Write lookup file
        File fileLookup = new File(fullPath.substring(0, fullPath.length() - 4) + "-Lookup.csv");
        CSVWriter lookupWriter = new CSVWriter(
                new OutputStreamWriter(new FileOutputStream(fileLookup), StandardCharsets.UTF_8),
                ',',
                CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END
        );

        String[] lookupHeader = new String[]{"Id", "Name"};
        lookupWriter.writeNext(lookupHeader);
        for (int i = 3, j = 0; i < headers.length && j < names.length; i++, j++) {

            String[] nameArray = names[j].split("\\|");
            String[] line = new String[]{headers[i], (nameArray.length == 1) ? names[j] : nameArray[0]};
            lookupWriter.writeNext(line);
        }

        lookupWriter.close();
        lookup.setRequestor(extractUserFromToken());
        lookup.setName(fileLookup.getName());
        lookup.setPath(fullPath);
        lookup.setEndDt(new Date());
        lookup.setDuration(lookup.getEndDt().getTime() - lookup.getStartDt().getTime());
        lookup.setStatus(ReportsStatusEnum.COMPLETED.getValue());
        lookup.setProgress("100.0%");

        reportHistoryDao.saveNewTransaction(lookup);
    }

    private ExportCSVVO populateAssessmentCSV(List<Interview> uniqueInterviews, ReportHistory reportHistory,
                                              long msPerInterview) {
        log.info("Export Assessment , started populating csv details to be written.");
        ExportCSVVO vo = new ExportCSVVO();
        Set<String> headers = populateHeadersAndAnswersAssessment(uniqueInterviews, vo, reportHistory, msPerInterview);
        vo.setHeaders(headers);
        log.info("Export Assessment , completed populating csv details to be written.");
        return vo;
    }

    private Set<String> populateHeadersAndAnswersAssessment(List<Interview> uniqueInterviews, ExportCSVVO exportCSVVO,
                                                            ReportHistory reportHistory, long msPerInterview) {

        updateProgress(reportHistory, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.1);
        Set<String> headers = new LinkedHashSet<>();
        headers.add("InterviewID");
        headers.add("ParticipantID");
        headers.add("Participant Status");
        headers.add("Assessment Status");
        headers.add("Intro Module Name");
        headers.add("Job Module Name");

        List<SystemProperty> properties = systemPropertyDao.getByType("STUDYAGENT");
        List<Agent> agents = new ArrayList<>();

        for (SystemProperty prop : properties) {
            Agent agent = new Agent();
            agent.setIdAgent(Long.parseLong(prop.getValue()));
            agent.setName(prop.getName());
            agents.add(agent);

            headers.add(agent.getName() + "_MANUAL");
            headers.add(agent.getName() + "_AUTO");
        }

        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        int currentCount = 0;

        for (Interview interview : uniqueInterviews) {

            currentCount++;

            updateProgress(uniqueInterviews.size(), reportHistory, currentCount, elapsedTime, msPerInterview);

            List<String> answers = new ArrayList<>();
            answers.add(String.valueOf(interview.getIdinterview()));
            answers.add(String.valueOf(interview.getReferenceNumber()));
            String pStatus = "ERROR";
            try {
                pStatus = String.valueOf(ProgressStatusEnum.getDisplayByStatus(interview.getParticipant().getStatus()));
            } catch (Exception e) {
                log.error("no participant at interview" + interview.getIdinterview(), e);
            }
            answers.add(pStatus);
            answers.add(interview.getAssessedStatus());
            addModuleNames(interview, answers);

            for (Agent agent : agents) {
                boolean aAgentFound = false;
                for (Rule rule : interview.getManualAssessedRules()) {
                    if (agent.getIdAgent() == rule.getAgentId()) {
                        answers.add(RuleMapperImpl.getDescriptionByValue(rule.getLevel()));
                        aAgentFound = true;
                        break;
                    }
                }
                if (!aAgentFound) {
                    answers.add("-NA-");
                }
                aAgentFound = false;
                for (Rule rule : interview.getAutoAssessedRules()) {
                    if (agent.getIdAgent() == rule.getAgentId()) {
                        answers.add(RuleMapperImpl.getDescriptionByValue(rule.getLevel()));
                        aAgentFound = true;
                        break;
                    }
                }
                if (!aAgentFound) {
                    answers.add("-NA-");
                }
            }
            exportCSVVO.getAnswers().put(interview, answers);

            elapsedTime = System.currentTimeMillis() - startTime;
        }

        return headers;
    }

    private void addModuleNames(Interview interview, List<String> answers) {
        if (interview.getModuleList() != null) {
            answers.add(getIntroModuleName(interview.getModuleList().get(0)));
            answers.add(getModuleName(
                    (interview.getModuleList().size() > 1) ? interview.getModuleList().get(1) : null));
        }
    }

    private String getModuleName(InterviewIntroModuleModule interviewIntroModuleModule) {

        return (interviewIntroModuleModule == null) ? ""
                : (interviewIntroModuleModule.getInterviewModuleName() == null) ? " "
                : interviewIntroModuleModule.getInterviewModuleName().substring(0, 4);
    }

    private String getIntroModuleName(InterviewIntroModuleModule interviewIntroModuleModule) {

        return (interviewIntroModuleModule == null) ? ""
                : (interviewIntroModuleModule.getIntroModuleNodeName() == null) ? " "
                : interviewIntroModuleModule.getIntroModuleNodeName().substring(0, 4);
    }

    private Node getTopModuleByTopNodeId(Long topNodeId) {
        // todo the getTopModuleByTopNodeId should be moved to NodeDao
        return questionDao.getTopModuleByTopNodeId(topNodeId);
    }

    private long getMsPerInterview(int size, ReportHistory reportHistory, String type) {

        long msPerInterview = 0;
        ReportHistory historicalReport = reportHistoryDao.getLatestByType(type);
        if (historicalReport != null) {
            msPerInterview = (long) ((historicalReport.getDuration() * MS_PER_MIN) / historicalReport.getRecordCount());
            reportHistory.setDuration((size * msPerInterview) / MS_PER_MIN);
            updateProgress(reportHistory, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.2);
        }
        return msPerInterview;
    }

    private void updateProgress(int interviewSize, ReportHistory reportHistory, int currentCount, long elapsedTime,
                                long msPerInterview) {

        // Check processed count and estimate duration
        if (msPerInterview == 0) {
            msPerInterview = elapsedTime / currentCount;
            reportHistory.setDuration((interviewSize * msPerInterview) / MS_PER_MIN);
        }

        updateProgress(reportHistory, reportHistory.getStatus(), (double) currentCount / interviewSize * 100,
                reportHistory.getStartDt(), reportHistory.getDuration());
    }

    private void updateProgress(ReportHistory reportHistory, String status, double progress) {

        Date endDate;

        if (!status.equalsIgnoreCase(ReportsStatusEnum.IN_PROGRESS.getValue())) {
            endDate = new Date();

            reportHistory
                    .setDuration((float) (endDate.getTime() - reportHistory.getStartDt().getTime()) / MS_PER_MIN);

            reportHistory.setEndDt(endDate);
        }
        updateProgress(reportHistory, status, progress, reportHistory.getStartDt(),
                reportHistory.getDuration());
    }

    private void updateProgress(ReportHistory reportHistory, String status, double progress,
                                Date startDate, float duration) {

        reportHistory.setStatus(status);

        if (progress != 0) {
            String progressInPercentage = df.format(progress) + "%";
            reportHistory.setProgress(progressInPercentage);

            if (startDate != null && reportHistory.getStartDt() == null) {
                reportHistory.setStartDt(startDate);
            }

            reportHistory.setDuration(Float.parseFloat(df.format(duration)));

        }
        reportHistoryDao.saveNewTransaction(reportHistory);
    }

    private ReportHistory insertToReportHistory(String exportFileCSV, String fullPath, Long id,
                                                String type) {

        ReportHistory reportHistory = new ReportHistory();
        if (id != null) {
            reportHistory.setId(id);
        }
        String user = extractUserFromToken();
        reportHistory.setName(exportFileCSV);
        reportHistory.setPath(fullPath);
        reportHistory.setRequestor(user);
        reportHistory.setStatus(ReportsStatusEnum.IN_PROGRESS.getValue());
        reportHistory.setType(type);
        reportHistory.setUpdatedBy(user);
        reportHistory.setProgress(0 + "%");
        return reportHistoryDao.saveNewTransaction(reportHistory);
    }

    private String extractUserFromToken() {
        TokenManager tokenManager = new TokenManager();
        String token = ((TokenResponse) SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
        return tokenManager.parseUsernameFromToken(token);
    }

    private void validateDirectory(String exportDir) {
        if (StringUtils.isEmpty(exportDir)) {
            throw new GenericException("Report directory does not exist.");
        }
        Path path = Paths.get(exportDir);
        if (!Files.exists(path)) {
            throw new GenericException("Report directory does not exist.");
        }
    }

    private String createFileName(String filename) {
        return filename + ".csv";
    }

}
