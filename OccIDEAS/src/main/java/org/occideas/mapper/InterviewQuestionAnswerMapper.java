package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.InterviewQuestionAnswer;
import org.occideas.vo.InterviewQuestionAnswerVO;

@Mapper(componentModel = "spring")
public interface InterviewQuestionAnswerMapper {

	InterviewQuestionAnswerVO convertToInterviewQuestionAnswerVO(InterviewQuestionAnswer Interview);

	List<InterviewQuestionAnswerVO> convertToInterviewQuestionAnswerVOList(List<InterviewQuestionAnswer> InterviewList);

	InterviewQuestionAnswer convertToInterviewQuestionAnswer(InterviewQuestionAnswerVO InterviewVO);
	
	List<InterviewQuestionAnswer> convertToInterviewQuestionAnswerList(List<InterviewQuestionAnswerVO> InterviewVO);
	
}
