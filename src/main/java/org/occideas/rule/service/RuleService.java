package org.occideas.rule.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.RuleVO;

import java.util.List;

public interface RuleService extends BaseService<RuleVO> {

  void saveOrUpdate(RuleVO o);

  List<RuleVO> findByAgentId(long agentId);
}
