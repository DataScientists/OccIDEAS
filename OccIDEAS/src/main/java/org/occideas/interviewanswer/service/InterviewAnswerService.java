package org.occideas.interviewanswer.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewQuestionVO;

public interface InterviewAnswerService extends BaseService<InterviewAnswerVO>{
	
	List<InterviewAnswerVO> updateIntA(List<InterviewAnswerVO> o);

	List<InterviewAnswerVO> saveIntervewAnswersAndQueueQuestions(List<InterviewAnswerVO> o);

	List<InterviewAnswerVO> findByInterviewId(Long id);

	List<InterviewQuestionVO> saveIntervewAnswersAndGetChildQuestion(List<InterviewAnswerVO> o);

	
}
