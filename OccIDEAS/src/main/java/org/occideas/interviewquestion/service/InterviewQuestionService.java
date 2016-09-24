package org.occideas.interviewquestion.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewQuestionVO;

public interface InterviewQuestionService extends BaseService<InterviewQuestionVO> {
	
	void updateModuleNameForInterviewId(long idInterview,String newName);
	
	List<InterviewQuestionVO> findByInterviewId(Long id);
	
	InterviewQuestionVO findIntQuestion(long idInterview,long questionId);

	InterviewQuestionVO updateIntQ(InterviewQuestionVO o);

	InterviewQuestionVO updateInterviewLinkAndQueueQuestions(InterviewQuestionVO o);

	List<InterviewQuestionVO> updateIntQs(List<InterviewQuestionVO> o);
	
	Long getMaxIntQuestionSequence(long idInterview);
	
	List<InterviewQuestionVO> getUniqueInterviewQuestions();
}
