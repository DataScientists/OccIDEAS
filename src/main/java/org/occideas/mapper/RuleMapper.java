package org.occideas.mapper;

import org.mapstruct.Mapper;
import org.occideas.entity.Rule;
import org.occideas.vo.RuleVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RuleMapper {

  RuleVO convertToRuleVO(Rule rule);

  List<RuleVO> convertToRuleVOList(List<Rule> rule);

  RuleVO convertToRuleVOExcPa(Rule rule);

  List<RuleVO> convertToRuleVOExcPaList(List<Rule> rule);

  Rule convertToRule(RuleVO ruleVO);

  Rule convertToRuleExcPa(RuleVO ruleVO);

  List<Rule> convertToRuleList(List<RuleVO> ruleVO);

  List<Rule> convertToRuleExcPaList(List<RuleVO> ruleVO);

}
