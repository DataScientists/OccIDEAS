package org.occideas.interview.service;

import org.occideas.base.service.BaseService;
import org.occideas.entity.Interview;
import org.occideas.entity.Rule;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.RandomInterviewReport;
import org.occideas.vo.SystemPropertyVO;

import java.math.BigInteger;
import java.util.List;

public interface InterviewService extends BaseService<InterviewVO> {
  void update(Interview interview);

  void merge(Interview interview);

  void merge(InterviewVO o);

  List<InterviewVO> listAssessments();

  List<InterviewVO> listAllWithAnswers();

  List<Interview> listAllWithRules(String[] modules);

  List<InterviewVO> listAllWithRulesVO(String type);

  Long getAllWithRulesCount(String[] modules);

  List<InterviewVO> findByIdWithRules(Long id);

  List<InterviewVO> findByReferenceNumber(String referenceNumber);

  List<InterviewVO> getInterview(long idinterview);

  List<Long> getInterviewIdlist();

  List<InterviewVO> listAllInterviewsWithoutAnswers();

  List<Interview> getInterviewQuestionAnswer(long idinterview);

  List<Interview> getInterviewsQuestionAnswer(Long[] ids);

  List<InterviewVO> getInterviewQuestionAnswerVO(long idinterview);

  List<InterviewVO> getUnprocessedQuestions(Long id);

  InterviewVO findInterviewWithFiredRulesById(Long id);

  List<Interview> listAllWithAssessments(String[] modules);

  List<InterviewVO> findByIdWithRules(Long id, boolean isIncludeAnswer);

  InterviewVO getQuestionHistory(Long id);

  BigInteger listAllWithRuleCount(String assessmentStatus);

  boolean isQuestionAnswered(Long interviewId, Long nodeId);

  Long getIntroModuleId(Long interviewId);

  Long checkFragmentProcessed(long idFragment, long primaryKey);

  List<RandomInterviewReport> createRandomInterviews(int count, Boolean isRandomAnswers, String[] filterModule);

  List<Interview> listAssessmentsForNotes(String[] filterModule);

  List<String> getNoteTypes();

  List<QuestionVO> getLinksByModule(Long id);

  SystemPropertyVO preloadActiveIntro();

  void preloadAllModules();

  List<InterviewVO> listAllInterviewsNotAssessed();

  List<InterviewVO> listAllInterviewsAssessed();

  void cleanDeletedAnswers(Long id);

  SystemPropertyVO preloadFilterStudyAgent(Long idNode);

  void autoAssessedRules();

  void evaluateAssessmentStatus(Interview interview);

  void deleteOldAutoAssessments(Interview interview);

  List<Rule> determineFiredRules(Interview interview);

  InterviewVO updateFiredRule(long interviewId);

  Interview autoAssessedRule(List<Long> listAgentIds, Interview interview);

  void deleteOldAutoAssessments();
}
