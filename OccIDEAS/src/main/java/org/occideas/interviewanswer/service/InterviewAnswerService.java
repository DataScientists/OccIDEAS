package org.occideas.interviewanswer.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewAnswerVO;

public interface InterviewAnswerService extends BaseService<InterviewAnswerVO>{
	
	List<InterviewAnswerVO> updateIntA(List<InterviewAnswerVO> o);

	List<InterviewAnswerVO> saveIntervewAnswersAndQueueQuestions(List<InterviewAnswerVO> o);

	
}
