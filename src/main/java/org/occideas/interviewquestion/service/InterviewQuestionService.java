package org.occideas.interviewquestion.service;

import org.occideas.base.service.BaseService;
import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewQuestion;
import org.occideas.vo.InterviewQuestionVO;

import java.util.List;

public interface InterviewQuestionService extends BaseService<InterviewQuestionVO> {

  void updateModuleNameForInterviewId(long idInterview, String newName);

  List<InterviewQuestionVO> findByInterviewId(Long id);

  InterviewQuestionVO findIntQuestion(long idInterview, long questionId);

  InterviewQuestionVO updateIntQ(InterviewQuestionVO o);

  InterviewQuestionVO updateInterviewLinkAndQueueQuestions(InterviewQuestionVO o);

  List<InterviewQuestionVO> updateIntQs(List<InterviewQuestionVO> o);

  Long getMaxIntQuestionSequence(long idInterview);

  List<InterviewQuestion> getUniqueInterviewQuestions(String[] filterModule);

  Long getUniqueInterviewQuestionCount(String[] filterModule);

  List<InterviewQuestionVO> findById(Long questionId, Long interviewId);

  List<InterviewQuestionVO> findQuestionsByNodeId(Long questionId);

  List<InterviewQuestionVO> getInterviewQuestionsByNodeIdAndIntId(Long questionId, Long idInterview);

  InterviewAnswer getInterviewAnswerByAnsIdAndIntId(Long answerId, Long idInterview);

}
