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
	private InterviewQuestionMapper questionMapper;
    
    @Override
    public InterviewQuestionAnswerVO convertToInterviewQuestionAnswerVO(InterviewQuestionAnswer interview) {
        if (interview == null) {
            return null;
        }
        InterviewQuestionAnswerVO interviewVO = new InterviewQuestionAnswerVO();
        interviewVO.setId(interview.getId());
        interviewVO.setIdInterview(interview.getIdInterview());
        interviewVO.setQuestion(questionMapper.convertToInterviewQuestionVO(interview.getQuestion()));
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
        	if(interview.getDeleted() == 0){
            list.add(convertToInterviewQuestionAnswerVO(interview));
        	}
        }
        return list;
    }

    @Override
    public InterviewQuestionAnswer convertToInterviewQuestionAnswer(InterviewQuestionAnswerVO interviewVO) {
        if (interviewVO == null) {
            return null;
        }
        InterviewQuestionAnswer interview = new InterviewQuestionAnswer();
        
        interview.setQuestion(questionMapper.convertToInterviewQuestion(interviewVO.getQuestion()));
        interview.setDeleted(interviewVO.getDeleted());
        interview.setId(interviewVO.getId());
        interview.setIdInterview(interviewVO.getIdInterview());
        
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
