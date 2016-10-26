package org.occideas.agent.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.AgentVO;
import org.occideas.vo.SystemPropertyVO;

public interface AgentService extends BaseService<AgentVO>{

	void updateStudyAgents(AgentVO json);

	List<SystemPropertyVO> loadStudyAgents();

	void deleteStudyAgents(SystemPropertyVO vo);
}
