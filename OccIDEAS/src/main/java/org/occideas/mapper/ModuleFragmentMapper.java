package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.ModuleFragment;
import org.occideas.vo.ModuleFragmentVO;

@Mapper(componentModel = "spring")
public interface ModuleFragmentMapper {

	ModuleFragmentVO convertToModuleFragmentVO(ModuleFragment entity);
	
	List<ModuleFragmentVO> convertToModuleFragmentList(List<ModuleFragment> list);
}
