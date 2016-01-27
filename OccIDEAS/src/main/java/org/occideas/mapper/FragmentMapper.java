package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.Fragment;
import org.occideas.vo.FragmentVO;

//@Mapper(componentModel = "spring",uses=NodeMapper.class)
public interface FragmentMapper {

//	@Mapping(target = "notes", ignore=true)
	FragmentVO convertToFragmentVO(Fragment moduleEntity,boolean includeChild);
	
//	@Mapping(target = "notes", ignore=true)
	List<FragmentVO> convertToFragmentVOList(List<Fragment> moduleEntity,boolean includeChild);

//	@Mapping(target = "notes", ignore=true)
	Fragment convertToFragment(FragmentVO moduleVO);
	
//	@Mapping(target = "notes", ignore=true)
	List<Fragment> convertToFragmentList(List<FragmentVO> moduleVO);
}
