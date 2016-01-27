package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.Agent;
import org.occideas.vo.AgentVO;

@Mapper(componentModel = "spring")
public interface AgentMapper {

	AgentVO convertToAgentVO(Agent agent);

	List<AgentVO> convertToAgentVOList(List<Agent> agentList);

	Agent convertToAgent(AgentVO agentVO);
	
	List<Agent> convertToAgentList(List<AgentVO> agentVO);
	
}
