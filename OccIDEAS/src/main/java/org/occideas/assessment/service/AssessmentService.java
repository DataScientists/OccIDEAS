package org.occideas.assessment.service;

import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.vo.AssessmentAnswerSummaryFilterVO;
import org.occideas.vo.PageVO;

public interface AssessmentService {

	PageVO<AssessmentAnswerSummary> getAnswerSummaryByName(AssessmentAnswerSummaryFilterVO filter);
	
	
}
