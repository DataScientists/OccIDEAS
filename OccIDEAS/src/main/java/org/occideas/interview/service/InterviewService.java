package org.occideas.interview.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.entity.Interview;
import org.occideas.vo.InterviewVO;

public interface InterviewService extends BaseService<InterviewVO> {
	void merge(InterviewVO o);

	List<InterviewVO> listAssessments();

	List<InterviewVO> listAllWithAnswers();

	List<Interview> listAllWithRules(String[] modules);
	
	List<InterviewVO> listAllWithRules();
	
	Long getAllWithRulesCount(String[] modules);

	List<InterviewVO> findByIdWithRules(Long id);
	
	List<InterviewVO> findByReferenceNumber(String referenceNumber);
	
	List<InterviewVO> getInterview(long idinterview);

	List<Long> getInterviewIdlist();

	List<InterviewVO> listAllInterviewsWithoutAnswers();

	List<Interview> getInterviewQuestionAnswer(long idinterview);
	
	List<InterviewVO> getInterviewQuestionAnswerVO(long idinterview);

	List<InterviewVO> getUnprocessedQuestions(Long id);

	InterviewVO findInterviewWithFiredRulesById(Long id);

	List<Interview> listAllWithAssessments(String[] modules);
}
