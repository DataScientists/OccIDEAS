package org.occideas.modulerule.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.ModuleRuleVO;

import java.util.List;

public interface ModuleRuleService extends BaseService<ModuleRuleVO> {

  List<ModuleRuleVO> findByModuleId(Long id);

  List<ModuleRuleVO> findByAgentId(Long id);

  List<ModuleRuleVO> findByIdNode(Long id);

  Number getRuleCountById(Long id);

  List<ModuleRuleVO> findByModuleIdAndAgentId(Long moduleId, Long agentId);
}
