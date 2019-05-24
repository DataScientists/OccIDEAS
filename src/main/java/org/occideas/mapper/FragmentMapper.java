package org.occideas.mapper;

import org.occideas.entity.Fragment;
import org.occideas.vo.FragmentVO;

import java.util.List;

public interface FragmentMapper {

  FragmentVO convertToFragmentVO(Fragment moduleEntity, boolean includeChild);

  FragmentVO convertToInterviewFragmentVO(Fragment moduleEntity);

  List<FragmentVO> convertToFragmentVOList(List<Fragment> moduleEntity, boolean includeChild);

  List<FragmentVO> convertToInterviewFragmentVOList(List<Fragment> moduleEntity);

  Fragment convertToFragment(FragmentVO moduleVO, boolean includeChild);

  List<Fragment> convertToFragmentList(List<FragmentVO> moduleVO, boolean includeChild);
}
