package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewAutoAssessment;
import org.occideas.vo.InterviewAutoAssessmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterviewAutoAssessmentMapperImpl implements InterviewAutoAssessmentMapper{

	@Autowired
	private RuleMapper ruleMapper;
	
	@Override
	public InterviewAutoAssessmentVO convertToInterviewAutoAssessmentVO(InterviewAutoAssessment entity) {
		if(entity == null){
			return null;
		}
		InterviewAutoAssessmentVO vo = new InterviewAutoAssessmentVO();
		vo.setIdInterview(entity.getIdInterview());
		vo.setId(entity.getId());
		vo.setRule(ruleMapper.convertToRuleVO(entity.getRule()));
		return vo;
	}

	@Override
	public InterviewAutoAssessment convertToInterviewAutoAssessment(InterviewAutoAssessmentVO vo) {
		if(vo == null){
			return null;
		}
		InterviewAutoAssessment entity = new InterviewAutoAssessment();
		entity.setIdInterview(vo.getIdInterview());
		entity.setId(vo.getId());
		entity.setRule(ruleMapper.convertToRule(vo.getRule()));
		return entity;
	}

	@Override
	public List<InterviewAutoAssessment> convertToInterviewAutoAssessmentList(List<InterviewAutoAssessmentVO> entity) {
		if(entity == null){
			return null;
		}
		List<InterviewAutoAssessment> list = new ArrayList<>();
		for(InterviewAutoAssessmentVO rules:entity){
			list.add(convertToInterviewAutoAssessment(rules));
		}
		return list;
	}
	@Override
	public List<InterviewAutoAssessmentVO> convertToInterviewAutoAssessmentVOList(List<InterviewAutoAssessment> entity) {
		if(entity == null){
			return null;
		}
		List<InterviewAutoAssessmentVO> list = new ArrayList<>();
		for(InterviewAutoAssessment rules:entity){
			list.add(convertToInterviewAutoAssessmentVO(rules));
		}
		return list;
	}

	@Override
	public InterviewAutoAssessmentVO convertToInterviewAutoAssessmentVOWithRules(InterviewAutoAssessment entity) {
		if(entity == null){
			return null;
		}
		InterviewAutoAssessmentVO vo = new InterviewAutoAssessmentVO();
		vo.setIdInterview(entity.getIdInterview());
		vo.setId(entity.getId());
		vo.setRule(ruleMapper.convertToRuleVO(entity.getRule()));
		
		return vo;
	}

	@Override
	public List<InterviewAutoAssessmentVO> convertToInterviewAutoAssessmentVOWithRulesList(List<InterviewAutoAssessment> entity) {
		if(entity == null){
			return null;
		}
		List<InterviewAutoAssessmentVO> list = new ArrayList<>();
		for(InterviewAutoAssessment rules:entity){
			list.add(convertToInterviewAutoAssessmentVOWithRules(rules));
		}
		return list;
	}

}
