package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Agent;
import org.occideas.entity.AgentGroup;
import org.occideas.vo.AgentGroupVO;
import org.occideas.vo.AgentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgentMapperImpl implements AgentMapper {

	@Autowired
    private AgentGroupMapper agentGroupMapper;
	
	@Autowired
    private RuleMapper ruleMapper;
	
    @Override
    public AgentVO convertToAgentVO(Agent agent,boolean includeRules) {
        if ( agent == null ) {
            return null;
        }

        AgentVO agentVO = new AgentVO();

        agentVO.setIdAgent( agent.getIdAgent() );
        agentVO.setName( agent.getName() );
        agentVO.setDescription( agent.getDescription() );       
        agentVO.setLastUpdated( agent.getLastUpdated() ); 
        agentVO.setAgentGroup(agentGroupMapper.convertToAgentGroupVO(agent.getGroup()));
        agentVO.setDeleted( agent.getDeleted() );
        if(includeRules){
        	agentVO.setRules(ruleMapper.convertToRuleVOExcPaList(agent.getRules()));
        }
        return agentVO;
    }

    @Override
    public List<AgentVO> convertToAgentVOList(List<Agent> moduleEntity,boolean includeRules) {
        if ( moduleEntity == null ) {
            return null;
        }

        List<AgentVO> list = new ArrayList<AgentVO>();
        for ( Agent module : moduleEntity ) {
            list.add( convertToAgentVO( module, includeRules) );
        }

        return list;
    }

    @Override
    public Agent convertToAgent(AgentVO agentVO,boolean includeRules) {
        if ( agentVO == null ) {
            return null;
        }

        Agent agent = new Agent();

        agent.setIdAgent( agentVO.getIdAgent() );
        agent.setName( agentVO.getName() );
        agent.setDescription( agentVO.getDescription() );
        agent.setGroup( agentGroupMapper.convertToAgentGroup(agentVO.getAgentGroup()));
        agent.setLastUpdated( agentVO.getLastUpdated() );            
        agent.setDeleted( agentVO.getDeleted() );

        if(includeRules){
        	agent.setRules(ruleMapper.convertToRuleExcPaList(agentVO.getRules()));
        }

        return agent;
    }

    @Override
    public List<Agent> convertToAgentList(List<AgentVO> agentVO,boolean includeRules) {
        if ( agentVO == null ) {
            return null;
        }

        List<Agent> list = new ArrayList<Agent>();
        for ( AgentVO agent : agentVO ) {
            list.add( convertToAgent( agent ,includeRules) );
        }

        return list;
    }

	@Override
	public AgentGroup convertToAgentGroup(AgentGroupVO vo) {
		if ( vo == null ) {
            return null;
        }

       AgentGroup group = new AgentGroup();
       group.setDeleted(vo.getDeleted());
       group.setDescription(vo.getDescription());
       group.setName(vo.getName());
       
       return group;       
	}    
}

