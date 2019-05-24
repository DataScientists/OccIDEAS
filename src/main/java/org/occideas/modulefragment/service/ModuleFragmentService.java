package org.occideas.modulefragment.service;

import org.occideas.vo.ModuleFragmentVO;

import java.util.List;

public interface ModuleFragmentService {

  List<ModuleFragmentVO> getModuleFragmentByModuleId(long moduleId);

}
