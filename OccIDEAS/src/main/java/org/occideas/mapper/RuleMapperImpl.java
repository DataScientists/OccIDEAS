package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Rule;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RuleMapperImpl implements RuleMapper {

	@Autowired
	private PossibleAnswerMapper paMapper;
	
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
        ruleVO.setLevel(ruleEntity.getLevel());
        ruleVO.setType(ruleEntity.getType());
        List<PossibleAnswer> conditions = ruleEntity.getConditions();
		if (!CommonUtil.isListEmpty(conditions)) {
			ruleVO.setConditions(paMapper.convertToPossibleAnswerVOList(conditions,false));
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
        rule.setLevel(ruleVO.getLevel());
        rule.setType(ruleVO.getType());
        rule.setConditions(paMapper.convertToPossibleAnswerList(ruleVO.getConditions()));
        //rule.setAdditionalfields(additionalfields);
        
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
    
}

