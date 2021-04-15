package org.occideas.interviewquestion.dao;

import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewQuestion;
import org.occideas.vo.SystemPropertyVO;

import java.util.List;

public interface IInterviewQuestionDao {

  Long checkFragmentProcessed(long link, long id);

  Long getIntroModuleId(Long interviewId);

  List<InterviewQuestion> getQuestionsByNodeId(Long questionId);

  InterviewQuestion getByQuestionId(Long questionId, Long interviewId);

  Long getUniqueInterviewQuestionCount(String[] filterModule);

  Long getMaxIntQuestionSequence(long idInterview);

  InterviewQuestion findIntQuestion(long idInterview, long questionId);

  List<InterviewQuestion> findByInterviewId(Long interviewId);

  List<InterviewQuestion> getAllActive();

  List<InterviewQuestion> getAll();

  InterviewQuestion saveInterviewLinkAndQueueQuestions(InterviewQuestion iq);

  List<InterviewQuestion> saveOrUpdate(List<InterviewQuestion> iqs);

  InterviewQuestion saveOrUpdate(InterviewQuestion iq);

  InterviewQuestion merge(InterviewQuestion iq);

  InterviewQuestion get(Long id);

  void delete(InterviewQuestion iq);

  InterviewQuestion save(InterviewQuestion iq);

  void updateModuleNameForInterviewId(long id, String newName);

  List<InterviewQuestion> getUniqueInterviewQuestions(String[] filterModule);

  SystemPropertyVO preloadActiveIntro();

  void preloadAllModules();

  List<InterviewQuestion> getAllDeleted();

  InterviewQuestion saveOrUpdateSingleTransaction(InterviewQuestion iq);

  List<InterviewQuestion> getAllDeleted(Long idInterview);

  List<InterviewQuestion> getAllChildInterviewQuestions(Long idAnswer, Long idInterview);

  List<InterviewQuestion> getInterviewQuestionsByNodeIdAndIntId(Long questionId, Long idInterview);

  InterviewAnswer getInterviewAnswerByAnsIdAndIntId(Long answerId, Long idInterview);

  void deleteAll();

  Boolean checkIfStudyAgentPreLoaded();

  SystemPropertyVO preloadFilterStudyAgent(Long idNode);
}
