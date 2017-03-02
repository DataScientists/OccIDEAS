package org.occideas.interview.dao;

import java.math.BigInteger;
import java.util.List;

import org.occideas.entity.Interview;

public interface IInterviewDao {

	void saveNewTransaction(Interview interview);

	BigInteger getAnswerCount(Long interviewId, Long nodeId);

	BigInteger getAssessmentCount(String assessmentStatus);

	Long getCountForModules(String[] modules);

	List<Interview> getAllInterviewsWithoutAnswers();

	List<Interview> getInterviewIdList();

	List<Interview> getInterviews(Long[] interviewIds);

	List<Interview> getInterview(Long interviewId);

	List<Interview> findByReferenceNumber(String referenceNumber);

	List<Interview> getAssessments();

	List<Interview> getAllWithModules(String[] modules);

	List<Interview> getAll(String assessmentStatus);

	List<Interview> getAll();

	void saveOrUpdate(Interview interview);

	Interview merge(Interview interview);

	Interview get(Long id);

	void delete(Interview interview);

	void save(Interview interview);

}
