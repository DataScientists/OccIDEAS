package org.occideas.module.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;

public interface ModuleService extends BaseService<ModuleVO>{
	public void merge(ModuleVO module);
	public Long getMaxId();
}
