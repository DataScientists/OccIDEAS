package org.occideas.interviewintromodulemodule.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.entity.SystemProperty;
import org.occideas.interviewintromodulemodule.dao.InterviewIntroModuleModuleDao;
import org.occideas.mapper.InterviewIntroModuleModuleMapper;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.vo.InterviewIntroModuleModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class InterviewIntroModuleModuleServiceImpl implements InterviewIntroModuleModuleService {

  private Logger log = LogManager.getLogger(this.getClass());

  @Autowired
  private InterviewIntroModuleModuleDao dao;
  @Autowired
  private InterviewIntroModuleModuleMapper mapper;
  @Autowired
  private SystemPropertyDao systemPropertyDao;

  @Override
  public List<InterviewIntroModuleModuleVO> findInterviewByModuleId(long idModule) {
    List<InterviewIntroModuleModule> list = dao.getInterviewIntroModByModId(idModule);
    return mapper.convertToVOList(list);
  }

  @Override
  public List<InterviewIntroModuleModuleVO> findModulesByInterviewId(long idInterview) {
    List<InterviewIntroModuleModule> list = dao.getModulesByInterviewId(idInterview);
    return mapper.convertToVOList(list);
  }

  @Override
  public List<InterviewIntroModuleModuleVO> getDistinctModules() {
    return mapper.convertToVOList(dao.getDistinctModules());
  }

  @Override
  public List<InterviewIntroModuleModuleVO> findInterviewIdByModuleId(long idModule) {
    List<InterviewIntroModuleModule> list = dao.getInterviewByModuleId(idModule);
    return mapper.convertToVOList(list);
  }

  @Override
  public List<InterviewIntroModuleModuleVO> findNonIntroById(Long id) {
    SystemProperty activeIntro = systemPropertyDao.getByName("activeIntro");
    if (activeIntro == null || !StringUtils.isNumeric(activeIntro.getValue())) {
      log.error("Active intro is either null or is not numeric");
      return null;
    }
    List<InterviewIntroModuleModule> list = dao.findNonIntroById(id, activeIntro.getValue());
    return mapper.convertToVOList(list);
  }

}
