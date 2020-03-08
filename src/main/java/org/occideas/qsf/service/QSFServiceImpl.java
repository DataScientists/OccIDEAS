package org.occideas.qsf.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.interview.dao.IInterviewDao;
import org.occideas.interview.service.InterviewService;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.module.service.ModuleService;
import org.occideas.participant.service.ParticipantService;
import org.occideas.possibleanswer.service.PossibleAnswerService;
import org.occideas.qsf.dao.INodeQSFDao;
import org.occideas.qsf.results.Response;
import org.occideas.qsf.results.SurveyResponses;
import org.occideas.question.service.QuestionService;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    private PossibleAnswerService possibleAnswerService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private InterviewQuestionService interviewQuestionService;
    @Autowired
    private InterviewAnswerService interviewAnswerService;

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

        String prefix = "QID";
        int count = 1;
        long interviewId = 0;
        while(response.getLabels().containsKey(prefix+(count))){
            String value = response.getLabels().get(prefix+(count)).toString();
            String values[] = value.split(" ");
            String chosenAnswer = values[0];
            String splitAnswer[] = chosenAnswer.split("_");
            String moduleKey = splitAnswer[0];
            String answerNumber = splitAnswer[1];

            // get module
            ModuleVO moduleVO = moduleService.getModuleByNameLength(moduleKey,4);
            long moduleId = moduleVO.getIdNode();
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
                InterviewVO interviewVO = new InterviewVO();
                interviewVO.setParticipant(participantVO);
                interviewVO.setModule(moduleVO);
                interviewVO.setReferenceNumber(referenceNumber);
                InterviewVO newInterview = interviewService.create(interviewVO);
                interviewId = newInterview.getInterviewId();
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

}
