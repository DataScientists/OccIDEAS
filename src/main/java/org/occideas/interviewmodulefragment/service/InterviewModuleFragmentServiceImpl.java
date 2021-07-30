package org.occideas.interviewmodulefragment.service;

import org.occideas.entity.InterviewModuleFragment;
import org.occideas.interviewmodulefragment.dao.InterviewModuleFragmentDao;
import org.occideas.mapper.InterviewModuleFragmentMapper;
import org.occideas.vo.InterviewModuleFragmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class InterviewModuleFragmentServiceImpl implements InterviewModuleFragmentService {

  @Autowired
  private InterviewModuleFragmentDao dao;
  @Autowired
  private InterviewModuleFragmentMapper mapper;

  @Override
  public List<InterviewModuleFragmentVO> findInterviewByFragmentId(long id) {
    List<InterviewModuleFragment> list = dao.getModFragmentById(id);
    return mapper.convertToVOList(list);
  }

  @Override
  public List<InterviewModuleFragmentVO> findFragmentByInterviewId(long id) {
    List<InterviewModuleFragment> list = dao.getModFragmentByInterviewId(id);
    return mapper.convertToVOList(list);
  }

}
