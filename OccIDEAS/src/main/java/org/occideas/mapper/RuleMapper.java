package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.Rule;
import org.occideas.vo.RuleVO;

@Mapper(componentModel = "spring")
public interface RuleMapper {

	RuleVO convertToRuleVO(Rule rule);
	
	List<RuleVO> convertToRuleVOList(List<Rule> rule);
	
	RuleVO convertToRuleVOExcPa(Rule rule);
	
	List<RuleVO> convertToRuleVOExcPaList(List<Rule> rule);

	Rule convertToRule(RuleVO ruleVO);
	
	List<Rule> convertToRuleList(List<RuleVO> ruleVO);
	
}
