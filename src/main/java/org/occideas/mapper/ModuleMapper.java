package org.occideas.mapper;

import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.entity.JobModule;
import org.occideas.vo.InterviewIntroModuleModuleVO;
import org.occideas.vo.ModuleVO;

import java.util.List;

//@Mapper(componentModel = "spring",uses=NodeMapper.class)
public interface ModuleMapper {

  //	@Mapping(target = "notes", ignore=true)
  ModuleVO convertToModuleVO(JobModule moduleEntity, boolean includeChild);

  ModuleVO convertToInterviewModuleVO(JobModule moduleEntity);

  List<InterviewIntroModuleModuleVO> convertToInterviewModuleListVO(List<InterviewIntroModuleModule> moduleList);

  //	@Mapping(target = "notes", ignore=true)
  List<ModuleVO> convertToModuleVOList(List<JobModule> moduleEntity, boolean includeChild);

  //	@Mapping(target = "notes", ignore=true)
  JobModule convertToModule(ModuleVO moduleVO, boolean includeChild);

  //	@Mapping(target = "notes", ignore=true)
  List<JobModule> convertToModuleList(List<ModuleVO> moduleVO, boolean includeChild);

  ModuleVO convertToModuleWithFlagsVO(JobModule moduleEntity, boolean includeChildNodes, boolean includeRules);

  ModuleVO convertToModuleVOOnly(JobModule moduleEntity);
}
