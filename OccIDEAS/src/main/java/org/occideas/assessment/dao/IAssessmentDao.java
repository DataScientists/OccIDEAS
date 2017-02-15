package org.occideas.assessment.dao;

import java.util.List;

import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.vo.AssessmentAnswerSummaryFilterVO;

public interface IAssessmentDao {

	List<AssessmentAnswerSummary> getAnswerSummaryByName(AssessmentAnswerSummaryFilterVO filter);

	Long getAnswerSummaryByNameTotalCount(AssessmentAnswerSummaryFilterVO filter);
	
}
