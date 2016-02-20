package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.ModuleRule;
import org.occideas.vo.ModuleRuleVO;
import org.springframework.stereotype.Component;

@Component
public class ModuleRuleMapperImpl implements ModuleRuleMapper {


	@Override
    public ModuleRuleVO convertToModuleRuleVO(ModuleRule moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }
        ModuleRuleVO moduleVO = new ModuleRuleVO();
        moduleVO.setIdAgent(moduleEntity.getIdAgent());
        moduleVO.setAgentName(moduleEntity.getAgentName());
        moduleVO.setIdModule(moduleEntity.getIdModule());
        moduleVO.setModuleName(moduleEntity.getModuleName());
        moduleVO.setIdNode(moduleEntity.getIdNode());
        moduleVO.setNodeNumber(moduleEntity.getNodeNumber());
        moduleVO.setIdRule(moduleEntity.getIdRule());
        moduleVO.setRuleLevel(moduleEntity.getRuleLevel());
        
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

        return null;
    }

    @Override
    public List<ModuleRule> convertToModuleRuleList(List<ModuleRuleVO> moduleVO) {
        
        return null;
    }

}

