package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.entity.Module;
import org.occideas.vo.InterviewIntroModuleModuleVO;
import org.occideas.vo.ModuleVO;

//@Mapper(componentModel = "spring",uses=NodeMapper.class)
public interface ModuleMapper {

//	@Mapping(target = "notes", ignore=true)
	ModuleVO convertToModuleVO(Module moduleEntity,boolean includeChild);
	
	ModuleVO convertToInterviewModuleVO(Module moduleEntity);
	
	List<InterviewIntroModuleModuleVO> convertToInterviewModuleListVO(List<InterviewIntroModuleModule> moduleList);
	
//	@Mapping(target = "notes", ignore=true)
	List<ModuleVO> convertToModuleVOList(List<Module> moduleEntity,boolean includeChild);

//	@Mapping(target = "notes", ignore=true)
	Module convertToModule(ModuleVO moduleVO,boolean includeChild);
	
//	@Mapping(target = "notes", ignore=true)
	List<Module> convertToModuleList(List<ModuleVO> moduleVO,boolean includeChild);

	ModuleVO convertToModuleWithFlagsVO(Module moduleEntity, boolean includeChildNodes, boolean includeRules);

    ModuleVO convertToModuleVOOnly(Module moduleEntity);
}
