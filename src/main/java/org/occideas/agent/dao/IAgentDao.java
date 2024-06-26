package org.occideas.agent.dao;

import org.occideas.entity.Agent;
import org.occideas.entity.AgentGroup;
import org.occideas.entity.AgentPlain;

import java.util.List;

public interface IAgentDao {

  void deleteAll();

  List<Long> getStudyAgentIds();

  Long saveAgentGroup(AgentGroup group);

  List<Agent> getStudyAgents();

  List<Agent> getAllActive();

  List<Agent> getAll();

  long saveOrUpdate(Agent module);

  Agent merge(Agent module);

  Agent get(Long id);

  void delete(Agent module);

  Agent save(Agent module);

  void saveBatchAgents(List<Agent> agents);

  void saveBatchAgentsPlain(List<AgentPlain> copyAgentInfoPlainFromDB);

}
