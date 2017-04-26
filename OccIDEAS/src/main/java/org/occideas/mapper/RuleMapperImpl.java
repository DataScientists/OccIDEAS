package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Rule;
import org.occideas.entity.RuleAdditionalField;
import org.occideas.rule.constant.RuleLevelEnum;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.RuleAdditionalFieldVO;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RuleMapperImpl implements RuleMapper {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private PossibleAnswerMapper paMapper;
	@Autowired
	private AgentMapper agentMapper;

	@Autowired
	private RuleAdditionalFieldMapper additionalFieldMapper;

	@Override
	public RuleVO convertToRuleVO(Rule ruleEntity) {
		if (ruleEntity == null) {
			return null;
		}

		RuleVO ruleVO = new RuleVO();

		ruleVO.setIdRule(ruleEntity.getIdRule());
		ruleVO.setLastUpdated(ruleEntity.getLastUpdated());
		ruleVO.setDeleted(ruleEntity.getDeleted());
		ruleVO.setAgentId(ruleEntity.getAgentId());
		ruleVO.setAgent(agentMapper.convertToAgentVO(ruleEntity.getAgent(), false));
		ruleVO.setLegacyRuleId(ruleEntity.getLegacyRuleId());
		ruleVO.setLevel(getDescriptionByValue(ruleEntity.getLevel()));
		ruleVO.setLevelValue(ruleEntity.getLevel());
		ruleVO.setType(ruleEntity.getType());
		List<PossibleAnswer> conditions = ruleEntity.getConditions();
		if (!CommonUtil.isListEmpty(conditions)) {
			ruleVO.setConditions(paMapper.convertToPossibleAnswerVOList(conditions, false));
		}
		List<RuleAdditionalField> additionalFields = ruleEntity.getRuleAdditionalfields();
		if (!CommonUtil.isListEmpty(additionalFields)) {
			ruleVO.setRuleAdditionalfields(additionalFieldMapper.convertToRuleAdditionalFieldVOList(additionalFields));
		}
		return ruleVO;
	}

	@Override
	public List<RuleVO> convertToRuleVOList(List<Rule> ruleEntity) {
		if (ruleEntity == null) {
			return null;
		}

		List<RuleVO> list = new ArrayList<RuleVO>();
		for (Rule rule : ruleEntity) {
			list.add(convertToRuleVO(rule));
		}

		return list;
	}

	@Override
	public Rule convertToRule(RuleVO ruleVO) {
		if (ruleVO == null) {
			return null;
		}
		Rule rule = new Rule();
		rule.setIdRule(ruleVO.getIdRule());
		rule.setDeleted(ruleVO.getDeleted());
		rule.setAgentId(ruleVO.getAgentId());
		rule.setAgent(agentMapper.convertToAgent(ruleVO.getAgent(), false));
		rule.setLegacyRuleId(ruleVO.getLegacyRuleId());
		int level = getValueByDescription(ruleVO.getLevel());
		if (level == -1) {
			log.warn("level returned -1:" + ruleVO.getLevel());
		}
		rule.setLevel(level);
		rule.setType(ruleVO.getType());
		List<PossibleAnswerVO> conditions = ruleVO.getConditions();
		if (!CommonUtil.isListEmpty(conditions)) {
			rule.setConditions(paMapper.convertToPossibleAnswerList(conditions));
		}
		List<RuleAdditionalFieldVO> ruleAdditionalfields = ruleVO.getRuleAdditionalfields();
		if (!CommonUtil.isListEmpty(ruleAdditionalfields)) {
			rule.setRuleAdditionalfields(additionalFieldMapper.convertToRuleAdditionalFieldList(ruleAdditionalfields));
		}
		return rule;
	}

	@Override
	public Rule convertToRuleExcPa(RuleVO ruleVO) {
		if (ruleVO == null) {
			return null;
		}
		Rule rule = new Rule();
		rule.setIdRule(ruleVO.getIdRule());
		rule.setDeleted(ruleVO.getDeleted());
		rule.setAgentId(ruleVO.getAgentId());
		rule.setAgent(agentMapper.convertToAgent(ruleVO.getAgent(), false));
		rule.setLegacyRuleId(ruleVO.getLegacyRuleId());
		int level = getValueByDescription(ruleVO.getLevel());
		if (level == -1) {
			log.warn("level returned -1:" + ruleVO.getLevel());
		}
		rule.setLevel(level);
		rule.setType(ruleVO.getType());
		List<PossibleAnswerVO> conditions = ruleVO.getConditions();
		if (!CommonUtil.isListEmpty(conditions)) {
			rule.setConditions(paMapper.convertToPossibleAnswerExModRuleList(conditions));
		}
		List<RuleAdditionalFieldVO> additionalFields = ruleVO.getRuleAdditionalfields();
		if (!CommonUtil.isListEmpty(additionalFields)) {
			rule.setRuleAdditionalfields(additionalFieldMapper.convertToRuleAdditionalFieldList(additionalFields));
		}

		return rule;
	}

	@Override
	public List<Rule> convertToRuleList(List<RuleVO> ruleVO) {
		if (ruleVO == null) {
			return null;
		}

		List<Rule> list = new ArrayList<Rule>();
		for (RuleVO ruleVO_ : ruleVO) {
			list.add(convertToRule(ruleVO_));
		}

		return list;
	}

	@Override
	public RuleVO convertToRuleVOExcPa(Rule rule) {
		if (rule == null) {
			return null;
		}

		RuleVO ruleVO = new RuleVO();

		ruleVO.setIdRule(rule.getIdRule());
		ruleVO.setLastUpdated(rule.getLastUpdated());
		ruleVO.setDeleted(rule.getDeleted());
		ruleVO.setAgentId(rule.getAgentId());
		ruleVO.setAgent(agentMapper.convertToAgentVO(rule.getAgent(), false));
		ruleVO.setLegacyRuleId(rule.getLegacyRuleId());
		ruleVO.setLevel(getDescriptionByValue(rule.getLevel()));
		ruleVO.setLevelValue(rule.getLevel());
		ruleVO.setType(rule.getType());
		List<PossibleAnswer> conditions = rule.getConditions();
		if (!CommonUtil.isListEmpty(conditions)) {
			ruleVO.setConditions(paMapper.convertToPossibleAnswerVOExModRuleList(conditions));
		}
		List<RuleAdditionalField> additionalFields = rule.getRuleAdditionalfields();
		if (!CommonUtil.isListEmpty(additionalFields)) {
			ruleVO.setRuleAdditionalfields(additionalFieldMapper.convertToRuleAdditionalFieldVOList(additionalFields));
		}
		return ruleVO;
	}

	@Override
	public List<RuleVO> convertToRuleVOExcPaList(List<Rule> ruleEntity) {
		if (ruleEntity == null) {
			return null;
		}

		List<RuleVO> list = new ArrayList<RuleVO>();
		for (Rule rule : ruleEntity) {
			list.add(convertToRuleVOExcPa(rule));
		}

		return list;
	}

	public static String getDescriptionByValue(int value) {
		for (RuleLevelEnum x : RuleLevelEnum.values()) {
			if (x.getValue() == value) {
				return x.getDescription();
			}
		}
		return "";
	}

	private int getValueByDescription(String description) {
		for (RuleLevelEnum x : RuleLevelEnum.values()) {
			if (x.getDescription().equals(description)) {
				return x.getValue();
			}
		}
		return -1;
	}

	@Override
	public List<Rule> convertToRuleExcPaList(List<RuleVO> ruleVO) {
		if (ruleVO == null) {
			return null;
		}

		List<Rule> list = new ArrayList<Rule>();
		for (RuleVO ruleVO_ : ruleVO) {
			list.add(convertToRuleExcPa(ruleVO_));
		}

		return list;
	}

}
