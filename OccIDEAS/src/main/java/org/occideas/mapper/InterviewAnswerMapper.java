package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewAnswer;
import org.occideas.vo.InterviewAnswerVO;

public interface InterviewAnswerMapper {

	InterviewAnswerVO convertToInterviewAnswerVO(InterviewAnswer answer);

	List<InterviewAnswerVO> convertToInterviewAnswerVOList(List<InterviewAnswer> answer);

	InterviewAnswer convertToInterviewAnswer(InterviewAnswerVO answerVO);
	
	List<InterviewAnswer> convertToInterviewAnswerList(List<InterviewAnswerVO> answerVOList);

	InterviewAnswerVO convertToInterviewAnswerWithRulesVO(InterviewAnswer answer);

	List<InterviewAnswerVO> convertToInterviewAnswerWithRulesVOList(List<InterviewAnswer> answerList);
	
}
