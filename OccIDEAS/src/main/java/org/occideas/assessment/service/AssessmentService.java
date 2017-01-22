package org.occideas.assessment.service;

import java.util.List;

import org.occideas.entity.AssessmentAnswerSummary;

public interface AssessmentService {

	List<AssessmentAnswerSummary> getAnswerSummaryByName(Long answerId, String name);
	
	
}
