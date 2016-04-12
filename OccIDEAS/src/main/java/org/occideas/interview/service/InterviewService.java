package org.occideas.interview.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewVO;

public interface InterviewService extends BaseService<InterviewVO> {
	void merge(InterviewVO o);

	List<InterviewVO> listAssessments();

	List<InterviewVO> listAllWithAnswers();

	List<InterviewVO> findByIdWithRules(Long id);
	
	List<InterviewVO> findByReferenceNumber(String referenceNumber);
}
