package org.occideas.interviewquestion.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewQuestionVO;

public interface InterviewQuestionService extends BaseService<InterviewQuestionVO> {
	List<InterviewQuestionVO> findByInterviewId(Long id);

	
}
