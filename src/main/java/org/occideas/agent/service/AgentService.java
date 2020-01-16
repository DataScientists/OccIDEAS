package org.occideas.agent.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.AgentGroupVO;
import org.occideas.vo.AgentVO;
import org.occideas.vo.SystemPropertyVO;

import java.util.List;

public interface AgentService extends BaseService<AgentVO> {

  long updateAndGetId(AgentVO o);

  void updateStudyAgents(AgentVO json);

  List<SystemPropertyVO> loadStudyAgents();

  void deleteStudyAgents(SystemPropertyVO vo);

  List<AgentVO> getStudyAgents();

  void saveAgentGroup(AgentGroupVO json);
}
