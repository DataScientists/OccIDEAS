package org.occideas.interviewanswer.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewQuestionVO;

import java.util.List;

public interface InterviewAnswerService extends BaseService<InterviewAnswerVO> {

  List<InterviewAnswerVO> updateIntA(List<InterviewAnswerVO> o);

  List<InterviewAnswerVO> saveIntervewAnswersAndQueueQuestions(List<InterviewAnswerVO> o);

  List<InterviewAnswerVO> findByInterviewId(Long id);

  List<InterviewQuestionVO> saveIntervewAnswersAndGetChildQuestion(List<InterviewAnswerVO> o);

  InterviewAnswerVO saveOrUpdate(InterviewAnswerVO answerVO);

}
