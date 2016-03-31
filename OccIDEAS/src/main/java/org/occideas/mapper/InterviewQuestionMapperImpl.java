package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewQuestion;
import org.occideas.vo.InterviewQuestionVO;

public class InterviewQuestionMapperImpl implements InterviewQuestionMapper{

	@Override
	public InterviewQuestionVO convertToInterviewQuestionVO(InterviewQuestion question) {
		if (question == null) {
	            return null;
	    }
		InterviewQuestionVO vo = new InterviewQuestionVO();
		vo.setIdInterview(question.getIdInterview());
		vo.setName(question.getName());
		vo.setNodeClass(question.getNodeClass());
		vo.setNumber(question.getNumber());
		vo.setQuestionId(question.getQuestionId());
		vo.setType(question.getType());
		vo.setDescription(question.getDescription());
		vo.setDeleted(question.getDeleted());
		return vo;
	}

	@Override
	public List<InterviewQuestionVO> convertToInterviewQuestionVOList(List<InterviewQuestion> question) {
		if (question == null) {
            return null;
        }
        List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
        for (InterviewQuestion iq : question) {
        	if(iq.getDeleted() == 0){
        		list.add(convertToInterviewQuestionVO(iq));
        	}
        }
        return list;
	}

	@Override
	public InterviewQuestion convertToInterviewQuestion(InterviewQuestionVO questionVO) {
		if(questionVO == null){
			return null;
		}
		InterviewQuestion question = new InterviewQuestion();
		question.setIdInterview(questionVO.getIdInterview());
		question.setName(questionVO.getName());
		question.setDeleted(questionVO.getDeleted());
		question.setDescription(questionVO.getDescription());
		question.setNodeClass(questionVO.getNodeClass());
		question.setNumber(questionVO.getNumber());
		question.setQuestionId(questionVO.getQuestionId());
		question.setType(questionVO.getType());
		return question;
	}

	@Override
	public List<InterviewQuestion> convertToInterviewQuestionList(List<InterviewQuestionVO> iq) {
		if (iq == null) {
            return null;
        }
        List<InterviewQuestion> list = new ArrayList<InterviewQuestion>();
        for (InterviewQuestionVO iqu : iq) {
        	if(iqu.getDeleted() == 0){
        		list.add(convertToInterviewQuestion(iqu));
        	}
        }
        return list;
	}

}
