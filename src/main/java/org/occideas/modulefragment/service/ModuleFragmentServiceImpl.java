package org.occideas.modulefragment.service;

import org.occideas.mapper.ModuleFragmentMapper;
import org.occideas.modulefragment.dao.IModuleFragmentDao;
import org.occideas.vo.ModuleFragmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ModuleFragmentServiceImpl implements ModuleFragmentService {

  @Autowired
  private IModuleFragmentDao dao;
  @Autowired
  private ModuleFragmentMapper mapper;

  @Override
  public List<ModuleFragmentVO> getModuleFragmentByModuleId(long moduleId) {
    return mapper.convertToModuleFragmentList(dao.getModuleFragmentByModuleId(moduleId));
  }

}
