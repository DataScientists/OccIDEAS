package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewQuestion;
import org.occideas.vo.InterviewQuestionVO;

public interface InterviewQuestionMapper {

	InterviewQuestionVO convertToInterviewQuestionVO(InterviewQuestion question);

	List<InterviewQuestionVO> convertToInterviewQuestionVOList(List<InterviewQuestion> question);

	InterviewQuestion convertToInterviewQuestion(InterviewQuestionVO questionVO);
	
	List<InterviewQuestion> convertToInterviewQuestionList(List<InterviewQuestionVO> questionVO);

	InterviewQuestionVO convertToInterviewQuestionWithRulesVO(InterviewQuestion question);

	List<InterviewQuestionVO> convertToInterviewQuestionWithRulesVOList(List<InterviewQuestion> question);
	
	InterviewQuestionVO convertToInterviewQuestionWithoutAnswers(InterviewQuestion question);

	List<InterviewQuestionVO> convertToInterviewQuestionWithoutAnswersList(List<InterviewQuestion> question);
	
}
