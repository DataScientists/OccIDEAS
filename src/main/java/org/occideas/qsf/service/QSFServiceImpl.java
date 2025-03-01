package org.occideas.qsf.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.agent.dao.AgentDao;
import org.occideas.config.QualtricsConfig;
import org.occideas.entity.Constant;
import org.occideas.entity.Interview;
import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewQuestion;
import org.occideas.entity.InterviewResults;
import org.occideas.entity.NodeQSF;
import org.occideas.entity.Participant;
import org.occideas.entity.QSFQuestionMapper;
import org.occideas.entity.QualtricsSurvey;
import org.occideas.entity.QualtricsSurveyResponse;
import org.occideas.entity.SystemProperty;
import org.occideas.exceptions.GenericException;
import org.occideas.exceptions.NodeNotFoundException;
import org.occideas.exceptions.StudyIntroModuleNotFoundException;
import org.occideas.fragment.service.FragmentService;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.interview.dao.InterviewResultDao;
import org.occideas.interview.service.AutoAssessmentService;
import org.occideas.interview.service.InterviewService;
import org.occideas.interview.service.result.assessor.NoiseAssessmentService;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.interviewquestion.dao.InterviewQuestionDao;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.mapper.RuleMapperImpl;
import org.occideas.module.service.ModuleService;
import org.occideas.participant.dao.ParticipantDao;
import org.occideas.participant.service.ParticipantService;
import org.occideas.possibleanswer.service.PossibleAnswerService;
import org.occideas.qsf.IQSFClient;
import org.occideas.qsf.QSFNodeTypeMapper;
import org.occideas.qsf.dao.INodeQSFDao;
import org.occideas.qsf.dao.QSFQuestionMapperDao;
import org.occideas.qsf.dao.QualtricsSurveyResponseDao;
import org.occideas.qsf.payload.CopySurveyPayload;
import org.occideas.qsf.request.SurveyExportRequest;
import org.occideas.qsf.response.Response;
import org.occideas.qsf.response.SurveyExportResponse;
import org.occideas.qsf.response.SurveyListResponse;
import org.occideas.qsf.response.SurveyMetadata;
import org.occideas.qsf.response.SurveyResponses;
import org.occideas.qsf.subscriber.constant.QualtricsProcessStatus;
import org.occideas.qsf.subscriber.service.QualtricsSurveyService;
import org.occideas.question.service.QuestionService;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.CommonUtil;
import org.occideas.utilities.QualtricsUtil;
import org.occideas.utilities.StudyAgentUtil;
import org.occideas.utilities.ZipUtil;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewQuestionVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.ParticipantVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.ResponseSummary;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Transactional
@Service
public class QSFServiceImpl implements IQSFService {

    public static final String QID = "QID";
    private Logger log = LogManager.getLogger(this.getClass());


    @Autowired
    private INodeQSFDao dao;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    @Lazy
    private ModuleService moduleService;
    @Autowired
    private FragmentService fragmentService;
    @Autowired
    private PossibleAnswerService possibleAnswerService;
    @Autowired
    @Lazy
    private QuestionService questionService;
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private InterviewQuestionService interviewQuestionService;
    @Autowired
    private InterviewAnswerService interviewAnswerService;
    @Autowired
    @Lazy
    private SystemPropertyService systemPropertyService;
    @Autowired
    private IQSFClient iqsfClient;
    @Autowired
    private SystemPropertyDao systemPropertyDao;
    @Autowired
    private StudyAgentUtil studyAgentUtil;
    @Autowired
    private QualtricsSurveyService qualtricsSurveyService;
    @Autowired
    private QSFQuestionMapperDao qsfQuestionMapperDao;
    @Autowired
    private AutoAssessmentService autoAssessmentService;
    @Autowired
    private QualtricsSurveyResponseDao qualtricsSurveyResponseDao;
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private NoiseAssessmentService noiseAssessmentService;
    @Autowired
    private RuleMapperImpl ruleMapper;
    @Autowired
    private QSFInterviewReplicationService qsfInterviewReplicationService;
    @Autowired
    private InterviewDao interviewDao;
    @Autowired
    private ParticipantDao participantDao;
    @Autowired
    private InterviewQuestionDao interviewQuestionDao;
    @Autowired
    private QualtricsConfig qualtricsConfig;
    @Autowired
    private InterviewResultDao interviewResultDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String save(String surveyId, long idNode, String path) {
        return dao.save(surveyId, idNode, path);
    }

    @Override
    public String getSurveyIdByIdNode(long idNode) {
        return dao.getSurveyIdByIdNode(idNode);
    }

    @Override
    public NodeQSF getByIdNode(long idNode) {
        return dao.getByIdNode(idNode);
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void copySurveys(String userId, String prefix) throws InterruptedException {
        SurveyListResponse response = (SurveyListResponse) iqsfClient.listSurvey().getEntity();
        response.getResult().getElements()
                .stream().forEach(survey -> iqsfClient.copySurvey(new CopySurveyPayload(survey.getName() + prefix), survey.getId(), userId));
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void exportResponseQSF(Long id) throws InterruptedException {
        List<ModuleVO> modules = moduleService.findById(id);
        if (!modules.isEmpty()) {
            final SurveyResponses surveyResponses = this.exportQSFResponses(modules.get(0).getIdNode());
            if (surveyResponses != null) {
                this.consumeQSFResponse(surveyResponses);
            } else {
                log.error("No survey id found for module id {}", modules.get(0).getIdNode());
            }
        }
    }

    @Override
    public void importResponseQSF(String surveyId, String reference) throws InterruptedException, IOException {
        SurveyExportRequest surveyExportRequest = new SurveyExportRequest();
        surveyExportRequest.setFormat("json");
        javax.ws.rs.core.Response surveyExportResponse = iqsfClient.createExportResponse(surveyId, surveyExportRequest);

        String fileId = monitorExportProgress(surveyId, surveyExportResponse);
        if (!StringUtils.isEmpty(fileId)) {
            File surveyJsonFile = processDownloadedFile(surveyId, fileId);
            ObjectMapper objectMapper = new ObjectMapper();
            SurveyResponses surveyResponses = objectMapper.readValue(surveyJsonFile, SurveyResponses.class);
            surveyResponses.getResponses().forEach(response -> {
                QualtricsSurvey qualtricsSurvey = new QualtricsSurvey();
                qualtricsSurvey.setResponseId(response.getResponseId());
                qualtricsSurvey.setSurveyId(surveyId);
                qualtricsSurvey.setQualtricsStatus(QualtricsProcessStatus.COMPLETE.name());
                qualtricsSurvey.setTopic(reference);
                qualtricsSurvey.setBrandId("Manually triggered");
                qualtricsSurvey.setCompletedDate(LocalDateTime.now());
                try {
                    qualtricsSurvey.setResponse(objectMapper.writeValueAsString(response).getBytes());
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage(), e);
                }
                qualtricsSurveyService.consumeSurveyResponse(qualtricsSurvey);
            });
        }
    }

    @Override
    public SurveyResponses exportQSFResponses(long idNode) throws InterruptedException {
        String surveyId = this.getSurveyIdByIdNode(idNode);

        if (surveyId == null) {
            return null;
        }

        SurveyExportRequest surveyExportRequest = new SurveyExportRequest();
        surveyExportRequest.setFormat("json");
        javax.ws.rs.core.Response response = iqsfClient.createExportResponse(surveyId, surveyExportRequest);
        Thread.currentThread().sleep(2000);
        if (response != null) {
            if (!(response.getEntity() instanceof SurveyExportResponse)) {
                log.error(response.getEntity());
                return null;
            }

            SurveyExportResponse exportResponse = (SurveyExportResponse) response.getEntity();
            int tries = 0;
            String fileId = null;
            while (true) {
                log.info("Export Progress:" + (exportResponse.getResult().getPercentComplete() * 100) + "%");
                javax.ws.rs.core.Response exportProgress = iqsfClient.getExportResponseProgress(surveyId, exportResponse.getResult().getProgressId());
                if (exportProgress != null) {
                    exportResponse = (SurveyExportResponse) exportProgress.getEntity();
                }
                if (exportResponse.getResult().getFileId() != null) {
                    fileId = exportResponse.getResult().getFileId();
                    break;
                }
                if (tries == 20) {
                    log.error("Stop check export qualtrics, seems its down or failed... tried " + tries + " times.");
                    break;
                }
                tries++;
                Thread.currentThread().sleep(5000);
            }
            log.info("export in qualtrics has been completed , tried to check " + tries + " times.");
            File file = iqsfClient.getExportResponseFile(surveyId, fileId);
            log.info("File is successfully exported here " + file.getAbsolutePath());
            this.save(surveyId, idNode, file.getAbsolutePath());
            File responseDir = null;
            try {
                File newDirectory = new File(file.getParent(), FilenameUtils.removeExtension(file.getName()));
                if (!newDirectory.exists()) {
                    newDirectory.mkdir();
                }
                ZipUtil.unzip(file, newDirectory.getAbsolutePath());
                responseDir = newDirectory;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            File[] files = responseDir.listFiles();
            File surveyJsonFile = null;
            for (File survey : files) {
                if ("json".equals(FilenameUtils.getExtension(survey.getName()))) {
                    surveyJsonFile = survey;
                    break;
                }
            }
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                SurveyResponses surveyResponses = objectMapper.readValue(surveyJsonFile, SurveyResponses.class);
                log.info(surveyResponses.toString());
                return surveyResponses;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    Set<Long> uniqueModules = null;

    @Override
    public void consumeQSFResponse(SurveyResponses surveyResponses) {
        if (isEmptyResponse(surveyResponses)) {
            return;
        }
        uniqueModules = new HashSet<>();

        SystemPropertyVO introModule = Optional.ofNullable(systemPropertyService.getByName(Constant.STUDY_INTRO))
                .orElseThrow(StudyIntroModuleNotFoundException::new);
        NodeVO nodeVO = moduleService.getNodeById(Long.valueOf(introModule.getValue()));

        surveyResponses.getResponses().stream().forEach(response -> {
            processResponse(nodeVO, response);
        });

    }

    @Override
    public void processResponse(NodeVO nodeVO, Response response) {
        String referenceNumber = getIDAnswer(response);
        ParticipantVO participantVO = createParticipant(referenceNumber);
        InterviewVO newInterview = createNewInterview(referenceNumber, participantVO);
        createIntroQuestion(newInterview, nodeVO);
        processResponseAnswers(response, referenceNumber, newInterview, nodeVO);
    }

    @Override
    public long processResponse(String surveyId, String reference, Response response) {
        javax.ws.rs.core.Response responseSurveyMetadata = iqsfClient.getSurveyMetadata(surveyId);
        SurveyMetadata surveyMetadata = (SurveyMetadata) responseSurveyMetadata.getEntity();

        String moduleName = surveyMetadata.getMetadata().getSurveyName();
        validateModuleExist(moduleName);

        String responseId = response.getResponseId();

        Map<String, QSFQuestionMapper> questionBySurveyId = qsfQuestionMapperDao.getQuestionBySurveyId(surveyId);
        if (questionBySurveyId.isEmpty()) {
            String message = "Occideas question mapper with qualtrics is empty.";
            log.error(message);
            throw new GenericException(message);
        }

        log.info("started processing response for survey {} , response id {}, module {}", surveyId, responseId, moduleName);

        Map<String, Object> values = response.getValues();
        LinkedList<QuestionAnswerResponse> queue = getQSFQuestionAnswerResponses(questionBySurveyId, values);

        if (queue.isEmpty()) {
            String message = "Occideas question answer response with qualtrics is empty.";
            log.error(message);
            throw new GenericException(message);
        }

        List<Long> listAgentIds = agentDao.getStudyAgentIds();
        Map<String, Object> labels = response.getLabels();
        Map<String, ResponseSummary> summary = createResponseSummary(responseId, questionBySurveyId, queue, listAgentIds, labels);

        long interviewId = qsfInterviewReplicationService.replicateQualtricsInterviewIntoOccideas(responseId, reference,moduleName, summary);
        if(interviewId != -1){
            Interview interview = interviewDao.get(interviewId);
            autoAssessmentService.syncAutoAssessedRule(listAgentIds, interview);
            saveQualtricsResponse(surveyId, responseId, values, summary);


            interviewDao.saveNewTransaction(interview);
            InterviewQuestion oldRootQuestion = interview.getQuestionHistory().get(2); //delete the old root question
            oldRootQuestion.setDeleted(1);
            interviewQuestionDao.save(oldRootQuestion);

            String referenceNumber = interview.getReferenceNumber();

            Participant participant = participantDao.getByReferenceNumber(referenceNumber);
            participant.setStatus(4); //Set status to interview complete
            participantDao.saveOrUpdate(participant);
        }

        
        return interviewId;
    }

    @Override
    public String getWorkshift(Interview interview) {
        if (StringUtils.isEmpty(qualtricsConfig.getNode().getLastShiftHours())) {
            return "N/A";
        }

        Optional<InterviewAnswer> answer = interview.getAnswerHistory().stream()
                .filter(interviewAnswer -> qualtricsConfig.getNode().getLastShiftHours().equalsIgnoreCase(String.valueOf(interviewAnswer.getAnswerId())))
                .findFirst();

        if (answer.isEmpty()) {
            return "N/A";
        }
        log.info("last shift hours is {}", answer.get().getAnswerFreetext());
        return answer.get().getAnswerFreetext();
    }

    @Override
    public Long getWorkshiftIdNode(Interview interview) {
        if (StringUtils.isEmpty(qualtricsConfig.getNode().getLastShiftHours())) {
            return null;
        }

        Optional<InterviewAnswer> answer = interview.getAnswerHistory().stream()
                .filter(interviewAnswer -> qualtricsConfig.getNode().getLastShiftHours().equalsIgnoreCase(String.valueOf(interviewAnswer.getAnswerId())))
                .findFirst();

        if (answer.isEmpty()) {
            return null;
        }

        return answer.get().getAnswerId();
    }

    @Override
    public void saveAssessmentResults(String referenceNumber,
                                      List<Long> listAgentIds,
                                      Interview interview,
                                      BigDecimal workshift) {
        log.info("started updating assessment results for {} and interview id {}", referenceNumber, interview.getIdinterview());
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] results = new byte[0];
        try {
            results = objectMapper.writeValueAsString(noiseAssessmentService.getResults(interview.getIdinterview(), listAgentIds, interview.getFiredRules(), workshift)).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        interviewResultDao.deleteByReferenceNumber(referenceNumber);
        InterviewResults interviewResults = new InterviewResults();
        interviewResults.setInterviewId(interview.getIdinterview());
        interviewResults.setReferenceNumber(referenceNumber);
        interviewResults.setResults(results);
        interviewResultDao.save(interviewResults);
        log.info("completed updating assessment results for {} ", referenceNumber);

    }

    @Override
    public void saveQualtricsResponse(String surveyId,
                                      String responseId,
                                      Map<String, Object> values,
                                      Map<String, ResponseSummary> summary) {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] questionAnswers = new byte[0];
        try {
            questionAnswers = objectMapper.writeValueAsString(summary).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        QualtricsSurveyResponse responderDetails = new QualtricsSurveyResponse(
                surveyId,
                responseId,
                CommonUtil.getValue(values.get("ipAddress")),
                CommonUtil.getValue(values.get("progress")),
                CommonUtil.getValue(values.get("duration")),
                CommonUtil.getValue(values.get("finished")),
                CommonUtil.getValue(values.get("recordedDate")),
                CommonUtil.getValue(values.get("locationLatitude")),
                CommonUtil.getValue(values.get("locationLongitude")),
                questionAnswers
        );
        qualtricsSurveyResponseDao.deleteByResponseId(responseId);
        qualtricsSurveyResponseDao.save(responderDetails);
    }

    private Map<String, ResponseSummary> createResponseSummary(String responseId, Map<String, QSFQuestionMapper> questionBySurveyId, LinkedList<QuestionAnswerResponse> queue, List<Long> listAgentIds, Map<String, Object> labels) {
        Map<String, ResponseSummary> summary = new LinkedHashMap<>();
        while (Objects.nonNull(queue.peek())) {
            QuestionAnswerResponse questionAnswerResponse = queue.poll();
            log.info("processing question {} and answer {} for response {}", questionAnswerResponse.getQsfQuestionId(),
                    questionAnswerResponse.getOccideasAnswerIdNode(),
                    responseId);
            
            ResponseSummary responseSummary = new ResponseSummary();
            if(questionAnswerResponse.getQsfQuestionId().equalsIgnoreCase("AMRID")) {
            	responseSummary.setAnswer(questionAnswerResponse.getOccideasAnswerIdNode());
            	summary.put(questionAnswerResponse.getQsfQuestionId(), responseSummary);
            }
            QSFQuestionMapper qsfQuestionMapper = questionBySurveyId.get(questionAnswerResponse.getQsfQuestionId());
            if(qsfQuestionMapper==null) {
            	continue;
            }
            
            responseSummary.setQuestion(qsfQuestionMapper.getQuestionText());
            responseSummary.setQuestionIdNode(String.valueOf(qsfQuestionMapper.getIdNode()));
            responseSummary.setQuestionType(qsfQuestionMapper.getType());
            responseSummary.setAnswerIdNode(questionAnswerResponse.getOccideasAnswerIdNode());
            Object answer = labels.get(questionAnswerResponse.getQsfQuestionId());
            if (Objects.nonNull(answer)) {
                responseSummary.setAnswer(answer.toString());
            } else {
                if (qualtricsConfig.getNode().getLastShiftHours().equalsIgnoreCase(questionAnswerResponse.getOccideasAnswerIdNode())) {
                    responseSummary.setAnswer(CommonUtil.deriveWorkshift(questionAnswerResponse.getFreeTextAnswer()).toPlainString());
                } else {
                    responseSummary.setAnswer(questionAnswerResponse.getFreeTextAnswer());
                }
            }
            List<Long> answers = QualtricsUtil.parseAnswers(questionAnswerResponse.getOccideasAnswerIdNode());
            for (Long answerId : answers) {
            	/*
                List<Rule> listOfFiredRules = autoAssessmentService.deriveFiredRulesByAnswerProvided(answerId);
                List<Rule> autoAssessedRules = new ArrayList<>();
                autoAssessedRules.addAll(autoAssessmentService.getRuleLevelNoExposure(
                        listAgentIds
                        , listOfFiredRules));
                responseSummary.setFiredRules(ruleMapper.convertToRuleVOList(listOfFiredRules));
                responseSummary.setAutoAssessedRules(ruleMapper.convertToRuleVOList(autoAssessedRules));
                */
                summary.put(questionAnswerResponse.getQsfQuestionId(), responseSummary);
            }
        }
        return summary;
    }


    private LinkedList<QuestionAnswerResponse> getQSFQuestionAnswerResponses(Map<String, QSFQuestionMapper> questionBySurveyId, Map<String, Object> values) {
        LinkedList<QuestionAnswerResponse> queue = new LinkedList<>();
        values.entrySet().forEach(entry -> {
            if (isNormalQuestion(entry)) {
                queue.add(new QuestionAnswerResponse(entry.getKey(), CommonUtil.getValue(entry.getValue())));
            } else if (isTextQuestion(entry)) {
                String[] key = entry.getKey().split("_");
                QSFQuestionMapper qsfQuestionMapper = questionBySurveyId.get(key[0]);
                Long answerId = qsfQuestionMapper.getFrequencyIdNode();
                queue.add(new QuestionAnswerResponse(key[0], String.valueOf(answerId), CommonUtil.getValue(entry.getValue())));
            } else if (isFrequencyQuestion(entry)) {
                String[] key = entry.getKey().split("_");
                QSFQuestionMapper qsfQuestionMapper = questionBySurveyId.get(key[0]);
                String frequency = deriveFrequencyVFormValue(values, entry);
                Long frequencyIdNode = qsfQuestionMapper.getFrequencyIdNode();
                if (Objects.nonNull(frequencyIdNode)) {
                    queue.add(new QuestionAnswerResponse(key[0], String.valueOf(frequencyIdNode), frequency));
                }
            } else if (isOccIDEASReferenceNumber(entry)) {
            	queue.add(new QuestionAnswerResponse(entry.getKey(), CommonUtil.getValue(entry.getValue())));
            }
        });
        return queue;
    }

    private String deriveFrequencyVFormValue(Map<String, Object> values, Map.Entry<String, Object> entry) {
        String frequency = entry.getValue().toString();
        String[] splittedQID = entry.getKey().split("_");
        String qid2 = splittedQID[0] + "_2";
        if (values.containsKey(qid2)) {
        	BigDecimal hours = new BigDecimal(frequency);
        	BigDecimal minutes = new BigDecimal(values.get(qid2).toString()).divide(new BigDecimal(60), 15, RoundingMode.CEILING);
            frequency = hours.add(minutes).toString();
        }
        return frequency;
    }

    private boolean isFrequencyQuestion(Map.Entry<String, Object> entry) {
        return entry.getKey().contains("QID") && entry.getKey().contains("_") && entry.getKey().contains("_1") && !entry.getKey().contains("_DO");
    }

    private boolean isNormalQuestion(Map.Entry<String, Object> entry) {
        return entry.getKey().contains("QID") && !entry.getKey().contains("_DO") & !entry.getKey().contains("_");
    }

    private boolean isTextQuestion(Map.Entry<String, Object> entry) {
        return entry.getKey().contains("QID") && entry.getKey().contains("_") && entry.getKey().contains("TEXT") && !entry.getKey().contains("_DO");
    }
    
    private boolean isOccIDEASReferenceNumber(Map.Entry<String, Object> entry) {
        return entry.getKey().contains("AMRID");
    }

    private void validateModuleExist(String moduleName) {
        ModuleVO moduleByName = moduleService.getModuleByName(moduleName);
        if (Objects.isNull(moduleByName)) {
            log.error("Module name does not exist in occideas - " + moduleName);
            throw new GenericException("Module name does not exist in occideas - " + moduleName);
        }
    }

    @Override
    public void createQSFTranslationModule() {
        SystemPropertyVO introModule = Optional.ofNullable(systemPropertyService.getByName(Constant.STUDY_INTRO))
                .orElseThrow(StudyIntroModuleNotFoundException::new);
        List<ModuleVO> modules = moduleService.findById(Long.valueOf(introModule.getValue()));
        if (!CollectionUtils.isEmpty(modules)) {
            studyAgentUtil.buildQSF(modules.get(0), true, true);
        }
    }

    private String getIDAnswer(Response response) {
        Object value = response.getValues().get("IDAnswer");
        return value == null ? null : (String) value;
    }

    @Override
    public void processResponseAnswers(Response response, String referenceNumber, InterviewVO newInterview, NodeVO nodeVO) {

        PriorityQueue<String> answersQueue = new PriorityQueue<>();
        response.getLabels().entrySet().stream()
                .filter(item -> item.getKey().contains(QID))
                .map(item -> item.getValue().toString())
                .forEach(answer -> answersQueue.add(answer));

        createInterviewQuestionsAnswers(answersQueue, nodeVO, newInterview);
    }

    private void createInterviewQuestionsAnswers(PriorityQueue<String> answersQueue, NodeVO nodeVO, InterviewVO newInterview) {
        AtomicInteger questionCounter = new AtomicInteger(1);

        QuestionVO topQuestion = getParentQuestionsFromFirstAnswer(answersQueue.peek(), nodeVO, newInterview);
        createInterviewQuestionFromQuestion(topQuestion, newInterview.getInterviewId(), questionCounter);

        AtomicInteger newQuestionCounter = new AtomicInteger();

        while (answersQueue.peek() != null) {
            String answer = answersQueue.poll();
            System.out.println(answer);
            if (answer.contains("[")) {
                handleMultipleAnswer(newInterview, newQuestionCounter, answer);
                continue;
            } else {
                handleSimpleAnswer(newInterview, newQuestionCounter, answer);
            }
        }
    }

    private void handleMultipleAnswer(InterviewVO newInterview, AtomicInteger questionCounter, String answer) {
        answer = answer.replace("[", "").replace("]", "");
        if (!StringUtils.isEmpty(answer)) {
            if (answer.contains(",")) {
                String[] answers = answer.split(",");
                Arrays.stream(answers)
                        .filter(ans -> ans.contains("_"))
                        .forEach(ans -> handleSimpleAnswer(newInterview, questionCounter, ans.trim()));
            } else {
                handleSimpleAnswer(newInterview, questionCounter, answer);
            }
        }
    }

    private void handleSimpleAnswer(InterviewVO newInterview, AtomicInteger questionCounter, String answer) {
        String values[] = answer.toString().split(" ");
        String splitAnswer[] = values[0].split("_");
        final String moduleKey = splitAnswer[0];
        final String answerNumber = splitAnswer[1];

        Optional<NodeVO> node = getNodeByModuleKey(moduleKey);
        if (node.isPresent()) {
            NodeVO moduleVO = node.get();
            PossibleAnswerVO possibleAnswerVO = possibleAnswerService
                    .findByTopNodeIdAndNumber(moduleVO.getIdNode(), answerNumber);

            if (!uniqueModules.contains(node.get().getIdNode()) && node.get().getNodeclass().equals("M")) {
                interviewQuestionService.updateIntQ(createInterviewModuleQuestion(possibleAnswerVO, moduleVO, newInterview.getInterviewId(), questionCounter.incrementAndGet()));
                uniqueModules.add(node.get().getIdNode());
            } else if (!uniqueModules.contains(node.get().getIdNode()) && node.get().getNodeclass().equals("F")) {
                final List<FragmentVO> modules = fragmentService.findByIdForInterview(node.get().getIdNode());
                if (!modules.isEmpty()) {
                    FragmentVO linkedModule = modules.get(0);

                    if (linkedModule != null) {
                        if (!uniqueModules.contains(linkedModule.getIdNode())) {
                            interviewQuestionService.updateIntQ(createInterviewAJSMQuestion(possibleAnswerVO,
                                    moduleVO,
                                    linkedModule,
                                    newInterview.getInterviewId(),
                                    questionCounter.incrementAndGet()));
                            uniqueModules.add(linkedModule.getIdNode());
                        }

                    }

                } else {
                    log.error("Cant find linked module {}", node.get().getIdNode());
                }
            }


            final Set<Long> linkModules = possibleAnswerVO.getChildNodes().stream().filter(questions -> {
                return questions.getLink() > 0L;
            }).map(QuestionVO::getLink).collect(Collectors.toSet());

            if (!linkModules.isEmpty()) {
                linkModules.forEach(idNode -> {
                    final List<FragmentVO> modules = fragmentService.findByIdForInterview(idNode);
                    if (!modules.isEmpty()) {
                        FragmentVO linkedModule = modules.get(0);

                        if (linkedModule != null) {
                            if (!uniqueModules.contains(linkedModule.getIdNode())) {
                                interviewQuestionService.updateIntQ(createInterviewAJSMQuestion(possibleAnswerVO,
                                        moduleVO,
                                        linkedModule,
                                        newInterview.getInterviewId(),
                                        questionCounter.incrementAndGet()));
                                uniqueModules.add(linkedModule.getIdNode());
                            }

                        }

                    } else {
                        log.error("Cant find linked module {}", idNode);
                    }
                });
            }

            List<QuestionVO> questions = questionService.getQuestionsWithSingleChildLevel(Long.valueOf(possibleAnswerVO.getParentId()));
            if (!questions.isEmpty()) {
                QuestionVO questionVO = questions.get(0);
                InterviewQuestionVO interviewQuestion =
                        interviewQuestionService.updateIntQ(createInterviewQuestion(questionVO,
                                newInterview.getInterviewId(),
                                questionCounter.incrementAndGet()));
                interviewAnswerService.saveOrUpdate(createInterviewAnswer(
                        newInterview.getInterviewId(),
                        possibleAnswerVO,
                        interviewQuestion.getId()));
            } else {
                log.error("Unable to find question in survey {} with answer number {} and parent id node {}",
                        answerNumber, newInterview.getReferenceNumber(), possibleAnswerVO.getParentId());
            }
        }
    }

    private void createInterviewQuestionFromQuestion(QuestionVO questionVO, long interviewId, AtomicInteger questionCounter) {
        InterviewQuestionVO interviewQuestion =
                interviewQuestionService.updateIntQ(createInterviewQuestion(questionVO,
                        interviewId,
                        questionCounter.incrementAndGet()));
        final PossibleAnswerVO possibleAnswerVO = questionVO.getChildNodes().get(0);
        interviewAnswerService.saveOrUpdate(createInterviewAnswer(
                interviewId,
                possibleAnswerVO,
                interviewQuestion.getId()));
        if (!possibleAnswerVO.getChildNodes().isEmpty()) {
            createInterviewQuestionFromQuestion(possibleAnswerVO.getChildNodes().get(0), interviewId, questionCounter);
        }

    }

    private QuestionVO getParentQuestionsFromFirstAnswer(String answer, NodeVO topNode, InterviewVO newInterview) {
        String values[] = answer.toString().split(" ");
        String splitAnswer[] = values[0].split("_");
        final String moduleKey = splitAnswer[0].replace("[", "");
        final String answerNumber = splitAnswer[1];

        System.out.println(answer);
        Optional<NodeVO> node = getNodeByModuleKey(moduleKey);
        if (node.isPresent()) {
            QuestionVO linkQuestion = questionService
                    .getNearestQuestionByLinkIdAndTopId(node.get().getIdNode(), topNode.getIdNode());
            PossibleAnswerVO parentAnswer = possibleAnswerService.findByIdExcludeChildren(Long.valueOf(linkQuestion.getParentId()));
            QuestionVO parentQuestion = questionService.findByIdExcludeChildren(Long.valueOf(parentAnswer.getParentId()));
            parentQuestion.setChildNodes(Arrays.asList(parentAnswer));
            if (Long.valueOf(parentQuestion.getParentId()) != topNode.getIdNode()) {
                return queueUntilTopQuestion(topNode.getIdNode(), parentQuestion);
            } else {
                return parentQuestion;
            }
        } else {
            throw new NodeNotFoundException(topNode.getIdNode());
        }
    }

    private QuestionVO queueUntilTopQuestion(long topNodeId, QuestionVO questionVO) {
        final Long parentId = Long.valueOf(questionVO.getParentId());
        if (topNodeId == parentId) {
            return questionVO;
        }
        PossibleAnswerVO possibleAnswerVO = possibleAnswerService.findByIdExcludeChildren(parentId);
        possibleAnswerVO.setChildNodes(Arrays.asList(questionVO));
        final long answerParentId = Long.valueOf(possibleAnswerVO.getParentId());
        QuestionVO parentQuestion = questionService.findByIdExcludeChildren(answerParentId);
        parentQuestion.setChildNodes(Arrays.asList(possibleAnswerVO));
        return queueUntilTopQuestion(topNodeId, parentQuestion);
    }

    private Optional<NodeVO> getNodeByModuleKey(String moduleKey) {
        Optional<NodeVO> node =
                Optional.ofNullable(moduleService.getModuleByNameLength(moduleKey, 4));
        if (!node.isPresent()) {
            node = Optional.ofNullable(fragmentService.getModuleByNameLength(moduleKey, 4));
        }
        return node;
    }

    private void createIntroQuestion(InterviewVO newInterview, NodeVO node) {
        InterviewQuestionVO introInterviewQuestionVO = new InterviewQuestionVO();
        introInterviewQuestionVO.setDeleted(0);
        introInterviewQuestionVO.setDescription(node.getDescription());
        introInterviewQuestionVO.setIdInterview(newInterview.getInterviewId());
        introInterviewQuestionVO.setIntQuestionSequence(1);
        introInterviewQuestionVO.setLink(node.getIdNode());
        introInterviewQuestionVO.setName(node.getName());
        introInterviewQuestionVO.setNodeClass("M");
        introInterviewQuestionVO.setNumber("1");
        introInterviewQuestionVO.setParentModuleId(0);
        introInterviewQuestionVO.setProcessed(true);
        introInterviewQuestionVO.setTopNodeId(node.getIdNode());
        introInterviewQuestionVO.setType("M_IntroModule");

        interviewQuestionService.updateIntQ(introInterviewQuestionVO);
    }

    public InterviewVO createNewInterview(String referenceNumber, ParticipantVO participantVO) {
        InterviewVO interviewVO = new InterviewVO();
        interviewVO.setParticipant(participantVO);
        interviewVO.setReferenceNumber(referenceNumber);
        return interviewService.create(interviewVO);
    }

    public ParticipantVO createParticipant(String referenceNumber) {
        ParticipantVO partVO = new ParticipantVO();
        partVO.setReference(referenceNumber);
        partVO.setStatus(2);
        return participantService.create(partVO);
    }

    private void consumeQSFModuleResponse(SurveyResponses surveyResponses,
                                          NodeVO nodeVO,
                                          InterviewVO newInterview) {
        if (isEmptyResponse(surveyResponses)) {
            return;
        }

        final Response response = surveyResponses.getResponses().get(0);

        Map<String, Optional<NodeVO>> storage = new HashMap<String, Optional<NodeVO>>();
        AtomicInteger questionCounter = new AtomicInteger();
        response.getLabels().forEach((key, value) -> {
            if (key.contains(QID)) {
                if (value instanceof List) {
                    value = ((List) value).get(0).toString();
                }
                String values[] = value.toString().split(" ");
                String splitAnswer[] = values[0].split("_");
                final String moduleKey = splitAnswer[0];
                final String answerNumber = splitAnswer[1];

                NodeVO newNodeVO = nodeVO;
                if (!moduleKey.equals(newNodeVO.getName().substring(0, 4))) {
                    newNodeVO = fragmentService.getModuleByNameLength(moduleKey, 4);
                    storage.put(String.valueOf(newNodeVO.getIdNode()), Optional.ofNullable(newNodeVO));
                    PossibleAnswerVO fragmentAnswerVO = possibleAnswerService
                            .findByTopNodeIdAndNumber(newNodeVO.getIdNode(), answerNumber);

                    List<QuestionVO> questions = questionService.getQuestionsWithSingleChildLevel(Long.valueOf(fragmentAnswerVO.getParentId()));
                    if (!questions.isEmpty()) {
                        QuestionVO questionVO = questions.get(0);
                        interviewQuestionService.updateIntQ(createInterviewAJSMQuestion(questionVO,
                                nodeVO, newNodeVO,
                                newInterview.getInterviewId(),
                                questionCounter.incrementAndGet()));
                    }
                }
                PossibleAnswerVO possibleAnswerVO = possibleAnswerService
                        .findByTopNodeIdAndNumber(newNodeVO.getIdNode(), answerNumber);

                List<QuestionVO> questions = questionService.getQuestionsWithSingleChildLevel(Long.valueOf(possibleAnswerVO.getParentId()));
                if (!questions.isEmpty()) {
                    QuestionVO questionVO = questions.get(0);
                    InterviewQuestionVO interviewQuestion =
                            interviewQuestionService.updateIntQ(createInterviewQuestion(questionVO,
                                    newInterview.getInterviewId(),
                                    questionCounter.incrementAndGet()));
                    interviewAnswerService.saveOrUpdate(createInterviewAnswer(
                            newInterview.getInterviewId(),
                            possibleAnswerVO,
                            interviewQuestion.getId()));
                } else {
                    log.error("Unable to find question in survey {} with answer number {} and parent id node {}", response.getResponseId(), answerNumber, possibleAnswerVO.getParentId());
                }
            }
        });
    }

    private boolean isEmptyResponse(SurveyResponses surveyResponses) {
        if (surveyResponses.getResponses().isEmpty()) {
            log.error("Response is empty for json object", surveyResponses);
            return true;
        }
        return false;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void importQSFResponses() {
        List<NodeQSF> nodeQSF = dao.list();
        SystemProperty activeIntro = systemPropertyDao.getByName("activeIntro");
        nodeQSF.stream().forEach(qsf -> {
            try {
                if (activeIntro == null || !Long.valueOf(activeIntro.getValue()).equals(qsf.getIdNode())) {
                    processResponseForSurvey(qsf);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void importQSFResponsesForIntro() {
        SystemProperty activeIntro = systemPropertyDao.getByName("activeIntro");
        if (activeIntro == null || !StringUtils.isNumeric(activeIntro.getValue())) {
            log.error("Active intro is either null or is not numeric");
            return;
        }
        try {
            processResponseForSurvey(this.getByIdNode(Long.valueOf(activeIntro.getValue())));
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void cleanSurveyResponses() {
        dao.cleanSurveyResponses();
    }

    private InterviewAnswerVO createInterviewAnswer(long interviewId, PossibleAnswerVO possibleAnswerVO, long interviewQID) {
        InterviewAnswerVO interviewAnswerVO = new InterviewAnswerVO();
        interviewAnswerVO.setName(possibleAnswerVO.getName());
        interviewAnswerVO.setIsProcessed(true);
        interviewAnswerVO.setAnswerFreetext(possibleAnswerVO.getName());
        interviewAnswerVO.setAnswerId(possibleAnswerVO.getIdNode());
        interviewAnswerVO.setDeleted(0);
        interviewAnswerVO.setModCount(1);
        interviewAnswerVO.setIdInterview(interviewId);
        interviewAnswerVO.setInterviewQuestionId(interviewQID);
        interviewAnswerVO.setNodeClass(possibleAnswerVO.getNodeclass());
        interviewAnswerVO.setNumber(possibleAnswerVO.getNumber());
        interviewAnswerVO.setParentQuestionId(Optional.ofNullable(Long.valueOf(possibleAnswerVO.getParentId())).orElse(0L));
        interviewAnswerVO.setTopNodeId(possibleAnswerVO.getTopNodeId());
        interviewAnswerVO.setType(possibleAnswerVO.getType());
        interviewAnswerVO.setDescription(possibleAnswerVO.getDescription());
        interviewAnswerVO.setAnswerId(possibleAnswerVO.getIdNode());
        return interviewAnswerVO;
    }

    private InterviewQuestionVO createInterviewQuestion(NodeVO node, long interviewId, int sequence) {
        InterviewQuestionVO interviewQuestionVO = new InterviewQuestionVO();
        interviewQuestionVO.setDeleted(0);
        interviewQuestionVO.setDescription(node.getDescription());
        interviewQuestionVO.setIdInterview(interviewId);
        interviewQuestionVO.setIntQuestionSequence(sequence);
        interviewQuestionVO.setLink(node.getLink());
        interviewQuestionVO.setName(node.getName());
        interviewQuestionVO.setNodeClass(node.getNodeclass());
        interviewQuestionVO.setNumber(node.getNumber());
        interviewQuestionVO.setQuestionId(node.getIdNode());
        interviewQuestionVO.setModCount(1);
        interviewQuestionVO.setType(node.getType());
        interviewQuestionVO.setParentModuleId(node.getTopNodeId());
        if (node.getTopNodeId() != Long.valueOf(node.getParentId())) {
            interviewQuestionVO.setParentAnswerId(Long.valueOf(Optional.ofNullable(node.getParentId()).orElse("0")));
        }
        interviewQuestionVO.setProcessed(true);
        interviewQuestionVO.setTopNodeId(node.getTopNodeId());
        return interviewQuestionVO;
    }

    private InterviewQuestionVO createInterviewIntroModuleQuestion(NodeVO nodeVO, long interviewId, int sequence) {
        InterviewQuestionVO interviewQuestionVO = new InterviewQuestionVO();
        interviewQuestionVO.setDeleted(0);
        interviewQuestionVO.setDescription(nodeVO.getDescription());
        interviewQuestionVO.setIdInterview(interviewId);
        interviewQuestionVO.setIntQuestionSequence(sequence);
        interviewQuestionVO.setLink(nodeVO.getIdNode());
        interviewQuestionVO.setName(nodeVO.getName());
        interviewQuestionVO.setNodeClass("M");
        interviewQuestionVO.setNumber("0");
        interviewQuestionVO.setQuestionId(0);
        interviewQuestionVO.setModCount(0);
        interviewQuestionVO.setType(nodeVO.getType());
        interviewQuestionVO.setParentModuleId(nodeVO.getIdNode());
        interviewQuestionVO.setParentAnswerId(0L);
        interviewQuestionVO.setProcessed(true);
        interviewQuestionVO.setTopNodeId(nodeVO.getIdNode());
        interviewQuestionVO.setType(nodeVO.getType());
        return interviewQuestionVO;
    }

    private InterviewQuestionVO createInterviewModuleQuestion(NodeVO node, NodeVO linkedModule, long interviewId, int sequence) {
        InterviewQuestionVO interviewQuestionVO = new InterviewQuestionVO();
        interviewQuestionVO.setDeleted(0);
        interviewQuestionVO.setDescription(linkedModule.getDescription());
        interviewQuestionVO.setIdInterview(interviewId);
        interviewQuestionVO.setIntQuestionSequence(sequence);
        interviewQuestionVO.setLink(linkedModule.getIdNode());
        interviewQuestionVO.setName(linkedModule.getName());
        interviewQuestionVO.setNodeClass(null);
        interviewQuestionVO.setNumber(node.getNumber());
        interviewQuestionVO.setQuestionId(0);
        interviewQuestionVO.setModCount(1);
        // interviewQuestionVO.setType(node.getType());
        interviewQuestionVO.setParentModuleId(0L);
        if (node.getTopNodeId() != Long.valueOf(node.getParentId())) {
            interviewQuestionVO.setParentAnswerId(Optional.ofNullable(node.getIdNode()).orElse(0L));
        }
        interviewQuestionVO.setProcessed(true);
        interviewQuestionVO.setTopNodeId(linkedModule.getIdNode());
        interviewQuestionVO.setType(QSFNodeTypeMapper.Q_LINKEDMODULE.getDescription());
        return interviewQuestionVO;
    }

    private InterviewQuestionVO createInterviewAJSMQuestion(NodeVO question, NodeVO module,
                                                            NodeVO linkedAJSM, long interviewId, int sequence) {
        InterviewQuestionVO interviewQuestionVO = new InterviewQuestionVO();
        interviewQuestionVO.setDeleted(0);
        interviewQuestionVO.setDescription(linkedAJSM.getDescription());
        interviewQuestionVO.setIdInterview(interviewId);
        interviewQuestionVO.setIntQuestionSequence(sequence);
        interviewQuestionVO.setLink(linkedAJSM.getIdNode());
        interviewQuestionVO.setName(linkedAJSM.getName());
        interviewQuestionVO.setNodeClass(null);
        interviewQuestionVO.setNumber("1");
        interviewQuestionVO.setQuestionId(0);
        interviewQuestionVO.setModCount(1);
        interviewQuestionVO.setParentModuleId(module.getIdNode());
        interviewQuestionVO.setParentAnswerId(0L);
        interviewQuestionVO.setProcessed(true);
        interviewQuestionVO.setTopNodeId(linkedAJSM.getIdNode());
        interviewQuestionVO.setType(QSFNodeTypeMapper.Q_LINKEDAJSM.getDescription());
        return interviewQuestionVO;
    }


    private void processResponseForSurvey(NodeQSF nodeQSF) throws InterruptedException {
        SurveyExportRequest surveyExportRequest = new SurveyExportRequest();
        surveyExportRequest.setFormat("json");
        javax.ws.rs.core.Response response = iqsfClient.createExportResponse(nodeQSF.getSurveyId(), surveyExportRequest);
        if (!(response.getEntity() instanceof SurveyExportResponse)) {
            log.error(response.getEntity());
            return;
        }
        String fileId = monitorExportProgress(nodeQSF.getSurveyId(), response);
        if (!StringUtils.isEmpty(fileId)) {
            File surveyJsonFile = processDownloadedFile(nodeQSF, fileId);
            consumeJSONfile(surveyJsonFile);
        }
    }

    private void consumeJSONfile(File surveyJsonFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SurveyResponses surveyResponses = objectMapper.readValue(surveyJsonFile, SurveyResponses.class);
            log.info(surveyResponses.toString());
            this.consumeQSFResponse(surveyResponses);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private File processDownloadedFile(NodeQSF nodeQSF, String fileId) {
        File file = iqsfClient.getExportResponseFile(nodeQSF.getSurveyId(), fileId);
        log.info("File is successfully exported here " + file.getAbsolutePath());
        this.save(nodeQSF.getSurveyId(), nodeQSF.getIdNode(), file.getAbsolutePath());
        File responseDir = null;
        try {
            File newDirectory = new File(file.getParent(), FilenameUtils.removeExtension(file.getName()));
            if (!newDirectory.exists()) {
                newDirectory.mkdir();
            }
            ZipUtil.unzip(file, newDirectory.getAbsolutePath());
            responseDir = newDirectory;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        File[] files = responseDir.listFiles();
        File surveyJsonFile = null;
        for (File survey : files) {
            if ("json".equals(FilenameUtils.getExtension(survey.getName()))) {
                surveyJsonFile = survey;
                break;
            }
        }
        return surveyJsonFile;
    }

    private File processDownloadedFile(String surveyId, String fileId) {
        File file = iqsfClient.getExportResponseFile(surveyId, fileId);
        log.info("File is successfully exported here " + file.getAbsolutePath());
        File responseDir = null;
        try {
            File newDirectory = new File(file.getParent(), FilenameUtils.removeExtension(file.getName()));
            if (!newDirectory.exists()) {
                newDirectory.mkdir();
            }
            ZipUtil.unzip(file, newDirectory.getAbsolutePath());
            responseDir = newDirectory;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        File[] files = responseDir.listFiles();
        File surveyJsonFile = null;
        for (File survey : files) {
            if ("json".equals(FilenameUtils.getExtension(survey.getName()))) {
                surveyJsonFile = survey;
                break;
            }
        }
        return surveyJsonFile;
    }

    private String monitorExportProgress(String surveyId, javax.ws.rs.core.Response response) throws InterruptedException {
        Object entity = response.getEntity();
        SurveyExportResponse exportResponse = null;
        if (entity instanceof SurveyExportResponse) {
            exportResponse = (SurveyExportResponse) entity;
        } else {
            throw new GenericException("Unable to process survey id " + surveyId + ", check the logs.");
        }

        int tries = 0;
        String fileId = null;
        while (true) {
            log.info("Export Progress:" + (exportResponse.getResult().getPercentComplete() * 100) + "%");
            if (!isResponseProgressValid(response)) {
                response = iqsfClient.getExportResponseProgress(surveyId, exportResponse.getResult().getProgressId());
                continue;
            }
            javax.ws.rs.core.Response exportProgress = iqsfClient.getExportResponseProgress(surveyId, exportResponse.getResult().getProgressId());
            if (Objects.nonNull(exportProgress)) {
                exportResponse = (SurveyExportResponse) exportProgress.getEntity();
            }
            if (!StringUtils.isEmpty(exportResponse.getResult().getFileId())) {
                fileId = exportResponse.getResult().getFileId();
                break;
            }
            if (tries == 20) {
                log.error("Stop check export qualtrics, seems its down or failed... tried " + tries + " times.");
                break;
            }
            tries++;
            Thread.currentThread().sleep(5000);
        }
        log.info("export in qualtrics has been completed , tried to check " + tries + " times.");
        return fileId;
    }

    private boolean isResponseProgressValid(javax.ws.rs.core.Response response) {
        SurveyExportResponse exportResponse = (SurveyExportResponse) response.getEntity();
        return Objects.nonNull(exportResponse) &&
                Objects.nonNull(exportResponse.getResult()) &&
                Objects.nonNull(exportResponse.getResult().getProgressId());
    }
}
