package org.occideas.mapper;

import org.mapstruct.Mapper;
import org.occideas.entity.Agent;
import org.occideas.entity.AgentGroup;
import org.occideas.vo.AgentGroupVO;
import org.occideas.vo.AgentVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgentMapper {

  AgentVO convertToAgentVO(Agent agent, boolean includeRules);

  List<AgentVO> convertToAgentVOList(List<Agent> agentList, boolean includeRules);

  Agent convertToAgent(AgentVO agentVO, boolean includeRules);

  List<Agent> convertToAgentList(List<AgentVO> agentVO, boolean includeRules);

  AgentGroup convertToAgentGroup(AgentGroupVO vo);

}
