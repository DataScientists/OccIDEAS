package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.Module;
import org.occideas.vo.ModuleVO;

//@Mapper(componentModel = "spring",uses=NodeMapper.class)
public interface ModuleMapper {

//	@Mapping(target = "notes", ignore=true)
	ModuleVO convertToModuleVO(Module moduleEntity,boolean includeChild);
	
//	@Mapping(target = "notes", ignore=true)
	List<ModuleVO> convertToModuleVOList(List<Module> moduleEntity,boolean includeChild);

//	@Mapping(target = "notes", ignore=true)
	Module convertToModule(ModuleVO moduleVO);
	
//	@Mapping(target = "notes", ignore=true)
	List<Module> convertToModuleList(List<ModuleVO> moduleVO);
}