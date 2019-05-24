package org.occideas.interviewquestion.service;

import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewQuestion;
import org.occideas.interviewquestion.dao.IInterviewQuestionDao;
import org.occideas.mapper.InterviewQuestionMapper;
import org.occideas.vo.InterviewQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class InterviewQuestionServiceImpl implements InterviewQuestionService {

  @Autowired
  private IInterviewQuestionDao dao;

  @Autowired
  private InterviewQuestionMapper mapper;

  @Override
  public List<InterviewQuestionVO> listAll() {
    return mapper.convertToInterviewQuestionVOList(dao.getAllActive());
  }

  public List<InterviewQuestionVO> listAllAssessments() {
    return mapper.convertToInterviewQuestionVOList(dao.getAllActive());
  }

  @Override
  public List<InterviewQuestionVO> findById(Long id) {
    InterviewQuestion interview = dao.get(id);
    InterviewQuestionVO InterviewQuestionVO = mapper.convertToInterviewQuestionVO(interview);
    List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
    list.add(InterviewQuestionVO);
    return list;
  }

  @Override
  public List<InterviewQuestionVO> findById(Long questionId, Long interviewId) {
    InterviewQuestion interview = dao.getByQuestionId(questionId, interviewId);
    InterviewQuestionVO InterviewQuestionVO = mapper.convertToInterviewQuestionVO(interview);
    List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
    list.add(InterviewQuestionVO);
    return list;
  }

  @Override
  public InterviewQuestionVO create(InterviewQuestionVO o) {
    // TODO: Hotfix - Just don't understand why it returns interviewId instead of object
    Object obj = dao.save(mapper.convertToInterviewQuestion(o));
    InterviewQuestion intervew = null;
    if (obj instanceof InterviewQuestion) {
      intervew = (InterviewQuestion) obj;
    } else if (obj instanceof Long) {
      intervew = dao.get((Long) obj);
    }

    return mapper.convertToInterviewQuestionVO(intervew);
  }

  @Override
  public InterviewQuestionVO updateIntQ(InterviewQuestionVO o) {
    return mapper.convertToInterviewQuestionVO(dao.saveOrUpdate(mapper.convertToInterviewQuestion(o)));
  }

  @Override
  public List<InterviewQuestionVO> updateIntQs(List<InterviewQuestionVO> o) {
    return mapper.convertToInterviewQuestionVOList(dao.saveOrUpdate(mapper.convertToInterviewQuestionList(o)));
  }


  @Override
  public void delete(InterviewQuestionVO o) {
    dao.delete(mapper.convertToInterviewQuestion(o));
  }

  @Override
  public List<InterviewQuestionVO> findByInterviewId(Long id) {
    List<InterviewQuestion> modules = dao.findByInterviewId(id);
    ArrayList<InterviewQuestionVO> modulesVO = new ArrayList<InterviewQuestionVO>();
    modulesVO.addAll(mapper.convertToInterviewQuestionVOList(modules));
    return modulesVO;
  }

  @Override
  public void update(InterviewQuestionVO o) {
    // TODO Auto-generated method stub

  }

  @Override
  public InterviewQuestionVO findIntQuestion(long idInterview, long questionId) {
    return mapper.convertToInterviewQuestionVO(dao.findIntQuestion(idInterview, questionId));
  }

  @Override
  public InterviewQuestionVO updateInterviewLinkAndQueueQuestions(InterviewQuestionVO o) {
    return mapper.convertToInterviewQuestionVO(dao.saveInterviewLinkAndQueueQuestions(mapper.convertToInterviewQuestion(o)));
  }

  @Override
  public Long getMaxIntQuestionSequence(long idInterview) {
    return dao.getMaxIntQuestionSequence(idInterview);
  }

  @Override
  public void updateModuleNameForInterviewId(long id, String newName) {
    dao.updateModuleNameForInterviewId(id, newName);
  }

  @Override
  public List<InterviewQuestion> getUniqueInterviewQuestions(String[] filterModule) {
    System.out.println(">>>>>>>>>>>>>>>>" + Arrays.toString(filterModule));
    return dao.getUniqueInterviewQuestions(filterModule);
  }


  @Override
  public Long getUniqueInterviewQuestionCount(String[] filterModule) {

    return dao.getUniqueInterviewQuestionCount(filterModule);
  }

  @Override
  public List<InterviewQuestionVO> findQuestionsByNodeId(Long questionId) {

    return mapper.convertToInterviewQuestionVOList(
      dao.getQuestionsByNodeId(questionId));
  }

  @Override
  public List<InterviewQuestionVO> getInterviewQuestionsByNodeIdAndIntId(Long questionId, Long idInterview) {
    return mapper.convertToInterviewQuestionVOWithAnswerList(
      dao.getInterviewQuestionsByNodeIdAndIntId(questionId, idInterview));
  }

  @Override
  public InterviewAnswer getInterviewAnswerByAnsIdAndIntId(Long answerId, Long idInterview) {
    return null;
  }
}
