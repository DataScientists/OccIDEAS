package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.ModuleRule;
import org.occideas.vo.ModuleRuleVO;

@Mapper(componentModel = "spring")
public interface ModuleRuleMapper {

	ModuleRuleVO convertToModuleRuleVO(ModuleRule moduleRuleEntity);
	
	List<ModuleRuleVO> convertToModuleRuleVOList(List<ModuleRule> moduleRuleEntities);

	ModuleRule convertToModuleRule(ModuleRuleVO moduleRuleVO);

	List<ModuleRule> convertToModuleRuleList(List<ModuleRuleVO> moduleRuleVO);
	
}
