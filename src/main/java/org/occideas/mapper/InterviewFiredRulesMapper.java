package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewFiredRules;
import org.occideas.vo.InterviewFiredRulesVO;

public interface InterviewFiredRulesMapper {

	InterviewFiredRulesVO convertToInterviewFiredRulesVO(InterviewFiredRules entity);
	
	List<InterviewFiredRulesVO> convertToInterviewFiredRulesVOList(List<InterviewFiredRules> entity);
	
	InterviewFiredRules convertToInterviewFiredRules(InterviewFiredRulesVO vo);
	
	InterviewFiredRulesVO convertToInterviewFiredRulesVOWithRules(InterviewFiredRules entity);
	
	List<InterviewFiredRulesVO> convertToInterviewFiredRulesVOWithRulesList(List<InterviewFiredRules> entity);
	
	
	
}
