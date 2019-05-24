package org.occideas.modulefragment.service;

import java.util.List;

import org.occideas.vo.ModuleFragmentVO;

public interface ModuleFragmentService {

	List<ModuleFragmentVO> getModuleFragmentByModuleId(long moduleId);
	
}
