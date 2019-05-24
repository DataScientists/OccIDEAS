package org.occideas.mapper;

import org.occideas.entity.InterviewAnswer;
import org.occideas.vo.InterviewAnswerVO;

import java.util.List;

public interface InterviewAnswerMapper {

  InterviewAnswerVO convertToInterviewAnswerVO(InterviewAnswer answer);

  List<InterviewAnswerVO> convertToInterviewAnswerVOList(List<InterviewAnswer> answer);

  InterviewAnswer convertToInterviewAnswer(InterviewAnswerVO answerVO);

  List<InterviewAnswer> convertToInterviewAnswerList(List<InterviewAnswerVO> answerVOList);

  InterviewAnswerVO convertToInterviewAnswerWithRulesVO(InterviewAnswer answer);

  List<InterviewAnswerVO> convertToInterviewAnswerWithRulesVOList(List<InterviewAnswer> answerList);

}
