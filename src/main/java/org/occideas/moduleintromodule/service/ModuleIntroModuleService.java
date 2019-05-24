package org.occideas.moduleintromodule.service;

import org.occideas.vo.ModuleIntroModuleVO;
import org.occideas.vo.ModuleVO;

import java.util.List;

public interface ModuleIntroModuleService {

  List<ModuleIntroModuleVO> getModuleIntroModuleByModuleId(long moduleId);

  List<ModuleVO> findByIdWithFragments(Long id);

}
