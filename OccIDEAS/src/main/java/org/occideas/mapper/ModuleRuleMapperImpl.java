package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Agent;
import org.occideas.entity.Module;
import org.occideas.entity.ModuleRule;
import org.occideas.entity.Rule;
import org.occideas.vo.ModuleRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModuleRuleMapperImpl implements ModuleRuleMapper {

	@Autowired
    private AgentMapper agentMapper;
	
	@Autowired
    private RuleMapper ruleMapper;
	
	@Autowired
    private ModuleMapper moduleMapper;
	
	@Override
    public ModuleRuleVO convertToModuleRuleVO(ModuleRule moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        ModuleRuleVO moduleVO = new ModuleRuleVO();

        moduleVO.setAgent( agentMapper.convertToAgentVO(new Agent(moduleEntity.getIdAgent().longValue())));
        moduleVO.setModule( moduleMapper.convertToModuleVO(new Module(moduleEntity.getIdModule().longValue()),false));
        moduleVO.setRule( ruleMapper.convertToRuleVO(new Rule(moduleEntity.getIdRule().longValue())));
        //moduleVO.setNode( nodeMapper.convertToNodeVO(moduleEntity.getNode()));
        
        return moduleVO;
    }

    @Override
    public List<ModuleRuleVO> convertToModuleRuleVOList(List<ModuleRule> moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        List<ModuleRuleVO> list = new ArrayList<ModuleRuleVO>();
        for ( ModuleRule module : moduleEntity ) {
            list.add( convertToModuleRuleVO( module ) );
        }

        return list;
    }

    @Override
    public ModuleRule convertToModuleRule(ModuleRuleVO moduleVO) {
        if ( moduleVO == null ) {
            return null;
        }

        ModuleRule module = new ModuleRule();

        //module.setAgent( agentMapper.convertToAgent(moduleVO.getIdAgent()) );
        //module.setRule( ruleMapper.convertToRule(moduleVO.getIdRule()) );
        //module.setModule( moduleMapper.convertToModule(moduleVO.getIdModule()) );
        //module.setNode( nodeMapper.convertToNode(moduleVO.getNode()) );

        return module;
    }

    @Override
    public List<ModuleRule> convertToModuleRuleList(List<ModuleRuleVO> moduleVO) {
        if ( moduleVO == null ) {
            return null;
        }

        List<ModuleRule> list = new ArrayList<ModuleRule>();
        for ( ModuleRuleVO moduleVO_ : moduleVO ) {
            list.add( convertToModuleRule( moduleVO_ ) );
        }

        return list;
    }

}

