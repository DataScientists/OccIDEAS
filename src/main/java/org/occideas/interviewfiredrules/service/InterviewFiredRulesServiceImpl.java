package org.occideas.interviewfiredrules.service;

import org.occideas.entity.InterviewFiredRules;
import org.occideas.genericnode.dao.GenericNodeDao;
import org.occideas.interviewfiredrules.dao.InterviewFiredRulesDao;
import org.occideas.mapper.GenericNodeMapper;
import org.occideas.mapper.InterviewFiredRulesMapper;
import org.occideas.vo.GenericNodeVO;
import org.occideas.vo.InterviewFiredRulesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InterviewFiredRulesServiceImpl implements InterviewFiredRulesService {

  @Autowired
  private InterviewFiredRulesDao dao;
  @Autowired
  private InterviewFiredRulesMapper mapper;
  @Autowired
  private GenericNodeMapper genNodeMapper;
  @Autowired
  private GenericNodeDao genNodeDao;

  @Override
  public List<InterviewFiredRulesVO> listAll() {
    return null;
  }

  @Override
  public List<InterviewFiredRulesVO> findById(Long id) {
    return null;
  }

  @Override
  public InterviewFiredRulesVO create(InterviewFiredRulesVO vo) {
    InterviewFiredRules entity = dao.save(mapper.convertToInterviewFiredRules(vo));
    return mapper.convertToInterviewFiredRulesVO(entity);
  }

  @Override
  public void update(InterviewFiredRulesVO o) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(InterviewFiredRulesVO o) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<InterviewFiredRulesVO> findByInterviewId(Long interviewId) {
    List<InterviewFiredRules> entity = dao.findByInterviewId(interviewId);
    return mapper.convertToInterviewFiredRulesVOWithRulesList(entity);
  }


  @Override
  public GenericNodeVO findNodeById(long idNode) {
    return genNodeMapper.convertToGenNodeVO(genNodeDao.get(idNode));
  }


}
