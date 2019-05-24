package org.occideas.mapper;

import org.occideas.entity.InterviewRuleReport;
import org.occideas.vo.InterviewRuleReportVO;

import java.util.List;

public interface InterviewRulesMapper {

  InterviewRuleReportVO convertToInterviewRulesVO(InterviewRuleReport entity);

  List<InterviewRuleReportVO> convertToInterviewRuleVOList(List<InterviewRuleReport> question);

}
