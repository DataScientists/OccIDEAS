package org.occideas.question.service;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.Node;
import org.occideas.entity.NodesAgent;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.PossibleAnswerMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.question.dao.IQuestionDao;
import org.occideas.utilities.CommonUtil;
import org.occideas.utilities.StudyAgentUtil;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

  @Autowired
  private IQuestionDao qdao;

  @Autowired
  private BaseDao dao;

  @Autowired
  private QuestionMapper mapper;

  @Autowired
  private PossibleAnswerMapper paMapper;

  @Autowired
  private ModuleMapper modMapper;
  @Autowired
  private StudyAgentUtil studyAgentUtil;

  @Override
  public List<QuestionVO> listAll() {
    return mapper.convertToQuestionVOList(dao.getAll(Question.class));
  }

  @Override
  public List<QuestionVO> findById(Long id) {
    Question question = dao.get(Question.class, id);
    QuestionVO questionVO = mapper.convertToQuestionVO(question);
    Long parentId = Long.valueOf(question.getParentId());
    if (parentId != question.getTopNodeId()) {
      PossibleAnswer answer = dao.get(PossibleAnswer.class, parentId);
      questionVO.setParent(paMapper.convertToPossibleAnswerVO(answer, false));
    }


    List<QuestionVO> list = new ArrayList<QuestionVO>();
    list.add(questionVO);
    return list;
  }

  @Override
  public QuestionVO create(QuestionVO o) {
    Question question = new Question();
    question.setIdNode(dao.save(mapper.convertToQuestion(o)));
    studyAgentUtil.createStudyAgentForUpdatedNode(o.getTopNodeId(), o.getName());
    return mapper.convertToQuestionVO(question);
  }

  @Override
  public void update(QuestionVO o) {
    dao.saveOrUpdate(mapper.convertToQuestion(o));
    studyAgentUtil.createStudyAgentForUpdatedNode(o.getTopNodeId(), o.getName());
  }

  @Override
  public void delete(QuestionVO o) {
    dao.delete(mapper.convertToQuestion(o));
    studyAgentUtil.createStudyAgentForUpdatedNode(o.getTopNodeId(), o.getName());
  }

  @Override
  public QuestionVO determineNextQuestionByCurrentNumber(String moduleId, String nodeNumber) {
    String number = CommonUtil.getNextQuestionByCurrentNumber(nodeNumber);
    return mapper.convertToQuestionVOReducedDetails(qdao.getQuestionByModuleIdAndNumber(moduleId, number));
  }

  @Override
  public List<QuestionVO> getQuestionsWithSingleChildLevel(Long Id) {
    List<QuestionVO> list = new ArrayList<QuestionVO>();
    list.add(mapper.convertToInterviewQuestionVO(dao.get(Question.class, Id)));
    return list;
  }

  @Override
  public ModuleVO getQuestionWithParentId(Long idNode) {
    return modMapper.convertToModuleVO(qdao.getModuleByParentId(idNode), false);
  }

  @Override
  public List<QuestionVO> getQuestionsWithParentId(String parentId) {
    return mapper.convertToInterviewQuestionVOList(qdao.getQuestionsByParentId(parentId));
  }

  @Override
  public List<QuestionVO> getAllMultipleQuestions() {
    return mapper.convertToInterviewQuestionVOList(qdao.getAllMultipleQuestions());
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateWithIndependentTransaction(QuestionVO o) {
    dao.saveOrUpdate(mapper.convertToQuestion(o));
  }

  @Override
  public Question findMultipleQuestion(long questionId) {
    return qdao.findMultipleQuestion(questionId);
  }

  @Override
  public NodeVO getTopModuleByTopNodeId(long topNodeId) {
    Node node = qdao.getTopModuleByTopNodeId(topNodeId);
    if ("M".equals(node.getNodeclass())) {
      ModuleVO moduleVO = new ModuleVO();
      moduleVO.setAnchorId(node.getIdNode());
      moduleVO.setIdNode(node.getIdNode());
      moduleVO.setDeleted(node.getDeleted());
      moduleVO.setDescription(node.getDescription());
      moduleVO.setName(node.getName());
      moduleVO.setNumber(node.getNumber());
      moduleVO.setNodeclass(node.getNodeclass());
      moduleVO.setTopNodeId(node.getTopNodeId());
      moduleVO.setLink(node.getLink());
      return moduleVO;
    } else {
      FragmentVO fragmentVO = new FragmentVO();
      fragmentVO.setAnchorId(node.getIdNode());
      fragmentVO.setIdNode(node.getIdNode());
      fragmentVO.setName(node.getName());
      fragmentVO.setNodeclass(node.getNodeclass());
      fragmentVO.setNumber(node.getNumber());
      fragmentVO.setTopNodeId(node.getTopNodeId());
      return fragmentVO;
    }
  }

  @Override
  public List<NodesAgent> getNodesWithAgent(long agentId) {
    return qdao.getNodesWithAgent(agentId);
  }

  @Override
  public QuestionVO findByIdExcludeChildren(long idNode) {
    return mapper.convertToQuestionWithFlagsVO(qdao.get(idNode), false,false);
  }

  @Override
  public QuestionVO getQuestionByLinkIdAndTopId(long linkId, long topId) {
     return mapper.convertToQuestionWithFlagsVO(qdao.getQuestionByLinkIdAndTopId(linkId, topId), true,false);
  }

  @Override
  public QuestionVO getQuestionByTopIdAndNumber(Long topNodeId, String number) {
    return mapper.convertToQuestionVO(qdao.getQuestionByTopIdAndNumber(topNodeId, number));
  }

  @Override
  public QuestionVO getNearestQuestionByLinkIdAndTopId(long linkId, long topId) {
    return mapper.convertToQuestionWithFlagsVO(qdao.getNearestQuestionByLinkIdAndTopId(linkId, topId), true,false);
  }

}
