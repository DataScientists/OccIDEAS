package org.occideas.assessment.service;

import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.*;
import org.occideas.interviewanswer.dao.InterviewAnswerDao;
import org.occideas.interviewquestion.dao.InterviewQuestionDao;
import org.occideas.question.dao.QuestionDao;
import org.occideas.reporthistory.dao.ReportHistoryDao;
import org.occideas.utilities.ReportsStatusEnum;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Transactional
@Service
public class AssessmentLookupService {

    private final Logger log = LogManager.getLogger(this.getClass());

    private final InterviewAnswerDao interviewAnswerDao;
    private final InterviewQuestionDao interviewQuestionDao;
    private final QuestionDao questionDao;
    private final ReportHistoryDao reportHistoryDao;

    public AssessmentLookupService(InterviewAnswerDao interviewAnswerDao, InterviewQuestionDao interviewQuestionDao, QuestionDao questionDao, ReportHistoryDao reportHistoryDao) {
        this.interviewAnswerDao = interviewAnswerDao;
        this.interviewQuestionDao = interviewQuestionDao;
        this.questionDao = questionDao;
        this.reportHistoryDao = reportHistoryDao;
    }

    @Async("autoAssessmentTaskExecutor")
    public void writeLookup(String fullPath,
                            List<Interview> uniqueInterviews,
                            String[] filteredModule,
                            String requestorId) {
        log.info("Assessment started writing lookup file ");
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
            //if (iCount % (iSize * 0.1) == 0) {
            //    log.info("Assessment report processing {} of {}", iCount, iSize);
            //}
        }

        Map<Long, Node> nodeList = new HashMap<>();
        // Initialize map to prevent multiple re-queries
        for (String module : filteredModule) {
            nodeList.put(Long.valueOf(module), getTopModuleByTopNodeId(Long.valueOf(module)));
        }
        try {
            writeLookupNew(fullPath, allAnswers, nodeList, allQuestions, requestorId);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        log.info("Assessment completed writing lookup file ");
    }

    private void writeLookupNew(String fullPath,
                                List<InterviewAnswer> allAnswers,
                                Map<Long, Node> nodeVoList,
                                List<InterviewQuestion> questions,
                                String requestorId) throws IOException {

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
        lookup.setRequestor(requestorId);
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
        boolean isMulti = false;
        if("Q_multiple".equals(interviewQuestion.getType())) {
        	isMulti = true;
        }
        long topNodeId = interviewAnswer.getTopNodeId();
        Node topModule = nodeList.get(topNodeId);
        if (topModule == null) {
            topModule = getTopModuleByTopNodeId(topNodeId);
            nodeList.put(topNodeId, topModule);
        }

        StringBuilder header = new StringBuilder();
        String headerName = topModule.getName().substring(0, 4);
        if (headerName.charAt(0) == '_') {
            headerName = headerName.replaceFirst("_", "");
        }
        //header.append(headerName);
       // header.append("_");
        //header.append(interviewAnswer.getNumber());
        if(isMulti) {
        	header.append(headerName);
            header.append("_");
            header.append(interviewAnswer.getNumber());
            name = interviewQuestion.getName() + " " + interviewAnswer.getName();
        }else {
        	String parentNumber = interviewQuestion.getNumber();
        	
        	header.append(headerName);
            header.append("_");
            header.append(parentNumber);
            name = interviewQuestion.getName();
        }
        key = header.toString();
        //name = interviewQuestion.getName() + " " + interviewAnswer.getName();
        retValue = key + "<>" + name;
        return retValue;

    }

    private Node getTopModuleByTopNodeId(Long topNodeId) {
        // todo the getTopModuleByTopNodeId should be moved to NodeDao
        return questionDao.getTopModuleByTopNodeId(topNodeId);
    }
}
