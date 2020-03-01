package org.occideas.qsf.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.interview.dao.IInterviewDao;
import org.occideas.participant.service.ParticipantService;
import org.occideas.qsf.dao.INodeQSFDao;
import org.occideas.qsf.results.SurveyResponses;
import org.occideas.vo.ParticipantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
//        select * from participant
//        select * from Interview
//        select * from interview_answer
//        select * from interview_question
        if(surveyResponses.getResponses().isEmpty()){
            log.error("Response is empty for json object",surveyResponses);
            return;
        }

        String referenceNumber = String.valueOf(surveyResponses.getResponses().get(0).getResponseId());
        ParticipantVO partVO = new ParticipantVO();
        partVO.setReference(referenceNumber);
        ParticipantVO participantVO = participantService.create(partVO);
//        // create interview
//        InterviewVO interviewVO = new InterviewVO();
//        interviewVO.setParticipant(participantVO);
//        interviewVO.setModule(modVO);
//        interviewVO.setReferenceNumber(referenceNumber);
//        // set default assessed status
//        interviewVO.setAssessedStatus(AssessmentStatusEnum.NOTASSESSED.getDisplay());
//        Interview interviewEntity = mapper.convertToInterview(interviewVO);
//        interviewDao.saveNewTransaction(interviewEntity);
//        InterviewVO newInterviewVO = mapper.convertToInterviewVO(interviewEntity);
//        randomInterviewReport.setInterviewId(newInterviewVO.getInterviewId());
//        referenceNumber = generateReferenceAuto(referenceNumber);
//        // populate interview question by module
//        populateInterviewWithQuestions(modVO, results, randomInterviewReport, newInterviewVO, participantVO,
//                isRandomAnswers, filterModuleVO);
//
//        Interview interview = new Interview();
//        interviewDao.saveNewTransaction();
    }

}
