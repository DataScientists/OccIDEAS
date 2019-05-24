package org.occideas.mapper;

import org.occideas.entity.InterviewQuestion;
import org.occideas.vo.InterviewQuestionVO;

import java.util.List;

public interface InterviewQuestionMapper {

  InterviewQuestionVO convertToInterviewQuestionVO(InterviewQuestion question);

  InterviewQuestionVO convertToInterviewQuestionVO(InterviewQuestion question, boolean isIncludeAnswer);

  List<InterviewQuestionVO> convertToInterviewQuestionVOList(List<InterviewQuestion> question);

  List<InterviewQuestionVO> convertToInterviewQuestionVOList(List<InterviewQuestion> question, boolean isIncludeAnswer);

  InterviewQuestionVO convertToInterviewQuestionUnprocessedVO(InterviewQuestion question);

  List<InterviewQuestionVO> convertToInterviewQuestionUnprocessedVOList(List<InterviewQuestion> question);

  InterviewQuestion convertToInterviewQuestion(InterviewQuestionVO questionVO);

  InterviewQuestion convertToInterviewQuestionWithAnswers(InterviewQuestionVO questionVO);

  List<InterviewQuestion> convertToInterviewQuestionList(List<InterviewQuestionVO> questionVO);

  List<InterviewQuestion> convertToInterviewQuestionListWithAnswers(List<InterviewQuestionVO> questionVO);

  InterviewQuestionVO convertToInterviewQuestionWithRulesVO(InterviewQuestion question);

  List<InterviewQuestionVO> convertToInterviewQuestionWithRulesVOList(List<InterviewQuestion> question);

  InterviewQuestionVO convertToInterviewQuestionWithoutAnswers(InterviewQuestion question);

  List<InterviewQuestionVO> convertToInterviewQuestionWithoutAnswersList(List<InterviewQuestion> question);

  List<InterviewQuestionVO> convertToInterviewQuestionVOListExcAnswers(List<InterviewQuestion> question);

  InterviewQuestionVO convertToInterviewQuestionVOExcAnswers(InterviewQuestion question);

  InterviewQuestionVO convertToInterviewQuestionNoAnswersVO(InterviewQuestion question);

  List<InterviewQuestionVO> convertToInterviewQuestionNoAnswersVOList(List<InterviewQuestion> question);

  List<InterviewQuestionVO> convertToInterviewQuestionVOList(List<InterviewQuestion> questionHistory, Long qId);

  List<InterviewQuestionVO> convertToInterviewQuestionVOWithAnswerList(List<InterviewQuestion> question);

}
