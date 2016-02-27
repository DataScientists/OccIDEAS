package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Rule;
import org.occideas.entity.RuleAdditionalField;
import org.occideas.rule.constant.RuleLevelEnum;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RuleMapperImpl implements RuleMapper {
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private PossibleAnswerMapper paMapper;
	
	@Autowired
	private RuleAdditionalFieldMapper additionalFieldMapper;
	
    @Override
    public RuleVO convertToRuleVO(Rule ruleEntity) {
        if ( ruleEntity == null ) {
            return null;
        }

        RuleVO ruleVO = new RuleVO();

        ruleVO.setIdRule( ruleEntity.getIdRule() );
        ruleVO.setLastUpdated( ruleEntity.getLastUpdated() ); 
        ruleVO.setAgentId(ruleEntity.getAgentId());
        ruleVO.setLegacyRuleId(ruleEntity.getLegacyRuleId());
        ruleVO.setLevel(getDescriptionByValue(ruleEntity.getLevel()));
        ruleVO.setType(ruleEntity.getType());
        List<PossibleAnswer> conditions = ruleEntity.getConditions();
		if (!CommonUtil.isListEmpty(conditions)) {
			ruleVO.setConditions(paMapper.convertToPossibleAnswerVOList(conditions,false));
		}
		List<RuleAdditionalField> additionalFields = ruleEntity.getRuleAdditionalfields();
		if (!CommonUtil.isListEmpty(additionalFields)) {
			ruleVO.setRuleAdditionalfields(additionalFieldMapper.convertToRuleAdditionalFieldVOList(additionalFields));
		}
        return ruleVO;
    }

    @Override
    public List<RuleVO> convertToRuleVOList(List<Rule> ruleEntity) {
        if ( ruleEntity == null ) {
            return null;
        }

        List<RuleVO> list = new ArrayList<RuleVO>();
        for ( Rule rule : ruleEntity ) {
            list.add( convertToRuleVO( rule ) );
        }

        return list;
    }

    @Override
    public Rule convertToRule(RuleVO ruleVO) {
        if ( ruleVO == null ) {
            return null;
        }
        Rule rule = new Rule();
        rule.setIdRule( ruleVO.getIdRule() );
        rule.setAgentId(ruleVO.getAgentId());
        rule.setLegacyRuleId(ruleVO.getLegacyRuleId());
        int level = getValueByDescription(ruleVO.getLevel());
        if(level == -1){
        	log.warn("level returned -1:"+ruleVO.getLevel());
        }
        rule.setLevel(level);
        rule.setType(ruleVO.getType());
        rule.setConditions(paMapper.convertToPossibleAnswerList(ruleVO.getConditions()));
        rule.setRuleAdditionalfields(additionalFieldMapper.convertToRuleAdditionalFieldList(ruleVO.getRuleAdditionalfields()));
        
        return rule;
    }

    @Override
    public List<Rule> convertToRuleList(List<RuleVO> ruleVO) {
        if ( ruleVO == null ) {
            return null;
        }

        List<Rule> list = new ArrayList<Rule>();
        for ( RuleVO ruleVO_ : ruleVO ) {
            list.add( convertToRule( ruleVO_ ) );
        }

        return list;
    }

	@Override
	public RuleVO convertToRuleVOExcPa(Rule rule) {
		if ( rule == null ) {
            return null;
        }

        RuleVO ruleVO = new RuleVO();

        ruleVO.setIdRule( rule.getIdRule() );
        ruleVO.setLastUpdated( rule.getLastUpdated() ); 
        ruleVO.setAgentId(rule.getAgentId());
        ruleVO.setLegacyRuleId(rule.getLegacyRuleId());
        ruleVO.setLevel(getDescriptionByValue(rule.getLevel()));
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
		 if ( ruleEntity == null ) {
	            return null;
	        }

	        List<RuleVO> list = new ArrayList<RuleVO>();
	        for ( Rule rule : ruleEntity ) {
	            list.add( convertToRuleVOExcPa( rule ) );
	        }

	        return list;
	}
	
	private String getDescriptionByValue(int value){
		for(RuleLevelEnum x: RuleLevelEnum.values()){
			if(x.getValue() == value){
				return x.getDescription();
			}
		}
		return "";
	}
	
	private int getValueByDescription(String description){
		for(RuleLevelEnum x: RuleLevelEnum.values()){
			if(x.getDescription().equals(description)){
				return x.getValue();
			}
		}
		return -1;
	}
    
}

