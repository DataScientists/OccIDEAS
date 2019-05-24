package org.occideas.assessment.dao;

import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.vo.AssessmentAnswerSummaryFilterVO;

import java.util.List;

public interface IAssessmentDao {

  List<AssessmentAnswerSummary> getAnswerSummaryByName(AssessmentAnswerSummaryFilterVO filter);

  Long getAnswerSummaryByNameTotalCount(AssessmentAnswerSummaryFilterVO filter);

}
