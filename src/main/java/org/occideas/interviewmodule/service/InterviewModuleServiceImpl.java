package org.occideas.interviewmodule.service;

import org.occideas.base.dao.BaseDao;
import org.occideas.mapper.InterviewModuleMapper;
import org.occideas.vo.InterviewModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InterviewModuleServiceImpl implements InterviewModuleService {

  @Autowired
  private BaseDao dao;

  @Autowired
  private InterviewModuleMapper mapper;

  @Override
  public List<InterviewModuleVO> listAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<InterviewModuleVO> findById(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public InterviewModuleVO create(InterviewModuleVO o) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void update(InterviewModuleVO vo) {
    dao.saveOrUpdate(mapper.convertToInterviewModule(vo));
  }

  @Override
  public void delete(InterviewModuleVO o) {
    // TODO Auto-generated method stub

  }

}
