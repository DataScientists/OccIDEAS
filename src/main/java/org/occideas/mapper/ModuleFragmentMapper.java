package org.occideas.mapper;

import org.mapstruct.Mapper;
import org.occideas.entity.ModuleFragment;
import org.occideas.vo.ModuleFragmentVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModuleFragmentMapper {

  ModuleFragmentVO convertToModuleFragmentVO(ModuleFragment entity);

  List<ModuleFragmentVO> convertToModuleFragmentList(List<ModuleFragment> list);
}
