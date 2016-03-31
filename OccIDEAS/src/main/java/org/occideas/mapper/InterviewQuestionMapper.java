package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewQuestion;
import org.occideas.vo.InterviewQuestionVO;

public interface InterviewQuestionMapper {

	InterviewQuestionVO convertToInterviewQuestionVO(InterviewQuestion question);

	List<InterviewQuestionVO> convertToInterviewQuestionVOList(List<InterviewQuestion> question);

	InterviewQuestion convertToInterviewQuestion(InterviewQuestionVO questionVO);
	
	List<InterviewQuestion> convertToInterviewQuestionList(List<InterviewQuestionVO> questionVO);
	
}
