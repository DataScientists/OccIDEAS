package org.occideas.mapper;

import org.mapstruct.Mapper;
import org.occideas.entity.AgentGroup;
import org.occideas.vo.AgentGroupVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgentGroupMapper {

  AgentGroupVO convertToAgentGroupVO(AgentGroup agent);

  List<AgentGroupVO> convertToAgentGroupVOList(List<AgentGroup> agentList);

  AgentGroup convertToAgentGroup(AgentGroupVO agentVO);

  List<AgentGroup> convertToAgentGroupList(List<AgentGroupVO> agentVO);

}
