package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.Fragment;
import org.occideas.vo.FragmentVO;

public interface FragmentMapper {

	FragmentVO convertToFragmentVO(Fragment moduleEntity,boolean includeChild);
	
	List<FragmentVO> convertToFragmentVOList(List<Fragment> moduleEntity,boolean includeChild);

	Fragment convertToFragment(FragmentVO moduleVO);
	
	List<Fragment> convertToFragmentList(List<FragmentVO> moduleVO);
}
