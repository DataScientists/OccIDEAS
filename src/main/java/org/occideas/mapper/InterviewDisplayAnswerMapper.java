package org.occideas.mapper;

import org.occideas.entity.InterviewDisplayAnswer;
import org.occideas.vo.InterviewDisplayAnswerVO;

import java.util.List;

public interface InterviewDisplayAnswerMapper {

  InterviewDisplayAnswerVO convertToInterviewDisplayAnswerVO(InterviewDisplayAnswer entity);

  InterviewDisplayAnswer convertToInterviewDisplayAnswer(InterviewDisplayAnswerVO vo);

  List<InterviewDisplayAnswerVO> convertToInterviewDisplayAnswerVOList(List<InterviewDisplayAnswer> entity);

  List<InterviewDisplayAnswer> convertToInterviewDisplayAnswerList(List<InterviewDisplayAnswerVO> vo);

}
