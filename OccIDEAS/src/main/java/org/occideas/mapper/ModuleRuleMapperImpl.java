package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.ModuleRule;
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

        moduleVO.setAgent( agentMapper.convertToAgentVO(moduleEntity.getAgent()));
        moduleVO.setModule( moduleMapper.convertToModuleVO(moduleEntity.getModule(),false));
        moduleVO.setRule( ruleMapper.convertToRuleVO(moduleEntity.getRule()));
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

        module.setAgent( agentMapper.convertToAgent(moduleVO.getAgent()) );
        module.setRule( ruleMapper.convertToRule(moduleVO.getRule()) );
        module.setModule( moduleMapper.convertToModule(moduleVO.getModule()) );
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

