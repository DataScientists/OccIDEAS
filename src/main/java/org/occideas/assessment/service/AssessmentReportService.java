package org.occideas.assessment.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
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
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.utilities.ProgressStatusEnum;
import org.occideas.utilities.ReportsEnum;
import org.occideas.utilities.ReportsStatusEnum;
import org.occideas.vo.ExportCSVVO;
import org.occideas.vo.FilterModuleVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
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

    private final ReportConfig reportConfig;
    private final ReportHistoryDao reportHistoryDao;
    private final InterviewDao interviewDao;
    private final InterviewQuestionDao interviewQuestionDao;
    private final InterviewAnswerDao interviewAnswerDao;
    private final SystemPropertyDao systemPropertyDao;
    private final QuestionDao questionDao;
    private final AssessmentLookupService assessmentLookupService;

    private final DecimalFormat df = new DecimalFormat("#.0");
    private static final long MS_PER_MIN = 60 * 1000;

    public AssessmentReportService(ReportConfig reportConfig, ReportHistoryDao reportHistoryDao, InterviewDao interviewDao, InterviewQuestionDao interviewQuestionDao, InterviewAnswerDao interviewAnswerDao, SystemPropertyDao systemPropertyDao, QuestionDao questionDao, AssessmentLookupService assessmentLookupService) {
        this.reportConfig = reportConfig;
        this.reportHistoryDao = reportHistoryDao;
        this.interviewDao = interviewDao;
        this.interviewQuestionDao = interviewQuestionDao;
        this.interviewAnswerDao = interviewAnswerDao;
        this.systemPropertyDao = systemPropertyDao;
        this.questionDao = questionDao;
        this.assessmentLookupService = assessmentLookupService;
    }

    @Async("autoAssessmentTaskExecutor")
    public void exportAssessmentReport(FilterModuleVO filterModuleVO, String requestorId) {
        validateDirectory(reportConfig.getExportDir());
        log.info("Started export assessment with export directory {}", reportConfig.getExportDir());
        String exportFileCSV = createFileName(filterModuleVO.getFileName());

        ReportHistory reportHistory = insertToReportHistory(exportFileCSV, "", null,
                ReportsEnum.REPORT_ASSESSMENT_EXPORT.getValue(), requestorId);
        String fullPath = reportConfig.getExportDir() + "/" + reportHistory.getId() + "_" + exportFileCSV;
        log.info("Export Assessment full path {}", fullPath);
        reportHistory = insertToReportHistory(exportFileCSV, fullPath, reportHistory.getId(),
                ReportsEnum.REPORT_ASSESSMENT_EXPORT.getValue(), requestorId);

        Long count = interviewDao.getCountForModules(filterModuleVO.getFilterModule());
        log.info("Export Assessment is for number of modules {}", count);
        reportHistory.setRecordCount(count);

        updateProgress(reportHistory, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.11, new Date(), INITIAL_DURATION_MIN);

        long msPerInterview = getMsPerInterview(count.intValue(), reportHistory,
                ReportsEnum.REPORT_ASSESSMENT_EXPORT.getValue());

        List<Interview> uniqueInterviews = interviewDao.getAllWithModules(filterModuleVO.getFilterModule());
        log.info("Export Assessment is for number of interviews {}", uniqueInterviews.size());

        ExportCSVVO csvVO = populateAssessmentCSV(uniqueInterviews, reportHistory, msPerInterview);

        writeReport(fullPath, reportHistory, csvVO, requestorId);

        assessmentLookupService.writeLookup(fullPath, uniqueInterviews, filterModuleVO.getFilterModule(), requestorId);
        log.info("Completed export assessment with export directory {}", reportConfig.getExportDir());
    }

    public void exportInterviewsCSVReport(FilterModuleVO filterModuleVO, String requestorId) {
        validateDirectory(reportConfig.getExportDir());
        log.info("Started export interview with export directory {}", reportConfig.getExportDir());
        String exportFileCSV = createFileName(filterModuleVO.getFileName());
        ReportHistory reportHistory = insertToReportHistory(exportFileCSV, "", null, ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue(), requestorId);
        String fullPath = reportConfig.getExportDir() + "/" + reportHistory.getId() + "_" + exportFileCSV;
        log.info("Export interview full path {}", fullPath);
        reportHistory = insertToReportHistory(exportFileCSV, fullPath, reportHistory.getId(),
                ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue(), requestorId);

        Long count = interviewQuestionDao.getUniqueInterviewQuestionCount(filterModuleVO.getFilterModule());
        reportHistory.setRecordCount(count);

        updateProgress(reportHistory, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.11, new Date(),
                INITIAL_DURATION_MIN);

        long msPerInterview = getMsPerInterview(count.intValue(), reportHistory,
                ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue());

        List<Interview> uniqueInterviews = interviewDao.getAllWithModules(filterModuleVO.getFilterModule());
        log.info("Export interview is for number of interviews {}", uniqueInterviews.size());

        Map<Long, Node> nodeList = new HashMap<>();
        String[] modules = filterModuleVO.getFilterModule();
        for (String module : modules) {
            nodeList.put(Long.valueOf(module), getTopModuleByTopNodeId(Long.valueOf(module)));
        }
        try {
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            int currentCount = 0;

            String tempFolder = reportConfig.getExportDir() + "/" + reportHistory.getId() + "/";
            File temp = new File(tempFolder);
            temp.mkdir();
            int iCount = 0;
            List<InterviewAnswer> allAnswers = new ArrayList<>();
            List<InterviewQuestion> allQuestions = new ArrayList<>();
            for (Interview interview : uniqueInterviews) {

                currentCount++;

                updateProgress(uniqueInterviews.size(), reportHistory, currentCount, elapsedTime, msPerInterview);

                File file = new File(tempFolder + iCount + ".csv");
                CSVWriter writer = new CSVWriter(
                        new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8),
                        ',',
                        CSVWriter.DEFAULT_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END
                );
                List<InterviewAnswer> answers = interviewAnswerDao.findByInterviewId(interview.getIdinterview());
                List<InterviewQuestion> questions = interviewQuestionDao
                        .findByInterviewId(interview.getIdinterview());
                allAnswers.addAll(answers);
                allQuestions.addAll(questions);
                List<String> headerList = new ArrayList<String>();
                headerList.add("InterviewID");
                headerList.add("ParticipantID");
                headerList.add("Participant Status");
                headerList.add("Assessment Status");
                List<String> answerList = new ArrayList<String>();
                answerList.add(String.valueOf(interview.getIdinterview()));
                answerList.add(String.valueOf(interview.getReferenceNumber()));
                String pStatus = "ERROR";
                try {
                    pStatus = ProgressStatusEnum.getDisplayByStatus(interview.getParticipant().getStatus());
                } catch (Exception e) {
                    log.error("no participant at interview " + interview.getIdinterview(), e);
                }
                answerList.add(pStatus);
                answerList.add(interview.getAssessedStatus());

                for (InterviewAnswer ia : answers) {
                    String headerKey = generateHeaderKey(ia, nodeList);
                    boolean ignore = false;
                    if (headerList.contains(headerKey)) {
                        ignore = true;
                    } else {
                        headerList.add(headerKey);
                    }
                    if (!ignore) {
                        answerList.add(ia.getAnswerFreetext());
                    }
                }
                writer.writeNext(headerList.toArray(new String[headerList.size()]));
                writer.writeNext(answerList.toArray(new String[answerList.size()]));
                writer.close();
                iCount++;
                log.info("Write count for export interview {} of {}", iCount, uniqueInterviews.size());
            }
            File folder = new File(tempFolder);

            File[] listOfFiles = folder.listFiles();
            List<String> fullHeaderList = new ArrayList<>();
            for (File rfile : listOfFiles) {
                if (rfile.isFile()) {
                    CSVReader reader = new CSVReader(new FileReader(rfile));
                    String[] line;
                    iCount = 0;
                    String[] headerKeys;
                    while ((line = reader.readNext()) != null) {
                        if (iCount == 0) {
                            headerKeys = line;
                            for (String headerKey : headerKeys) {
                                if (!fullHeaderList.contains(headerKey)) {
                                    fullHeaderList.add(headerKey);
                                }
                            }
                        } else if (iCount == 1) {

                        } else {
                            log.error("Export Interview: Error more lines than there should be");
                        }
                        iCount++;
                    }
                    reader.close();

                }
            }
            File fileOut = new File(fullPath);
            CSVWriter writer = new CSVWriter(
                    new OutputStreamWriter(new FileOutputStream(fileOut), StandardCharsets.UTF_8),
                    ',',
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END
            );
            writer.writeNext(fullHeaderList.toArray(new String[fullHeaderList.size()]));
            for (File rfile : listOfFiles) {
                if (rfile.isFile()) {
                    CSVReader reader = new CSVReader(new FileReader(rfile));
                    iCount = 0;
                    String[] headerKeys = null;
                    String[] answers = null;
                    String[] record;
                    while ((record = reader.readNext()) != null) {
                        if (iCount == 0) {
                            headerKeys = record;
                        } else if (iCount == 1) {
                            answers = record;
                        } else {
                            log.error("Export Interview: Error more lines than there should be");
                        }
                        iCount++;
                    }
                    reader.close();
                    Map<String, String> answerMap = new HashMap<>();
                    for (int i = 0; i < headerKeys.length; i++) {
                        answerMap.put(headerKeys[i], answers[i]);
                    }
                    List<String> fullAnswerList = new ArrayList();
                    for (String header : fullHeaderList) {
                        String answer = "-NA-";
                        if (answerMap.get(header) != null) {
                            answer = answerMap.get(header);
                        }
                        fullAnswerList.add(answer);
                    }
                    writer.writeNext(fullAnswerList.toArray(new String[fullAnswerList.size()]));
                }
            }
            writer.close();
            FileUtils.deleteDirectory(temp);
            assessmentLookupService.writeLookup(fullPath, uniqueInterviews, filterModuleVO.getFilterModule(), requestorId);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        updateProgress(reportHistory, ReportsStatusEnum.COMPLETED.getValue(), 100);
        log.info("Completed export interview with export directory {}", reportConfig.getExportDir());
    }

    private void writeReport(String fullPath, ReportHistory reportHistory, ExportCSVVO csvVO, String requestorId) {
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
                writeLookup(fullPath, csvVO, headers, requestorId);
            }

            updateProgress(reportHistory, ReportsStatusEnum.COMPLETED.getValue(), 100);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            updateProgress(reportHistory, ReportsStatusEnum.FAILED.getValue(), 0);
        }
        log.info("Export Assessment completed writing to CSV {}", fullPath);

    }

    private String generateHeaderKey(InterviewAnswer interviewAnswer,
                                     Map<Long, Node> nodeList) {
        Node topModule = deriveTopModule(interviewAnswer, nodeList);
        return deriveHeader(topModule, interviewAnswer.getNumber());
    }

    private String deriveHeader(Node topModule, String number) {
        StringBuilder header = new StringBuilder();
        String headerName = topModule.getName().substring(0, 4);
        if (headerName.charAt(0) == '_') {
            headerName = headerName.replaceFirst("_", "");
        }
        header.append(headerName);
        header.append("_");
        header.append(number);
        return header.toString();
    }

    private Node deriveTopModule(InterviewAnswer interviewAnswer, Map<Long, Node> nodeList) {
        long topNodeId = interviewAnswer.getTopNodeId();
        Node topModule = nodeList.get(topNodeId);
        if (topModule == null) {
            topModule = getTopModuleByTopNodeId(topNodeId);
            nodeList.put(topNodeId, topModule);
        }
        return topModule;
    }

    private void writeLookup(String fullPath, ExportCSVVO csvVO, String[] headers, String requestorId) throws IOException {

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
        lookup.setRequestor(requestorId);
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
                                                String type, String requestorId) {

        ReportHistory reportHistory = new ReportHistory();
        if (id != null) {
            reportHistory.setId(id);
        }
        reportHistory.setName(exportFileCSV);
        reportHistory.setPath(fullPath);
        reportHistory.setRequestor(requestorId);
        reportHistory.setStatus(ReportsStatusEnum.IN_PROGRESS.getValue());
        reportHistory.setType(type);
        reportHistory.setUpdatedBy(requestorId);
        reportHistory.setProgress(0 + "%");
        return reportHistoryDao.saveNewTransaction(reportHistory);
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
