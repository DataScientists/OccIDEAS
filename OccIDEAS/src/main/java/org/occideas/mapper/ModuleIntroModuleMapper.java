package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.ModuleIntroModule;
import org.occideas.vo.ModuleIntroModuleVO;

@Mapper(componentModel = "spring")
public interface ModuleIntroModuleMapper {

	ModuleIntroModuleVO convertToModuleIntroModuleVO(ModuleIntroModule entity);
	
	List<ModuleIntroModuleVO> convertToModuleIntroModuleList(List<ModuleIntroModule> list);
	
}
