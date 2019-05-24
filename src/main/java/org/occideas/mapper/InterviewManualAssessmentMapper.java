package org.occideas.mapper;

import org.occideas.entity.InterviewManualAssessment;
import org.occideas.vo.InterviewManualAssessmentVO;

import java.util.List;

public interface InterviewManualAssessmentMapper {

  InterviewManualAssessmentVO convertToInterviewManualAssessmentVO(InterviewManualAssessment entity);

  List<InterviewManualAssessmentVO> convertToInterviewManualAssessmentVOList(List<InterviewManualAssessment> entity);

  List<InterviewManualAssessment> convertToInterviewManualAssessmentList(List<InterviewManualAssessmentVO> entity);

  InterviewManualAssessment convertToInterviewManualAssessment(InterviewManualAssessmentVO vo);

  InterviewManualAssessmentVO convertToInterviewManualAssessmentVOWithRules(InterviewManualAssessment entity);

  List<InterviewManualAssessmentVO> convertToInterviewManualAssessmentVOWithRulesList(List<InterviewManualAssessment> entity);


}
