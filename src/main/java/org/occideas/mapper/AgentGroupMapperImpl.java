package org.occideas.mapper;

import org.occideas.entity.AgentGroup;
import org.occideas.vo.AgentGroupVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgentGroupMapperImpl implements AgentGroupMapper {

  @Override
  public AgentGroupVO convertToAgentGroupVO(AgentGroup moduleEntity) {
    if (moduleEntity == null) {
      return null;
    }

    AgentGroupVO moduleVO = new AgentGroupVO();

    moduleVO.setIdAgent(moduleEntity.getIdAgent());
    moduleVO.setName(moduleEntity.getName());
    moduleVO.setDescription(moduleEntity.getDescription());
    moduleVO.setLastUpdated(moduleEntity.getLastUpdated());
    moduleVO.setDeleted(moduleEntity.getDeleted());
    return moduleVO;
  }

  @Override
  public List<AgentGroupVO> convertToAgentGroupVOList(List<AgentGroup> moduleEntity) {
    if (moduleEntity == null) {
      return null;
    }

    List<AgentGroupVO> list = new ArrayList<AgentGroupVO>();
    for (AgentGroup module : moduleEntity) {
      list.add(convertToAgentGroupVO(module));
    }

    return list;
  }

  @Override
  public AgentGroup convertToAgentGroup(AgentGroupVO moduleVO) {
    if (moduleVO == null) {
      return null;
    }

    AgentGroup module = new AgentGroup();

    module.setIdAgent(moduleVO.getIdAgent());
    module.setName(moduleVO.getName());
    module.setDescription(moduleVO.getDescription());
    module.setLastUpdated(moduleVO.getLastUpdated());
    module.setDeleted(moduleVO.getDeleted());

    return module;
  }

  @Override
  public List<AgentGroup> convertToAgentGroupList(List<AgentGroupVO> moduleVO) {
    if (moduleVO == null) {
      return null;
    }

    List<AgentGroup> list = new ArrayList<AgentGroup>();
    for (AgentGroupVO moduleVO_ : moduleVO) {
      list.add(convertToAgentGroup(moduleVO_));
    }

    return list;
  }

}

