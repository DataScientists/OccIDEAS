package org.occideas.module.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.ModuleVO;

public interface ModuleService extends BaseService<ModuleVO>{
	public void merge(ModuleVO module);
}
