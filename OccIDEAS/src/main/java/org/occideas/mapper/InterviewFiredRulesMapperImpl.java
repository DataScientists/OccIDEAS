package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewFiredRules;
import org.occideas.vo.InterviewFiredRulesVO;
import org.springframework.stereotype.Component;

@Component
public class InterviewFiredRulesMapperImpl implements InterviewFiredRulesMapper{

	@Override
	public InterviewFiredRulesVO convertToInterviewFiredRulesVO(InterviewFiredRules entity) {
		if(entity == null){
			return null;
		}
		InterviewFiredRulesVO vo = new InterviewFiredRulesVO();
		vo.setIdinterview(entity.getIdinterview());
		vo.setId(entity.getId());
		vo.setIdRule(entity.getIdRule());
		return vo;
	}

	@Override
	public InterviewFiredRules convertToInterviewFiredRules(InterviewFiredRulesVO vo) {
		if(vo == null){
			return null;
		}
		InterviewFiredRules entity = new InterviewFiredRules();
		entity.setIdinterview(vo.getIdinterview());
		entity.setId(vo.getId());
		entity.setIdRule(vo.getIdRule());
		return entity;
	}

	@Override
	public List<InterviewFiredRulesVO> convertToInterviewFiredRulesVOList(List<InterviewFiredRules> entity) {
		if(entity == null){
			return null;
		}
		List<InterviewFiredRulesVO> list = new ArrayList<>();
		for(InterviewFiredRules rules:entity){
			list.add(convertToInterviewFiredRulesVO(rules));
		}
		return list;
	}

}
