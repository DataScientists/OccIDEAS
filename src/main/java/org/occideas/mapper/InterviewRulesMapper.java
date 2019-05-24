package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewRuleReport;
import org.occideas.vo.InterviewRuleReportVO;

public interface InterviewRulesMapper {

	InterviewRuleReportVO convertToInterviewRulesVO(InterviewRuleReport entity);
	
	List<InterviewRuleReportVO> convertToInterviewRuleVOList(List<InterviewRuleReport> question);

}
