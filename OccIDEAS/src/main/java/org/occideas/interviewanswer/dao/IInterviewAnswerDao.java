package org.occideas.interviewanswer.dao;

import java.util.List;

import org.occideas.entity.InterviewAnswer;

public interface IInterviewAnswerDao {

	List<InterviewAnswer> saveOrUpdate(List<InterviewAnswer> ia);

	List<InterviewAnswer> saveAnswerAndQueueQuestions(List<InterviewAnswer> ia);

}
