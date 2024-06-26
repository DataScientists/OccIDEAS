package org.occideas.modulerule.service;

import org.occideas.entity.ModuleRule;
import org.occideas.mapper.ModuleRuleMapper;
import org.occideas.modulerule.dao.ModuleRuleDao;
import org.occideas.vo.ModuleRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ModuleRuleServiceImpl implements ModuleRuleService {

  @Autowired
  private ModuleRuleDao dao;

  @Autowired
  private ModuleRuleMapper mapper;

  @Override
  public List<ModuleRuleVO> listAll() {
    return mapper.convertToModuleRuleVOList(dao.getAll());
  }

  @Override
  public List<ModuleRuleVO> findById(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  public ArrayList<ModuleRuleVO> findByModuleId(Long id) {
    List<ModuleRule> modules = dao.getRulesByModule(id);
    ArrayList<ModuleRuleVO> modulesVO = new ArrayList<ModuleRuleVO>();
    modulesVO.addAll(mapper.convertToModuleRuleVOList(modules));
    return modulesVO;
  }

  public ArrayList<ModuleRuleVO> findByModuleIdAndAgentId(Long moduleId, Long agentId) {
    List<ModuleRule> modules = dao.getRulesByModuleAndAgent(moduleId, agentId);
    ArrayList<ModuleRuleVO> modulesVO = new ArrayList<ModuleRuleVO>();
    modulesVO.addAll(mapper.convertToModuleRuleVOList(modules));
    return modulesVO;
  }

  public List<ModuleRuleVO> findByIdNode(Long id) {
    List<ModuleRule> modules = dao.getRulesByIdNode(id);
    ArrayList<ModuleRuleVO> modulesVO = new ArrayList<ModuleRuleVO>();
    modulesVO.addAll(mapper.convertToModuleRuleVOList(modules));
    return modulesVO;
  }

  public List<ModuleRuleVO> findByAgentId(Long id) {
    List<ModuleRule> modules = dao.getRulesByAgent(id);
    ArrayList<ModuleRuleVO> modulesVO = new ArrayList<ModuleRuleVO>();
    modulesVO.addAll(mapper.convertToModuleRuleVOList(modules));
    return modulesVO;
  }


  @Override
  public ModuleRuleVO create(ModuleRuleVO o) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void update(ModuleRuleVO o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(ModuleRuleVO o) {
    // TODO Auto-generated method stub

  }

  @Override
  public Number getRuleCountById(Long id) {
    return dao.getRuleCountById(id);
  }


}
