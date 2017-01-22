package org.occideas.assessment.dao;

import java.util.List;

import org.occideas.entity.AssessmentAnswerSummary;

public interface IAssessmentDao {

	List<AssessmentAnswerSummary> getAnswerSummaryByName(Long answerId, String name);
	
}
