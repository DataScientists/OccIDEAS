package org.occideas.mapper;

import org.mapstruct.Mapper;
import org.occideas.entity.ModuleIntroModule;
import org.occideas.vo.ModuleIntroModuleVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModuleIntroModuleMapper {

  ModuleIntroModuleVO convertToModuleIntroModuleVO(ModuleIntroModule entity);

  List<ModuleIntroModuleVO> convertToModuleIntroModuleList(List<ModuleIntroModule> list);

}
