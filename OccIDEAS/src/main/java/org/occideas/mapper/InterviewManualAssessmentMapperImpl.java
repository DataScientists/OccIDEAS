package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewManualAssessment;
import org.occideas.vo.InterviewManualAssessmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterviewManualAssessmentMapperImpl implements InterviewManualAssessmentMapper{

	@Autowired
	private InterviewMapper interviewMapper;
	@Autowired
	private RuleMapper ruleMapper;
	
	@Override
	public InterviewManualAssessmentVO convertToInterviewManualAssessmentVO(InterviewManualAssessment entity) {
		if(entity == null){
			return null;
		}
		InterviewManualAssessmentVO vo = new InterviewManualAssessmentVO();
		vo.setIdInterview(entity.getIdInterview());
		vo.setId(entity.getId());
		vo.setRule(ruleMapper.convertToRuleVO(entity.getRule()));
		return vo;
	}

	@Override
	public InterviewManualAssessment convertToInterviewManualAssessment(InterviewManualAssessmentVO vo) {
		if(vo == null){
			return null;
		}
		InterviewManualAssessment entity = new InterviewManualAssessment();
		entity.setIdInterview(vo.getIdInterview());
		entity.setId(vo.getId());
		entity.setRule(ruleMapper.convertToRule(vo.getRule()));
		return entity;
	}

	@Override
	public List<InterviewManualAssessment> convertToInterviewManualAssessmentList(List<InterviewManualAssessmentVO> entity) {
		if(entity == null){
			return null;
		}
		List<InterviewManualAssessment> list = new ArrayList<>();
		for(InterviewManualAssessmentVO rules:entity){
			list.add(convertToInterviewManualAssessment(rules));
		}
		return list;
	}
	@Override
	public List<InterviewManualAssessmentVO> convertToInterviewManualAssessmentVOList(List<InterviewManualAssessment> entity) {
		if(entity == null){
			return null;
		}
		List<InterviewManualAssessmentVO> list = new ArrayList<>();
		for(InterviewManualAssessment rules:entity){
			list.add(convertToInterviewManualAssessmentVO(rules));
		}
		return list;
	}

	@Override
	public InterviewManualAssessmentVO convertToInterviewManualAssessmentVOWithRules(InterviewManualAssessment entity) {
		if(entity == null){
			return null;
		}
		InterviewManualAssessmentVO vo = new InterviewManualAssessmentVO();
		vo.setIdInterview(entity.getIdInterview());
		vo.setId(entity.getId());
		vo.setRule(ruleMapper.convertToRuleVO(entity.getRule()));
		
		return vo;
	}

	@Override
	public List<InterviewManualAssessmentVO> convertToInterviewManualAssessmentVOWithRulesList(List<InterviewManualAssessment> entity) {
		if(entity == null){
			return null;
		}
		List<InterviewManualAssessmentVO> list = new ArrayList<>();
		for(InterviewManualAssessment rules:entity){
			list.add(convertToInterviewManualAssessmentVOWithRules(rules));
		}
		return list;
	}

}
