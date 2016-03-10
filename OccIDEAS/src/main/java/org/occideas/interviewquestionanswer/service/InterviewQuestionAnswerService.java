package org.occideas.interviewquestionanswer.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewQuestionAnswerVO;

public interface InterviewQuestionAnswerService extends BaseService<InterviewQuestionAnswerVO> {
	List<InterviewQuestionAnswerVO> findByInterviewId(Long id);

	
}
