package org.occideas.interviewquestion.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewQuestionVO;
import org.occideas.vo.QuestionVO;

public interface InterviewQuestionService extends BaseService<InterviewQuestionVO> {
	List<InterviewQuestionVO> findByInterviewId(Long id);
	
	InterviewQuestionVO findIntQuestion(long idInterview,long questionId);

	InterviewQuestionVO updateIntQ(InterviewQuestionVO o);

	InterviewQuestionVO updateInterviewLinkAndQueueQuestions(InterviewQuestionVO o);

}
