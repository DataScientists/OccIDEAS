package org.occideas.moduleintromodule.service;

import java.util.List;

import org.occideas.vo.ModuleIntroModuleVO;
import org.occideas.vo.ModuleVO;

public interface ModuleIntroModuleService {

	List<ModuleIntroModuleVO> getModuleIntroModuleByModuleId(long moduleId);
	
	List<ModuleVO> findByIdWithFragments(Long id);
	
}
