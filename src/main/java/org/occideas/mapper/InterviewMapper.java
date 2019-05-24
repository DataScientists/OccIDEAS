package org.occideas.mapper;

import org.mapstruct.Mapper;
import org.occideas.entity.Interview;
import org.occideas.vo.InterviewVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InterviewMapper {


  List<InterviewVO> convertToInterviewVOList(List<Interview> InterviewList);

  Interview convertToInterview(InterviewVO InterviewVO);

  List<Interview> convertToInterviewList(List<InterviewVO> InterviewVO);

  List<InterviewVO> convertToInterviewVOnoQsList(List<Interview> InterviewList);

  InterviewVO convertToInterviewVOnoQs(Interview interview);

  InterviewVO convertToInterviewVO(Interview interview);

  InterviewVO convertToInterviewUnprocessQuestion(Interview interview);

  InterviewVO convertToInterviewWithRulesVO(Interview interview, boolean isIncludeAnswer);

  List<InterviewVO> convertToInterviewWithRulesVOList(List<Interview> interviewEntity);

  InterviewVO convertToInterviewWithModulesVO(Interview interview);

  List<InterviewVO> convertToInterviewWithModulesVOList(List<Interview> interviewEntity);

  InterviewVO convertToInterviewWithQuestionAnswer(Interview interview);

  List<InterviewVO> convertToInterviewWithQuestionAnswerList(List<Interview> interviewEntity);

  List<Long> convertToInterviewIdList(List<Interview> interviewIdList);

  InterviewVO convertToInterviewWithoutAnswers(Interview interview);

  List<InterviewVO> convertToInterviewWithoutAnswersList(List<Interview> interviewEntity);

  InterviewVO convertToInterviewVOWithFiredRules(Interview interview);

  InterviewVO convertToInterviewWithAssessmentsVO(Interview interview);

  List<InterviewVO> convertToInterviewWithAssessmentsVOList(List<Interview> interviewEntity);

  InterviewVO convertToInterviewWithRulesNoAnswersVO(Interview interview);

  List<InterviewVO> convertToInterviewWithRulesNoAnswersVOList(List<Interview> interviewEntity);


}
