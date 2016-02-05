package org.occideas.fragment.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.FragmentVO;

public interface FragmentService extends BaseService<FragmentVO>{
	public void createFragment(FragmentVO fragmentVO);
}
