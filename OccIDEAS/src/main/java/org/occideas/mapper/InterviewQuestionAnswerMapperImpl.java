package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewQuestionAnswer;
import org.occideas.vo.InterviewQuestionAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterviewQuestionAnswerMapperImpl implements InterviewQuestionAnswerMapper {
    
    @Autowired
	private QuestionMapper questionMapper;
    
    @Autowired
	private PossibleAnswerMapper possibleAnswerMapper;
    
    @Autowired
	private InterviewMapper interviewMapper;

    @Override
    public InterviewQuestionAnswerVO convertToInterviewQuestionAnswerVO(InterviewQuestionAnswer interview) {
        if (interview == null) {
            return null;
        }
        InterviewQuestionAnswerVO interviewVO = new InterviewQuestionAnswerVO();
        interviewVO.setId(interview.getId());
        interviewVO.setQuestion(questionMapper.convertToQuestionVO(interview.getQuestion()));
        interviewVO.setPossibleAnswer(possibleAnswerMapper.convertToPossibleAnswerVO(interview.getAnswer(), false));
        interviewVO.setInterview(interviewMapper.convertToInterviewVO(interview.getInterview(),false));
        interviewVO.setInterviewQuestionAnswerFreetext(interview.getInterviewQuestionAnswerFreetext());
        interviewVO.setDeleted(interview.getDeleted());
        return interviewVO;
    }

    @Override
    public List<InterviewQuestionAnswerVO> convertToInterviewQuestionAnswerVOList(List<InterviewQuestionAnswer> interviewEntity) {
        if (interviewEntity == null) {
            return null;
        }
        List<InterviewQuestionAnswerVO> list = new ArrayList<InterviewQuestionAnswerVO>();
        for (InterviewQuestionAnswer interview : interviewEntity) {
            list.add(convertToInterviewQuestionAnswerVO(interview));
        }
        return list;
    }

    @Override
    public InterviewQuestionAnswer convertToInterviewQuestionAnswer(InterviewQuestionAnswerVO interviewVO) {
        if (interviewVO == null) {
            return null;
        }
        InterviewQuestionAnswer interview = new InterviewQuestionAnswer();
        
        interview.setQuestion(questionMapper.convertToQuestion(interviewVO.getQuestion()));
        interview.setAnswer(possibleAnswerMapper.convertToPossibleAnswer(interviewVO.getPossibleAnswer()));
        interview.setDeleted(interviewVO.getDeleted());
        interview.setId(interviewVO.getId());
        interview.setInterviewQuestionAnswerFreetext(interviewVO.getInterviewQuestionAnswerFreetext());
        
        return interview;
    }

    @Override
    public List<InterviewQuestionAnswer> convertToInterviewQuestionAnswerList(List<InterviewQuestionAnswerVO> interviewVO) {
        if (interviewVO == null) {
            return null;
        }

        List<InterviewQuestionAnswer> list = new ArrayList<InterviewQuestionAnswer>();
        for (InterviewQuestionAnswerVO interviewVO_ : interviewVO) {
            list.add(convertToInterviewQuestionAnswer(interviewVO_));
        }

        return list;
    }
}
