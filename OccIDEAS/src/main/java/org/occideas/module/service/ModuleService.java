package org.occideas.module.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.ModuleCopyVO;
import org.occideas.vo.ModuleIdNodeRuleHolder;
import org.occideas.vo.ModuleVO;

public interface ModuleService extends BaseService<ModuleVO>{
	public void merge(ModuleVO module);
	public Long getMaxId();
	List<ModuleVO> findByIdForInterview(Long id);
	public ModuleIdNodeRuleHolder copyModule(ModuleCopyVO json);
	public void copyRules(ModuleIdNodeRuleHolder idNodeHolder);
	void addNodeRules(ModuleIdNodeRuleHolder idNodeHolder);
}
