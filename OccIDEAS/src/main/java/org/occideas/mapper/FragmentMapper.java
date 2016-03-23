package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.Fragment;
import org.occideas.vo.FragmentVO;

public interface FragmentMapper {

	FragmentVO convertToFragmentVO(Fragment moduleEntity,boolean includeChild);
	
	FragmentVO convertToInterviewFragmentVO(Fragment moduleEntity);
	
	List<FragmentVO> convertToFragmentVOList(List<Fragment> moduleEntity,boolean includeChild);
	
	List<FragmentVO> convertToInterviewFragmentVOList(List<Fragment> moduleEntity);

	Fragment convertToFragment(FragmentVO moduleVO,boolean includeChild);
	
	List<Fragment> convertToFragmentList(List<FragmentVO> moduleVO,boolean includeChild);
}
