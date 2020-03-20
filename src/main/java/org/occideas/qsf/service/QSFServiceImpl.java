package org.occideas.qsf.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.Constant;
import org.occideas.entity.Node;
import org.occideas.entity.NodeQSF;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@Service
public class QSFServiceImpl implements IQSFService{

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

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String save(String surveyId, long idNode, String path) {
        return dao.save(surveyId,idNode,path);
    }

    @Override
    public String getByIdNode(long idNode) {
        return dao.getByIdNode(idNode);
    }

    @Override
    public void consumeQSFResponse(SurveyResponses surveyResponses) {
        if(surveyResponses.getResponses().isEmpty()){
            log.error("Response is empty for json object",surveyResponses);
            return;
        }

        final Response response = surveyResponses.getResponses().get(0);
        String referenceNumber = String.valueOf(response.getResponseId());
        ParticipantVO partVO = new ParticipantVO();
        partVO.setReference(referenceNumber);
        partVO.setStatus(2);
        ParticipantVO participantVO = participantService.create(partVO);
        
        long interviewId = 0;
        InterviewVO interviewVO = new InterviewVO();
        interviewVO.setParticipant(participantVO);
        SystemPropertyVO introModule = systemPropertyService.getByName(Constant.STUDY_INTRO);
        ModuleVO introModuleVO = new ModuleVO();
        if (introModule == null) {
          log.error("no intro module set");
          return;
        } else {
          Node node = moduleDao.getNodeById(Long.valueOf(introModule.getValue()));
          
          introModuleVO.setIdNode(node.getIdNode());
          introModuleVO.setName(node.getName());
          introModuleVO.setDescription(node.getDescription());
          
          interviewVO.setModule(introModuleVO);
          
        }
        
        interviewVO.setReferenceNumber(referenceNumber);
        InterviewVO newInterview = interviewService.create(interviewVO);
        interviewId = newInterview.getInterviewId();

        InterviewQuestionVO introInterviewQuestionVO = new InterviewQuestionVO();
        introInterviewQuestionVO.setDeleted(0);
        introInterviewQuestionVO.setDescription(introModuleVO.getDescription());
        introInterviewQuestionVO.setIdInterview(interviewId);
        introInterviewQuestionVO.setIntQuestionSequence(1);
        introInterviewQuestionVO.setLink(introModuleVO.getIdNode());
        introInterviewQuestionVO.setName(introModuleVO.getName());
        introInterviewQuestionVO.setNodeClass("M");
        introInterviewQuestionVO.setNumber("1");
        introInterviewQuestionVO.setParentModuleId(0);
        introInterviewQuestionVO.setProcessed(true);
        introInterviewQuestionVO.setTopNodeId(introModuleVO.getIdNode());
        introInterviewQuestionVO.setType("M_IntroModule");
		// interviewQuestionVO.setParentAnswerId(Optional.ofNullable(Long.valueOf(questionVO.getParentId())).orElse(0L));
		// interviewQuestionVO.setQuestionId(questionVO.getIdNode());
		interviewQuestionService.updateIntQ(introInterviewQuestionVO);

        String prefix = "QID";
        int count = 1;
        long oldModuleId = 0;
        while(response.getLabels().containsKey(prefix+(count))){
            String value = response.getLabels().get(prefix+(count)).toString();
            String values[] = value.split(" ");
            String chosenAnswer = values[0];
            String splitAnswer[] = chosenAnswer.split("_");
            String moduleKey = splitAnswer[0].replace("[", "");
            String answerNumber = splitAnswer[1];

            // get module
            long moduleId = 0;
            ModuleVO moduleVO = moduleService.getModuleByNameLength(moduleKey,4);
            
            if(moduleVO == null){
            	FragmentVO fragmentVO = fragmentService.getModuleByNameLength(moduleKey,4);
                long fragementId = fragmentVO.getIdNode();
                moduleId = fragementId;
				if (oldModuleId != moduleId) {
					oldModuleId = moduleId;
					InterviewQuestionVO interviewQuestionVO = new InterviewQuestionVO();
					interviewQuestionVO.setDeleted(0);
					interviewQuestionVO.setDescription(fragmentVO.getDescription());
					interviewQuestionVO.setIdInterview(interviewId);
					interviewQuestionVO.setIntQuestionSequence(count);
					interviewQuestionVO.setLink(fragmentVO.getIdNode());
					interviewQuestionVO.setName(fragmentVO.getName());
					interviewQuestionVO.setNodeClass(fragmentVO.getNodeclass());
					interviewQuestionVO.setNumber(fragmentVO.getNumber());
					interviewQuestionVO.setParentModuleId(fragmentVO.getIdNode());
					interviewQuestionVO.setProcessed(true);
					interviewQuestionVO.setTopNodeId(fragmentVO.getIdNode());
					interviewQuestionVO.setType("Q_linkedajsm");
					// interviewQuestionVO.setParentAnswerId(Optional.ofNullable(Long.valueOf(questionVO.getParentId())).orElse(0L));
					// interviewQuestionVO.setQuestionId(questionVO.getIdNode());
					interviewQuestionService.updateIntQ(interviewQuestionVO);
				}
            }else{
            	moduleId = moduleVO.getIdNode();
				if (oldModuleId != moduleId) {
					oldModuleId = moduleId;
					InterviewQuestionVO interviewQuestionVO = new InterviewQuestionVO();
					interviewQuestionVO.setDeleted(0);
					interviewQuestionVO.setDescription(moduleVO.getDescription());
					interviewQuestionVO.setIdInterview(interviewId);
					interviewQuestionVO.setIntQuestionSequence(count);
					interviewQuestionVO.setLink(moduleVO.getIdNode());
					interviewQuestionVO.setName(moduleVO.getName());
					interviewQuestionVO.setNodeClass(moduleVO.getNodeclass());
					interviewQuestionVO.setNumber(moduleVO.getNumber());
					interviewQuestionVO.setParentModuleId(moduleVO.getIdNode());
					interviewQuestionVO.setProcessed(true);
					interviewQuestionVO.setTopNodeId(moduleVO.getIdNode());
					interviewQuestionVO.setType("Q_linkedmodule");
					// interviewQuestionVO.setParentAnswerId(Optional.ofNullable(Long.valueOf(questionVO.getParentId())).orElse(0L));
					// interviewQuestionVO.setQuestionId(questionVO.getIdNode());
					interviewQuestionService.updateIntQ(interviewQuestionVO);
				}
            }
            
            // get answer with module id and answer number
            PossibleAnswerVO possibleAnswerVO = possibleAnswerService.findByTopNodeIdAndNumber(moduleId,answerNumber);
            // get question from module based on answer parent_idNode
            List<QuestionVO> questionList = questionService.findById(Long.valueOf(possibleAnswerVO.getParentId()));
            QuestionVO questionVO =  null;
            if(questionList.isEmpty()){
                log.error("Question {} does not exist",possibleAnswerVO.getParentId());
                continue;
            }else{
                questionVO = questionList.get(0);
            }
            if(count == 1) {
            }
            InterviewQuestionVO interviewQuestionVO = new InterviewQuestionVO();
            interviewQuestionVO.setDeleted(0);
            interviewQuestionVO.setDescription(questionVO.getDescription());
            interviewQuestionVO.setIdInterview(interviewId);
            interviewQuestionVO.setIntQuestionSequence(count);
            interviewQuestionVO.setLink(questionVO.getLink());
            interviewQuestionVO.setName(questionVO.getName());
            interviewQuestionVO.setNodeClass(questionVO.getNodeclass());
            interviewQuestionVO.setNumber(questionVO.getNumber());
            interviewQuestionVO.setParentModuleId(questionVO.getTopNodeId());
            interviewQuestionVO.setProcessed(true);
            interviewQuestionVO.setTopNodeId(questionVO.getTopNodeId());
            interviewQuestionVO.setType(questionVO.getType());
            interviewQuestionVO.setParentAnswerId(Optional.ofNullable(Long.valueOf(questionVO.getParentId())).orElse(0L));
            interviewQuestionVO.setQuestionId(questionVO.getIdNode());
            interviewQuestionService.updateIntQ(interviewQuestionVO);

            InterviewAnswerVO interviewAnswerVO = new InterviewAnswerVO();
            interviewAnswerVO.setName(possibleAnswerVO.getName());
            interviewAnswerVO.setIsProcessed(true);
            interviewAnswerVO.setAnswerFreetext(possibleAnswerVO.getName());
            interviewAnswerVO.setAnswerId(possibleAnswerVO.getIdNode());
            interviewAnswerVO.setDeleted(0);
            interviewAnswerVO.setIdInterview(interviewId);
            interviewAnswerVO.setInterviewQuestionId(questionVO.getIdNode());
            interviewAnswerVO.setNodeClass(possibleAnswerVO.getNodeclass());
            interviewAnswerVO.setNumber(possibleAnswerVO.getNumber());
            interviewAnswerVO.setParentQuestionId(Optional.ofNullable(Long.valueOf(possibleAnswerVO.getParentId())).orElse(0L));
            interviewAnswerVO.setTopNodeId(possibleAnswerVO.getTopNodeId());
            interviewAnswerVO.setType(possibleAnswerVO.getType());
            interviewAnswerVO.setDescription(possibleAnswerVO.getDescription());
            interviewAnswerVO.setAnswerId(possibleAnswerVO.getIdNode());
            interviewAnswerService.saveOrUpdate(interviewAnswerVO);

            count++;
        }
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void importQSFResponses() {
        List<NodeQSF> list = nodeQSFDao.list();
        AtomicInteger count = new AtomicInteger(1);
        list.stream().forEach(nodeQSF -> {
            try {
                log.info("{} of {} - survey Id {}",count.getAndIncrement(),list.size(),nodeQSF.getSurveyId());
                processResponseForSurvey(nodeQSF);
            } catch (InterruptedException e) {
                log.error(e.getMessage(),e);
            }
        });
    }

    private void processResponseForSurvey(NodeQSF nodeQSF) throws InterruptedException {
        SurveyExportRequest surveyExportRequest = new SurveyExportRequest();
        surveyExportRequest.setFormat("json");
        javax.ws.rs.core.Response response = iqsfClient.createExportResponse(nodeQSF.getSurveyId(), surveyExportRequest);
        if (response != null) {
            if (!(response.getEntity() instanceof SurveyExportResponse)) {
                log.error(response.getEntity());
                return;
            }

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
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                SurveyResponses surveyResponses = objectMapper.readValue(surveyJsonFile, SurveyResponses.class);
                log.info(surveyResponses.toString());
                this.consumeQSFResponse(surveyResponses);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
