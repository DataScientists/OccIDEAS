package org.occideas.module.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.ModuleVO;

public interface ModuleService extends BaseService<ModuleVO>{
	public void merge(ModuleVO module);
	public Long getMaxId();
	List<ModuleVO> findByIdForInterview(Long id);
}
