package org.occideas.modulerule.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.ModuleRuleVO;

public interface ModuleRuleService extends BaseService<ModuleRuleVO>{

	List<ModuleRuleVO> findByModuleId(Long id);

	List<ModuleRuleVO> findByAgentId(Long id);
	
	List<ModuleRuleVO> findByIdNode(Long id);

	Number getRuleCountById(Long id);
}
