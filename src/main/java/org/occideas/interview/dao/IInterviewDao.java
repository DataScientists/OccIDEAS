package org.occideas.interview.dao;

import org.occideas.entity.Interview;
import org.occideas.entity.Question;

import java.math.BigInteger;
import java.util.List;

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

    void saveOrUpdate(List<Interview> interviews);

    Interview merge(Interview interview);

    Interview get(Long id);

    void delete(Interview interview);

    void save(Interview interview);

    List<Interview> getAssessmentsForNotes(String[] modules);

    List<String> getNoteTypes();

    List<Long> getLinksByAnswerId(long answerId);

    List<Question> getLinksByModule(Long id);

    List<Interview> getAllInterviewsWithAnswersAndAssessments();

    Interview getInterviewWithAnswersAndAssessments(long id);

    List<Interview> getAllInterviewsNotAssessed();

    List<Interview> getAllInterviewsAssessed();

    void deleteAutoAssessments();

    void deleteAutoAssessment(long id);

    void deleteAll();

}
