package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Rule;
import org.occideas.vo.RuleVO;
import org.springframework.stereotype.Component;

@Component
public class RuleMapperImpl implements RuleMapper {

    @Override
    public RuleVO convertToRuleVO(Rule ruleEntity) {
        if ( ruleEntity == null ) {
            return null;
        }

        RuleVO ruleVO = new RuleVO();

        ruleVO.setIdRule( ruleEntity.getIdRule() );
        ruleVO.setLastUpdated( ruleEntity.getLastUpdated() ); 
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
        rule.setLastUpdated( ruleVO.getLastUpdated() );            
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

