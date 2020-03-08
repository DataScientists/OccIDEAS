package org.occideas.interviewanswer.dao;

import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewQuestion;

import java.util.List;

public interface IInterviewAnswerDao {

  List<InterviewAnswer> saveOrUpdate(List<InterviewAnswer> ia);

  List<InterviewAnswer> saveAnswerAndQueueQuestions(List<InterviewAnswer> ia);

  List<InterviewAnswer> findByInterviewId(Long id);

  List<InterviewQuestion> saveIntervewAnswersAndGetChildQuestion(List<InterviewAnswer> convertToInterviewAnswerList);

  List<InterviewAnswer> findByInterviewId(Long interviewId, Long questionId);

  List<InterviewAnswer> saveWithClearSession(List<InterviewAnswer> convertToInterviewAnswerList);

  InterviewAnswer saveOrUpdate(InterviewAnswer answer) ;

  void deleteAll();

}
