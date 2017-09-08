package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewRuleReport;
import org.occideas.vo.InterviewRuleReportVO;
import org.springframework.stereotype.Component;

@Component
public class InterviewRulesMapperImpl implements InterviewRulesMapper{

	@Override
	public InterviewRuleReportVO convertToInterviewRulesVO(InterviewRuleReport entity) {
		if (entity == null) {
			return null;
		}
		InterviewRuleReportVO vo = new InterviewRuleReportVO();
		vo.setAgentName(entity.getAgentName());
		vo.setIdInterview(entity.getIdInterview());
		vo.setIdRule(entity.getIdRule());
		vo.setLevel(entity.getLevel());
		vo.setModName(entity.getModName());
		vo.setReferenceNumber(entity.getReferenceNumber());
		return vo;
	}

	@Override
	public List<InterviewRuleReportVO> convertToInterviewRuleVOList(List<InterviewRuleReport> list) {
		if (list == null) {
			return null;
		}
		List<InterviewRuleReportVO> l = new ArrayList<InterviewRuleReportVO>();
		for (InterviewRuleReport e : list) {
			l.add(convertToInterviewRulesVO(e));
		}
		return l;
	}

}
