package org.occideas.agent.service;

import org.occideas.agent.dao.IAgentDao;
import org.occideas.entity.Agent;
import org.occideas.entity.AgentGroup;
import org.occideas.entity.Constant;
import org.occideas.mapper.AgentMapper;
import org.occideas.security.handler.TokenManager;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.AgentGroupVO;
import org.occideas.vo.AgentVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AgentServiceImpl implements AgentService {

  @Autowired
  private IAgentDao dao;
  @Autowired
  private SystemPropertyService sysPropService;

  @Autowired
  private AgentMapper mapper;

  @Override
  public List<AgentVO> listAll() {

    return mapper.convertToAgentVOList(dao.getAllActive(), false);
  }

  @Override
  public List<AgentVO> getStudyAgents() {

    return mapper.convertToAgentVOList(dao.getStudyAgents(), false);
  }

  @Override
  public List<AgentVO> findById(Long id) {
    Agent module = dao.get(id);
    AgentVO moduleVO = mapper.convertToAgentVO(module, true);
    List<AgentVO> list = new ArrayList<AgentVO>();
    list.add(moduleVO);
    return list;
  }

  @Override
  public AgentVO create(AgentVO o) {
    Agent moduleEntity = dao.save(mapper.convertToAgent(o, false));
    return mapper.convertToAgentVO(moduleEntity, false);
  }

  @Override
  public void update(AgentVO o) {
    dao.saveOrUpdate(mapper.convertToAgent(o, false));
  }

  @Override
  public long updateAndGetId(AgentVO o) {
    return dao.saveOrUpdate(mapper.convertToAgent(o, false));
  }

  @Override
  public void delete(AgentVO o) {
    dao.delete(mapper.convertToAgent(o, false));
  }

  @Override
  public void updateStudyAgents(AgentVO json) {
    // insert into sys config
    SystemPropertyVO sysPropVO = new SystemPropertyVO();
    sysPropVO.setName(json.getName());
    sysPropVO.setType(Constant.STUDY_AGENT_SYS_PROP);
    sysPropVO.setValue(String.valueOf(json.getIdAgent()));
    sysPropVO.setUpdatedBy(new TokenManager().extractUserFromToken());
    sysPropService.save(sysPropVO);
  }

  @Override
  public List<SystemPropertyVO> loadStudyAgents() {
    return sysPropService.getByType(Constant.STUDY_AGENT_SYS_PROP);
  }

  @Override
  public void deleteStudyAgents(SystemPropertyVO vo) {
    sysPropService.delete(vo);
  }

  @Override
  public void saveAgentGroup(AgentGroupVO vo) {

    AgentGroup group = mapper.convertToAgentGroup(vo);
    Long id = dao.saveAgentGroup(group);
    group.setIdAgent(id);
    for (Agent agent : vo.getAgents()) {
      agent.setGroup(group);
      dao.saveOrUpdate(agent);
    }
  }
}
