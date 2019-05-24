package org.occideas.interviewautoassessment.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.GenericNodeVO;
import org.occideas.vo.InterviewAutoAssessmentVO;

import java.util.List;

public interface InterviewAutoAssessmentService extends BaseService<InterviewAutoAssessmentVO> {

  List<InterviewAutoAssessmentVO> findByInterviewId(Long interviewId);

  GenericNodeVO findNodeById(long idNode);

  List<InterviewAutoAssessmentVO> updateAutoAssessments(List<InterviewAutoAssessmentVO> json);

}
