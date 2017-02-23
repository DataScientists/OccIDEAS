package org.occideas.interviewmanualassessment.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.GenericNodeVO;
import org.occideas.vo.InterviewManualAssessmentVO;

public interface InterviewManualAssessmentService extends BaseService<InterviewManualAssessmentVO>{

	List<InterviewManualAssessmentVO> findByInterviewId(Long interviewId);
	
	GenericNodeVO findNodeById(long idNode);

	List<InterviewManualAssessmentVO> updateManualAssessments(List<InterviewManualAssessmentVO> json);
	
}
