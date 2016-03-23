package org.occideas.fragment.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.FragmentVO;

public interface FragmentService extends BaseService<FragmentVO>{
	public void createFragment(FragmentVO fragmentVO);

	public void merge(FragmentVO json);

	boolean checkExists(Long id);

	List<FragmentVO> findByIdForInterview(Long id);
}
