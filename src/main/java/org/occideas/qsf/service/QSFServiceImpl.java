package org.occideas.qsf.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.Constant;
import org.occideas.entity.NodeQSF;
import org.occideas.entity.SystemProperty;
import org.occideas.fragment.service.FragmentService;
import org.occideas.interview.dao.IInterviewDao;
import org.occideas.interview.service.InterviewService;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.module.dao.IModuleDao;
import org.occideas.module.service.ModuleService;
import org.occideas.participant.service.ParticipantService;
import org.occideas.possibleanswer.service.PossibleAnswerService;
import org.occideas.qsf.IQSFClient;
import org.occideas.qsf.dao.INodeQSFDao;
import org.occideas.qsf.request.SurveyExportRequest;
import org.occideas.qsf.response.SurveyExportResponse;
import org.occideas.qsf.results.Response;
import org.occideas.qsf.results.SurveyResponses;
import org.occideas.question.service.QuestionService;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.ZipUtil;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    private IInterviewDao interviewDao;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private FragmentService fragmentService;
    @Autowired
    private PossibleAnswerService possibleAnswerService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private InterviewQuestionService interviewQuestionService;
    @Autowired
    private InterviewAnswerService interviewAnswerService;
    @Autowired
    private SystemPropertyService systemPropertyService;
    @Autowired
    private IModuleDao moduleDao;
    @Autowired
    private INodeQSFDao nodeQSFDao;
    @Autowired
    private IQSFClient iqsfClient;
    @Autowired
    private SystemPropertyDao systemPropertyDao;

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
    public void exportResponseQSF(Long id) throws InterruptedException {
        List<ModuleVO> modules = moduleService.findById(id);
        if (!modules.isEmpty()) {
            this.consumeQSFResponse(this.exportQSFResponses(modules.get(0).getIdNode()));
        }
    }

    @Override
    public SurveyResponses exportQSFResponses(long idNode) throws InterruptedException {
        String surveyId = this.getSurveyIdByIdNode(idNode);
        SurveyExportRequest surveyExportRequest = new SurveyExportRequest();
        surveyExportRequest.setFormat("json");
        javax.ws.rs.core.Response response = iqsfClient.createExportResponse(surveyId, surveyExportRequest);
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

    @Override
    public void consumeQSFResponse(SurveyResponses surveyResponses) {
        if (isEmptyResponse(surveyResponses)) {
            return;
        }

        final Response response = surveyResponses.getResponses().get(0);
        String referenceNumber = String.valueOf(response.getResponseId());
        ParticipantVO partVO = new ParticipantVO();
        partVO.setReference(referenceNumber);
        partVO.setStatus(2);
        ParticipantVO participantVO = participantService.create(partVO);


        InterviewVO interviewVO = new InterviewVO();
        interviewVO.setParticipant(participantVO);
        interviewVO.setReferenceNumber(referenceNumber);
        InterviewVO newInterview = interviewService.create(interviewVO);

        Map<String, Optional<NodeVO>> storage = new HashMap<String, Optional<NodeVO>>();
        AtomicInteger questionCounter = new AtomicInteger();
        response.getLabels().forEach((key, value) -> {
            if (key.contains(QID)) {
                String values[] = value.toString().split(" ");
                String splitAnswer[] = values[0].split("_");
                final String moduleKey = splitAnswer[0];
                final String answerNumber = splitAnswer[1];
                Optional<NodeVO> module = Optional.ofNullable(storage.get(moduleKey))
                        .orElse(Optional.ofNullable(moduleService.getModuleByNameLength(moduleKey, 4)));
                if (module.isPresent()) {
                    NodeVO moduleVO = module.get();
                    if (!storage.containsKey(moduleKey)) {
                        storage.put(moduleKey, Optional.of(module.get()));
                        interviewQuestionService.updateIntQ(createInterviewIntroModuleQuestion(moduleVO,
                                newInterview.getInterviewId(), questionCounter.incrementAndGet()));
                    }

                    PossibleAnswerVO possibleAnswerVO = possibleAnswerService
                            .findByTopNodeIdAndNumber(moduleVO.getIdNode(), answerNumber);

                    final Set<Long> linkModules = possibleAnswerVO.getChildNodes().stream().filter(questions -> {
                        return questions.getLink() > 0L;
                    }).map(QuestionVO::getLink).collect(Collectors.toSet());

                    if (!linkModules.isEmpty()) {
                        linkModules.forEach(idNode -> {
                            final List<ModuleVO> modules = moduleService.findByIdForInterview(idNode);
                            if (!modules.isEmpty()) {
                                ModuleVO linkedModule = modules.get(0);
                                storage.put(String.valueOf(idNode), Optional.ofNullable(linkedModule));
                                interviewQuestionService.updateIntQ(createInterviewModuleQuestion(possibleAnswerVO,
                                        linkedModule,
                                        newInterview.getInterviewId(),
                                        questionCounter.incrementAndGet()));
                                try {
                                    final SurveyResponses moduleSurveyResponses =
                                            this.exportQSFResponses(linkedModule.getIdNode());
                                    consumeQSFModuleResponse(moduleSurveyResponses, linkedModule, newInterview);
                                } catch (InterruptedException e) {
                                    log.error(e.getMessage(), e);
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
                        log.error("Unable to find question in survey {} with answer number {} and parent id node {}", answerNumber, referenceNumber, possibleAnswerVO.getParentId());
                    }
                }

            }
        });
    }

    private void consumeQSFModuleResponse(SurveyResponses surveyResponses,
                                          ModuleVO moduleVO,
                                          InterviewVO newInterview) {
        if (isEmptyResponse(surveyResponses)) {
            return;
        }

        final Response response = surveyResponses.getResponses().get(0);

        Map<String, Optional<NodeVO>> storage = new HashMap<String, Optional<NodeVO>>();
        AtomicInteger questionCounter = new AtomicInteger();
        response.getLabels().forEach((key, value) -> {
            if (key.contains(QID)) {
                String values[] = value.toString().split(" ");
                String splitAnswer[] = values[0].split("_");
                final String moduleKey = splitAnswer[0];
                final String answerNumber = splitAnswer[1];
                PossibleAnswerVO possibleAnswerVO = possibleAnswerService
                        .findByTopNodeIdAndNumber(moduleVO.getIdNode(), answerNumber);

                final Set<Long> linkAJSM = possibleAnswerVO.getChildNodes().stream().filter(questions -> {
                    return questions.getLink() > 0L;
                }).map(QuestionVO::getLink).collect(Collectors.toSet());

                if (!linkAJSM.isEmpty()) {
                    linkAJSM.forEach(idNode -> {
                        final List<FragmentVO> fragments = fragmentService.findByIdForInterview(idNode);
                        if (!fragments.isEmpty()) {
                            FragmentVO linkedAJSM = fragments.get(0);
                            storage.put(String.valueOf(idNode), Optional.ofNullable(linkedAJSM));
                            interviewQuestionService.updateIntQ(createInterviewModuleQuestion(possibleAnswerVO,
                                    linkedAJSM,
                                    newInterview.getInterviewId(),
                                    questionCounter.incrementAndGet()));
                            try {
                                final SurveyResponses moduleSurveyResponses =
                                        this.exportQSFResponses(linkedAJSM.getIdNode());
                                consumeQSFModuleResponse(moduleSurveyResponses,
                                        moduleVO,
                                        newInterview);
                            } catch (InterruptedException e) {
                                log.error(e.getMessage(), e);
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
        interviewQuestionVO.setType(node.getType());
        interviewQuestionVO.setParentModuleId(0L);
        if (node.getTopNodeId() != Long.valueOf(node.getParentId())) {
            interviewQuestionVO.setParentAnswerId(Optional.ofNullable(node.getIdNode()).orElse(0L));
        }
        interviewQuestionVO.setProcessed(true);
        interviewQuestionVO.setTopNodeId(linkedModule.getIdNode());
        interviewQuestionVO.setType(Constant.Q_LINKEDMODULE);
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

        String fileId = monitorExportProgress(nodeQSF, response);
        File surveyJsonFile = processDownloadedFile(nodeQSF, fileId);
        consumeJSONfile(surveyJsonFile);
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

    private String monitorExportProgress(NodeQSF nodeQSF, javax.ws.rs.core.Response response) throws InterruptedException {
        SurveyExportResponse exportResponse = (SurveyExportResponse) response.getEntity();
        int tries = 0;
        String fileId = null;
        while (true) {
            log.info("Export Progress:" + (exportResponse.getResult().getPercentComplete() * 100) + "%");
            javax.ws.rs.core.Response exportProgress = iqsfClient.getExportResponseProgress(nodeQSF.getSurveyId(), exportResponse.getResult().getProgressId());
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
        return fileId;
    }
}
