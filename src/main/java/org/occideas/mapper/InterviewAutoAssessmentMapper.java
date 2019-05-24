package org.occideas.mapper;

import org.occideas.entity.InterviewAutoAssessment;
import org.occideas.vo.InterviewAutoAssessmentVO;

import java.util.List;

public interface InterviewAutoAssessmentMapper {

  InterviewAutoAssessmentVO convertToInterviewAutoAssessmentVO(InterviewAutoAssessment entity);

  List<InterviewAutoAssessmentVO> convertToInterviewAutoAssessmentVOList(List<InterviewAutoAssessment> entity);

  List<InterviewAutoAssessment> convertToInterviewAutoAssessmentList(List<InterviewAutoAssessmentVO> entity);

  InterviewAutoAssessment convertToInterviewAutoAssessment(InterviewAutoAssessmentVO vo);

  InterviewAutoAssessmentVO convertToInterviewAutoAssessmentVOWithRules(InterviewAutoAssessment entity);

  List<InterviewAutoAssessmentVO> convertToInterviewAutoAssessmentVOWithRulesList(List<InterviewAutoAssessment> entity);


}
