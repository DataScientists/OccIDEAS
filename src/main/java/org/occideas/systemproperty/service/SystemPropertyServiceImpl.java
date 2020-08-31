package org.occideas.systemproperty.service;

import org.occideas.entity.*;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.PossibleAnswerMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.mapper.SystemPropertyMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.modulefragment.service.ModuleFragmentService;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.utilities.StudyAgentUtil;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class SystemPropertyServiceImpl implements SystemPropertyService {

  @Autowired
  private SystemPropertyMapper mapper;
  @Autowired
  private SystemPropertyDao dao;
  @Autowired
  private PossibleAnswerMapper posAnsMapper;
  @Autowired
  private IModuleDao moduleDao;
  @Autowired
  private QuestionMapper questionMapper;
  @Autowired
  private ModuleMapper moduleMapper;
  @Autowired
  private ModuleFragmentService moduleFragmentService;

  @Autowired
  private StudyAgentUtil studyAgentUtil;


  private List<Long> nodeIds = new ArrayList<Long>();


  //used to check if answer has already been processed
  private List<Long> possAnswerCheckList = new ArrayList<>();
  private List<Long> qIdCheckList = new ArrayList<>();

  @Override
  public SystemPropertyVO save(SystemPropertyVO sysProp) {
    SystemProperty systemProperty = dao.save(mapper.convertSytemPropertyVOtoSystemProperty(sysProp));
    return mapper.convertSytemPropertyToSystemPropertyVO(systemProperty);
  }

  @Override
  public SystemPropertyVO getById(long id) {
    SystemProperty sysProp = dao.getById(id);
    return mapper.convertSytemPropertyToSystemPropertyVO(sysProp);
  }

  @Override
  public List<SystemPropertyVO> getAll() {
    List<SystemProperty> list = dao.getAll();
    return mapper.convertSystemPropertyListToSystemPropertyVOList(list);
  }

  @Override
  public void delete(SystemPropertyVO vo) {
    dao.delete(mapper.convertSytemPropertyVOtoSystemProperty(vo));
  }

  @Override
  public SystemPropertyVO getByName(String name) {
    SystemProperty sysProp = dao.getByName(name);
    return mapper.convertSytemPropertyToSystemPropertyVO(sysProp);
  }

  @Override
  public List<SystemPropertyVO> getByType(String type) {
    List<SystemProperty> list = dao.getByType(type);
    return mapper.convertSystemPropertyListToSystemPropertyVOList(list);
  }

  @Override
  public FragmentVO getFragmentNodesWithStudyAgents(FragmentVO vo) {
    List<PossibleAnswerVO> posAnsWithStudyAgentsList = getAnswersWithStudyAgents(vo);
    vo.setChildNodes(buildChildNodesWithStudyAgents(posAnsWithStudyAgentsList));
    if (vo.getChildNodes().isEmpty()) {
      return null;
    }
    return vo;
  }


  private List<QuestionVO> filterQuestionNodesWithStudyAgents(List<QuestionVO> questionVOList,
                                                              List<Long> listOfIdNodesWithStudyAgents, List<QuestionVO> newQuestionVOList) {
    for (QuestionVO qVo : questionVOList) {
      List<PossibleAnswerVO> newPossibleAnswerList = new ArrayList<>();
      filterAnswerNodesWithStudyAgents(qVo.getChildNodes(), listOfIdNodesWithStudyAgents, newPossibleAnswerList);
      if (!newPossibleAnswerList.isEmpty()) {
        qVo.setChildNodes(newPossibleAnswerList);
        newQuestionVOList.add(qVo);
      }
    }

    return newQuestionVOList;
  }

  private void filterAnswerNodesWithStudyAgents(List<PossibleAnswerVO> childNodes,
                                                List<Long> listOfIdNodesWithStudyAgents, List<PossibleAnswerVO> newPossibleAnswerList) {
    for (PossibleAnswerVO aVo : childNodes) {
      boolean isPushed = false;
      if (listOfIdNodesWithStudyAgents.contains(aVo.getIdNode())) {
        newPossibleAnswerList.add(aVo);
        isPushed = true;
        // if an answer is identified with rules which is a study agent ,
        // we must add the other answers to this question
      }
      // additional check for child questions, verify if its answers has rules with study agent and
      // add to the hierarchy accordingly
      if (!isPushed && !aVo.getChildNodes().isEmpty()) {
        List<QuestionVO> newQuestionVOList = new ArrayList<>();
        filterQuestionNodesWithStudyAgents(aVo.getChildNodes(), listOfIdNodesWithStudyAgents,
          newQuestionVOList);
        if (!newQuestionVOList.isEmpty()) {
          aVo.setChildNodes(newQuestionVOList);
        } else {
          aVo.setChildNodes(null);
        }
      }
    }

  }

  @Override
  public ModuleVO filterModulesNodesWithStudyAgents(ModuleVO vo) {
    vo.getChildNodes().clear();
    possAnswerCheckList.clear();
    qIdCheckList.clear();
    List<PossibleAnswerVO> posAnsWithStudyAgentsList = getAnswersWithStudyAgents(vo);
    List<NodeVO> nodeWithStudyAgentsList = new ArrayList<>();
    // check child links if we have study agents
    addAnsDependencyFromLinkAjsmNew(vo, nodeWithStudyAgentsList);
    getStudyAgentsForLinks(nodeWithStudyAgentsList, posAnsWithStudyAgentsList, vo);
    addAnsDependencyFromModuleFragment(vo, posAnsWithStudyAgentsList);
    for (PossibleAnswerVO avo : posAnsWithStudyAgentsList) {
      //System.out.println(avo.getIdNode() + "-"+avo.getNumber());
    }

    if (posAnsWithStudyAgentsList.isEmpty() && nodeWithStudyAgentsList.isEmpty()) {
      return null;
    } else {
      vo.getChildNodes().addAll(buildChildNodesWithStudyAgents(posAnsWithStudyAgentsList));
      vo.getChildNodes().removeAll(Collections.singleton(null));
      //printAllQIdNumbers(vo);
    }
    return vo;
  }

  @Override
  public List<String> filterNodesWithStudyAgents(NodeVO vo) {
    vo.getChildNodes().clear();
    List<String> list = new ArrayList<>();
    possAnswerCheckList.clear();
    qIdCheckList.clear();
    List<PossibleAnswerVO> posAnsWithStudyAgentsList = getAnswersWithStudyAgents(vo);
    List<NodeVO> nodeWithStudyAgentsList = new ArrayList<>();
    // check child links if we have study agents
    if (vo == null) {
      return list;
    }

    addAnsDependencyFromLinkAjsmNew(vo, nodeWithStudyAgentsList);
    getStudyAgentsForLinks(nodeWithStudyAgentsList, posAnsWithStudyAgentsList, vo);
    addAnsDependencyFromModuleFragment(vo, posAnsWithStudyAgentsList);
    if (posAnsWithStudyAgentsList.isEmpty() && nodeWithStudyAgentsList.isEmpty()) {
      return null;
    } else {
      if (vo instanceof ModuleVO) {
        ((ModuleVO) vo).getChildNodes().addAll(buildChildNodesWithStudyAgents(posAnsWithStudyAgentsList));
      } else {
        ((FragmentVO) vo).getChildNodes().addAll(buildChildNodesWithStudyAgents(posAnsWithStudyAgentsList));
      }
      listAllQId(list, vo);
      list = list.stream().distinct().collect(Collectors.toList());
    }
    return list;
  }

  private void printAllQIdNumbers(NodeVO vo) {
    System.out.println(vo.getIdNode() + "-" + vo.getNumber());
    if (vo instanceof QuestionVO) {
      QuestionVO qvo = (QuestionVO) vo;
      if (qvo.getChildNodes() != null && !qvo.getChildNodes().isEmpty()) {
        for (PossibleAnswerVO avo : qvo.getChildNodes()) {
          printAllQIdNumbers(avo);
        }
      }
    } else if (vo instanceof PossibleAnswerVO) {
      PossibleAnswerVO avo = (PossibleAnswerVO) vo;
      if (avo.getChildNodes() != null && !avo.getChildNodes().isEmpty()) {
        for (QuestionVO qvo : avo.getChildNodes()) {
          printAllQIdNumbers(qvo);
        }
      }
    } else if (vo instanceof ModuleVO) {
      ModuleVO mvo = (ModuleVO) vo;
      if (mvo.getChildNodes() != null && !mvo.getChildNodes().isEmpty()) {
        for (QuestionVO qvo : mvo.getChildNodes()) {
          printAllQIdNumbers(qvo);
        }
      }
    } else if (vo instanceof FragmentVO) {
      FragmentVO fvo = (FragmentVO) vo;
      if (fvo.getChildNodes() != null && !fvo.getChildNodes().isEmpty()) {
        for (QuestionVO qvo : fvo.getChildNodes()) {
          printAllQIdNumbers(qvo);
        }
      }
    }

  }

  @Override
  public void listAllQId(List<String> listOfIdNodes, NodeVO vo) {
    listOfIdNodes.add(String.valueOf(vo.getIdNode()));
    if (vo instanceof QuestionVO) {
      QuestionVO qvo = (QuestionVO) vo;
      if (qvo.getChildNodes() != null && !qvo.getChildNodes().isEmpty()) {
        for (PossibleAnswerVO avo : qvo.getChildNodes()) {
          listAllQId(listOfIdNodes, avo);
        }
      }
    } else if (vo instanceof PossibleAnswerVO) {
      PossibleAnswerVO avo = (PossibleAnswerVO) vo;
      if (avo.getChildNodes() != null && !avo.getChildNodes().isEmpty()) {
        for (QuestionVO qvo : avo.getChildNodes()) {
          listAllQId(listOfIdNodes, qvo);
        }
      }
    } else if (vo instanceof ModuleVO) {
      ModuleVO mvo = (ModuleVO) vo;
      if (mvo.getChildNodes() != null && !mvo.getChildNodes().isEmpty()) {
        for (QuestionVO qvo : mvo.getChildNodes()) {
          listAllQId(listOfIdNodes, qvo);
        }
      }
    } else if (vo instanceof FragmentVO) {
      FragmentVO fvo = (FragmentVO) vo;
      if (fvo.getChildNodes() != null && !fvo.getChildNodes().isEmpty()) {
        for (QuestionVO qvo : fvo.getChildNodes()) {
          listAllQId(listOfIdNodes, qvo);
        }
      }
    }

  }


  private void getStudyAgentsForLinks(List<NodeVO> nodeWithStudyAgentsList,
                                      List<PossibleAnswerVO> posAnsWithStudyAgentsList, NodeVO nodeVo) {
    List<String> parentIdList = new ArrayList<>();
    for (NodeVO node : nodeWithStudyAgentsList) {
      if (parentIdList.contains(node.getParentId())) {
        continue;
      }
//			System.out.println("parent id "+node.getParentId());
      Node n = moduleDao.getNodeById(Long.valueOf(node.getParentId()));
      if (n instanceof PossibleAnswer) {
        PossibleAnswerVO vo = posAnsMapper.convertToPossibleAnswerVO((PossibleAnswer) n, false);
        vo.getChildNodes().add((QuestionVO) node);
        posAnsWithStudyAgentsList.add(vo);
      } else if (n instanceof JobModule) {
        QuestionVO qVo = (QuestionVO) node;
        if (nodeVo instanceof ModuleVO) {
          ModuleVO modVo = (ModuleVO) nodeVo;
          if (!modVo.getChildNodes().contains(qVo)) {
            modVo.getChildNodes().add(qVo);
          }
        }
        if (nodeVo instanceof FragmentVO) {
          FragmentVO fragVo = (FragmentVO) nodeVo;
          if (!fragVo.getChildNodes().contains(qVo)) {
            fragVo.getChildNodes().add(qVo);
          }
        }

      } else {
        System.out.println("something else");
      }
    }
  }

  @Override
  public ModuleVO filterModulesNodesWithAgents(ModuleVO vo, long idAgent) {
    vo.getChildNodes().clear();
    possAnswerCheckList.clear();
    qIdCheckList.clear();
    List<PossibleAnswerVO> posAnsWithStudyAgentsList = getAnswersWithAgents(vo, idAgent);
    // check child links if we have study agents
    boolean shouldReturnNull = addAnsDependencyFromModuleFragment(vo, posAnsWithStudyAgentsList);
    if (posAnsWithStudyAgentsList.isEmpty() && shouldReturnNull) {
      return null;
    } else {
      vo.getChildNodes().addAll(buildChildNodesWithStudyAgents(posAnsWithStudyAgentsList));
      vo.getChildNodes().removeAll(Collections.singleton(null));
    }
    return vo;
  }

  private boolean addAnsDependencyFromLinkAjsmNew(NodeVO vo, List<NodeVO> nodeWithStudyAgentsList) {
    boolean shouldReturnNull = true;
    List<Question> qList = moduleDao.getAllLinkingQuestionByModId(vo.getIdNode());
    List<QuestionVO> qListVO = questionMapper.convertToQuestionVOList(qList);
    if (qListVO == null) {
      return true;
    }
    qListVO.removeAll(Collections.singleton(null));
    for (QuestionVO qVO : qListVO) {
      nodeWithStudyAgentsList.add(qVO);
    }
    return shouldReturnNull;
  }

  private boolean addAnsDependencyFromModuleFragment(NodeVO vo, List<PossibleAnswerVO> posAnsWithStudyAgentsList) {
    boolean shouldReturnNull = true;
    List<ModuleFragmentVO> moduleFragments = moduleFragmentService.getModuleFragmentByModuleId(vo.getIdNode());
    for (ModuleFragmentVO modFragVO : moduleFragments) {
      List<PossibleAnswerVO> fragmentStudyAgentsList = posAnsMapper.convertToPossibleAnswerVOExModRuleList
        (dao.getPosAnsWithStudyAgentsByIdMod(modFragVO.getFragmentId()));
      if (!fragmentStudyAgentsList.isEmpty()) {
        //link ajsm has answers with study agents
        // will need to  get the parent answer for the link ajsm
        List<? extends Node> nodeLink = moduleDao.getNodeByLinkAndModId(modFragVO.getFragmentId(), vo.getIdNode());
        if (!nodeLink.isEmpty() && "P".equals(nodeLink.get(0).getNodeclass())) {
          List<PossibleAnswerVO> listPosAnsFromFragment =
            posAnsMapper.convertToPossibleAnswerVOExModRuleList(
              (List<PossibleAnswer>) nodeLink);
          for (PossibleAnswerVO ansVO : listPosAnsFromFragment) {
            if (!posAnsWithStudyAgentsList.contains(ansVO)) {
              posAnsWithStudyAgentsList.add(ansVO);
            }
            addLinkingQuestionAsChild(vo, posAnsWithStudyAgentsList, modFragVO.getFragmentId(), ansVO);
          }
        } else if (!nodeLink.isEmpty() && "M".equals(nodeLink.get(0).getNodeclass())) {
          //parent is a module
          addLinkingQuestionAsChildForModule(vo, modFragVO);
          shouldReturnNull = false;
        }
      }
    }
    return shouldReturnNull;
  }

  private void addLinkingQuestionAsChild(NodeVO vo, List<PossibleAnswerVO> posAnsWithStudyAgentsList, Long moduleId,
                                         PossibleAnswerVO ansVO) {
    QuestionVO qVO = questionMapper.convertToQuestionVO(
      moduleDao.getLinkingQuestionByModId(moduleId,
        vo.getIdNode()));
    if (qVO != null) {
      posAnsWithStudyAgentsList.get(posAnsWithStudyAgentsList.indexOf(ansVO))
        .getChildNodes().add(qVO);
    }
  }

  private void addLinkingQuestionAsChildForModule(NodeVO vo, ModuleFragmentVO modFragVO) {
    QuestionVO qVO = questionMapper.convertToQuestionVO(
      moduleDao.getLinkingQuestionByModId(modFragVO.getFragmentId(),
        vo.getIdNode()));
    if (qVO != null) {
      ModuleVO modVo = (ModuleVO) vo;
      modVo.getChildNodes().add(qVO);
    }
  }

  private List<QuestionVO> buildChildNodesWithStudyAgents(List<PossibleAnswerVO> posAnsWithStudyAgentsList) {
    List<QuestionVO> nodeVOList = new ArrayList<>();
    List<String> parentIdList = new ArrayList<>();
    int index = 0;
    for (PossibleAnswerVO ans : posAnsWithStudyAgentsList) {
      index++;
      if (parentIdList.contains(ans.getParentId())) {
        continue;
      }
      // get parent until module is reached
      Node node = moduleDao.getNodeById(Long.valueOf(ans.getParentId()));
      parentIdList.add(ans.getParentId());
      if ("Q".equals(node.getNodeclass())) {
        //parent is a question
        QuestionVO questionVO = questionMapper.convertToQuestionWithModRulesReduced((Question) node);

        if (!ans.getChildNodes().isEmpty()) {
          // got a linking question
          try {
            PossibleAnswerVO posAns = questionVO.getChildNodes().get(questionVO.getChildNodes().indexOf(ans));
            if (posAns != null) {
              posAns.getChildNodes().addAll(ans.getChildNodes());
            }
          } catch (Throwable ex) {
            ex.printStackTrace();
          }
        }
        QuestionVO questionUntilRootModule = getQuestionUntilRootModule(questionVO.getParentId(), questionVO);
        if (nodeVOList.contains(questionUntilRootModule)) {
          QuestionVO qVO = nodeVOList.get(nodeVOList.indexOf(questionUntilRootModule));
          try {
            mergeDifferences(questionUntilRootModule, qVO, false);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
        } else {
          nodeVOList.add(questionUntilRootModule);
        }
      } else if ("F".equals(node.getNodeclass())) {
        //parent is a link
        System.out.println("inside F");
      }
    }
    return nodeVOList;
  }

  private List<String> buildStringNodesWithStudyAgents(List<PossibleAnswerVO> posAnsWithStudyAgentsList) {
    List<String> results = new ArrayList<>();
    List<QuestionVO> nodeVOList = new ArrayList<>();
    List<String> parentIdList = new ArrayList<>();
    int index = 0;
    for (PossibleAnswerVO ans : posAnsWithStudyAgentsList) {
      index++;
      if (parentIdList.contains(ans.getParentId())) {
        continue;
      }
      // get parent until module is reached
      Node node = moduleDao.getNodeById(Long.valueOf(ans.getParentId()));
      parentIdList.add(ans.getParentId());
      if ("Q".equals(node.getNodeclass())) {
        //parent is a question
        QuestionVO questionVO = questionMapper.convertToQuestionWithModRulesReduced((Question) node);

        if (!ans.getChildNodes().isEmpty()) {
          // got a linking question
          try {
            PossibleAnswerVO posAns = questionVO.getChildNodes().get(questionVO.getChildNodes().indexOf(ans));
            if (posAns != null) {
              posAns.getChildNodes().addAll(ans.getChildNodes());
            }
          } catch (Throwable ex) {
            ex.printStackTrace();
          }
        }
        QuestionVO questionUntilRootModule = getQuestionUntilRootModule(questionVO.getParentId(), questionVO);
        if (nodeVOList.contains(questionUntilRootModule)) {
          QuestionVO qVO = nodeVOList.get(nodeVOList.indexOf(questionUntilRootModule));
          try {
            mergeDifferences(questionUntilRootModule, qVO, false);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
        } else {
          results.add(String.valueOf(questionUntilRootModule.getIdNode()));
        }
      } else if ("F".equals(node.getNodeclass())) {
        //parent is a link
        System.out.println("inside F");
      }
    }
    return results;
  }


  private void mergeDifferences(NodeVO source, NodeVO target, boolean mergeComplete)
    throws IllegalAccessException, InvocationTargetException {
//		if(mergeComplete){
//			return;
//		}
    if (source instanceof QuestionVO) {
      QuestionVO qvoSource = (QuestionVO) source;
      QuestionVO qvoTarget = (QuestionVO) target;
      for (PossibleAnswerVO avo : qvoSource.getChildNodes()) {
        if (!qvoTarget.getChildNodes().contains(avo)) {
          qvoTarget.getChildNodes().add(avo);
          mergeComplete = true;
          //System.out.println("Merge complete.");
//					return; 
        } else {
          mergeComplete = false;
          mergeDifferences(avo, qvoTarget.getChildNodes().get(qvoTarget.getChildNodes().indexOf(avo)), mergeComplete);
        }
      }
    } else if (source instanceof PossibleAnswerVO) {
      PossibleAnswerVO ansSource = (PossibleAnswerVO) source;
      PossibleAnswerVO ansTarget = (PossibleAnswerVO) target;
      for (QuestionVO qvo : ansSource.getChildNodes()) {
        if (!ansTarget.getChildNodes().contains(qvo)) {
          ansTarget.getChildNodes().add(qvo);
          mergeComplete = true;
          //		System.out.println("Merge complete.");
//					return; 
        } else {
          mergeComplete = false;
          mergeDifferences(qvo, ansTarget.getChildNodes().get(ansTarget.getChildNodes().indexOf(qvo)), mergeComplete);
        }
      }
    }
  }

  public List<QuestionVO> getChildFrequencyNodes(String idNode, PossibleAnswerVO answerVO) {
    //System.out.println(answerVO.getName());
    List<Question> childQuestions = moduleDao.getChildFrequencyNodes(String.valueOf(answerVO.getIdNode()));
    if (childQuestions.isEmpty()) {
      return null;
    }
    List<QuestionVO> childFrequencyNodes = questionMapper.
      convertToQuestionVOReducedDetailsList(childQuestions);
    if (!childFrequencyNodes.isEmpty()) {
      for (QuestionVO qVO : childFrequencyNodes) {
        //System.out.println(qVO.getName());
        for (PossibleAnswerVO ansVO : qVO.getChildNodes()) {
          //System.out.println(ansVO.getName());
          List<QuestionVO> childFrequencyNodes2 = getChildFrequencyNodes(String.valueOf(ansVO.getIdNode()), ansVO);
          ansVO.setChildNodes(childFrequencyNodes2);
        }
      }
    }
    return childFrequencyNodes;
  }

  public List<QuestionVO> getChildLinkNodes(String idNode, PossibleAnswerVO answerVO) {
    //System.out.println(answerVO.getName());
    List<Question> childQuestions = moduleDao.getChildLinkNodes(String.valueOf(answerVO.getIdNode()));
    if (childQuestions.isEmpty()) {
      return null;
    }
    List<QuestionVO> childFrequencyNodes = questionMapper.
      convertToQuestionVOReducedDetailsList(childQuestions);
    if (!childFrequencyNodes.isEmpty()) {
      for (QuestionVO qVO : childFrequencyNodes) {
        //System.out.println(qVO.getName());
        for (PossibleAnswerVO ansVO : qVO.getChildNodes()) {
          //System.out.println(ansVO.getName());
          List<QuestionVO> childFrequencyNodes2 = getChildLinkNodes(String.valueOf(ansVO.getIdNode()), ansVO);
          ansVO.setChildNodes(childFrequencyNodes2);
        }
      }
    }
    return childFrequencyNodes;
  }

  private QuestionVO getQuestionUntilRootModule(String parentId, NodeVO nodeVO) {

    if ("Q".equals(nodeVO.getNodeclass())) {
      QuestionVO questionVO1 = (QuestionVO) nodeVO;

      for (PossibleAnswerVO answer : questionVO1.getChildNodes()) {
        // get all frequency question under study filtered answer
        List<QuestionVO> childFrequencyNodes = getChildFrequencyNodes(String.valueOf(answer.getIdNode()), answer);
        if (childFrequencyNodes != null && !childFrequencyNodes.isEmpty()) {
          PossibleAnswerVO posAns = questionVO1.getChildNodes().get(questionVO1.getChildNodes().indexOf(answer));
          if (posAns != null) {
            posAns.getChildNodes().addAll(childFrequencyNodes);
          }
        }

        List<QuestionVO> childlinkNodes = getChildLinkNodes(String.valueOf(answer.getIdNode()), answer);
        if (childlinkNodes != null && !childlinkNodes.isEmpty()) {
          PossibleAnswerVO posAns = questionVO1.getChildNodes().get(questionVO1.getChildNodes().indexOf(answer));
          if (posAns != null) {
            //				posAns.getChildNodes().addAll(childlinkNodes);
          }
        }
      }
    }


    Node node = moduleDao.getNodeById(Long.valueOf(parentId));
    if ("P".equals(node.getNodeclass())) {
      //parent is a answer
      PossibleAnswerVO possibleAnswerVO =
        posAnsMapper.convertToPossibleAnswerVOExcQuestionAnsChild((PossibleAnswer) node);

      for (int i = 0; i < possibleAnswerVO.getChildNodes().size(); i++) {
//				if(possibleAnswerVO.getIdNode() ==43781L ){
//					System.out.println("debug");
//				}
//				if(!possAnswerCheckList.contains(possibleAnswerVO.getIdNode())){
//					possAnswerCheckList.add(possibleAnswerVO.getIdNode());
        possibleAnswerVO.getChildNodes().clear();
//				}
//				if(!possibleAnswerVO.getChildNodes().contains(nodeVO)){
        possibleAnswerVO.getChildNodes().add((QuestionVO) nodeVO);
//				}
//				QuestionVO qVO = possibleAnswerVO.getChildNodes().get(i);
//				if(qVO.getIdNode() == nodeVO.getIdNode()){
//					possibleAnswerVO.getChildNodes().set(i,(QuestionVO)nodeVO);
//				}
      }
      return getQuestionUntilRootModule(possibleAnswerVO.getParentId(), possibleAnswerVO);
    } else if ("Q".equals(node.getNodeclass())) {
      //parent is a question
      QuestionVO questionVO = questionMapper.convertToQuestionVOReducedDetails((Question) node);
      //if(questionVO.getIdNode() ==43562 ){
      //	System.out.println("debug");
      //}
      PossibleAnswerVO pavo = (PossibleAnswerVO) nodeVO;
      for (int i = 0; i < questionVO.getChildNodes().size(); i++) {
        PossibleAnswerVO aVO = questionVO.getChildNodes().get(i);
        if (aVO.getIdNode() == nodeVO.getIdNode()) {
          qIdCheckList.add(pavo.getIdNode());
          questionVO.getChildNodes().set(i, pavo);
        }
      }
      return getQuestionUntilRootModule(questionVO.getParentId(), questionVO);
    } else if ("M".equals(node.getNodeclass())) {
      return (QuestionVO) nodeVO;
    } else if ("F".equals(node.getNodeclass())) {
      return (QuestionVO) nodeVO;
    }
    return null;
  }


  private List<PossibleAnswerVO> getAnswersWithStudyAgents(NodeVO vo) {
    if (vo == null) {
      return new ArrayList<>();
    }

    List<PossibleAnswer> posAnsWithStudyAgentsList = dao.getPosAnsWithStudyAgentsByIdMod(vo.getIdNode());
    List<PossibleAnswerVO> newPosAnsWithStudyAgentsList = posAnsMapper.convertToPossibleAnswerVOExModRuleList(posAnsWithStudyAgentsList);
    return newPosAnsWithStudyAgentsList;
  }


  private List<PossibleAnswerVO> getAnswersWithAgents(NodeVO vo, long idAgent) {
    List<PossibleAnswer> posAnsWithStudyAgentsList =
      dao.getPosAnsWithAgentAndIdMod(vo.getIdNode(), idAgent);
    List<PossibleAnswerVO> newPosAnsWithStudyAgentsList = posAnsMapper.convertToPossibleAnswerVOExModRuleList(posAnsWithStudyAgentsList);
    return newPosAnsWithStudyAgentsList;
  }


  @Override
  public FragmentVO filterFragmentNodesWithStudyAgents(FragmentVO vo) {
    vo.getChildNodes().clear();
    possAnswerCheckList.clear();
    qIdCheckList.clear();
    List<PossibleAnswerVO> posAnsWithStudyAgentsList = getAnswersWithStudyAgents(vo);
    List<NodeVO> nodeWithStudyAgentsList = new ArrayList<>();
    addAnsDependencyFromLinkAjsmNew(vo, nodeWithStudyAgentsList);
    getStudyAgentsForLinks(nodeWithStudyAgentsList, posAnsWithStudyAgentsList, vo);
    for (PossibleAnswerVO avo : posAnsWithStudyAgentsList) {
      System.out.println(avo.getIdNode() + "-" + avo.getNumber());
    }
    if (posAnsWithStudyAgentsList.isEmpty() && nodeWithStudyAgentsList.isEmpty()) {
      return null;
    } else {
      vo.setChildNodes(buildChildNodesWithStudyAgents(posAnsWithStudyAgentsList));
      vo.getChildNodes().removeAll(Collections.singleton(null));
    }
    return vo;
  }

  @Override
  public FragmentVO filterFragmentNodesWithAgents(FragmentVO vo, Long idAgent) {
    possAnswerCheckList.clear();
    List<PossibleAnswerVO> posAnsWithStudyAgentsList = getAnswersWithAgents(vo, idAgent);
    // check child links if we have study agents
    boolean shouldReturnNull = addAnsDependencyFromModuleFragment(vo, posAnsWithStudyAgentsList);
    if (posAnsWithStudyAgentsList.isEmpty() && shouldReturnNull) {
      return null;
    } else {
      vo.setChildNodes(buildChildNodesWithStudyAgents(posAnsWithStudyAgentsList));
      vo.getChildNodes().removeAll(Collections.singleton(null));
    }
    return vo;
  }

  @Override
  public void populateNodeidList(Long nodeId) {
    Node node = moduleDao.getNodeById(Long.valueOf(nodeId));
    if ("P".equals(node.getNodeclass())) {
      //parent is a answer
      PossibleAnswerVO possibleAnswerVO = posAnsMapper.convertToPossibleAnswerVOExcQuestionAnsChild((PossibleAnswer) node);
      this.nodeIds.add(Long.valueOf(possibleAnswerVO.getParentId()));
      populateNodeidList(Long.valueOf(possibleAnswerVO.getParentId()));
    } else if ("Q".equals(node.getNodeclass())) {
      //parent is a question
      QuestionVO questionVO = questionMapper.convertToQuestionVOReducedDetails((Question) node);
      this.nodeIds.add(Long.valueOf(questionVO.getParentId()));
      populateNodeidList(Long.valueOf(questionVO.getParentId()));

    } else if ("M".equals(node.getNodeclass())) {
      //	System.out.println("End of module");
    } else if ("F".equals(node.getNodeclass())) {
      System.out.println("End of fragment");
    }
  }

  @Override
  public void testNodeidList(Long nodeId) {

    try {
      boolean isError = false;
      ModuleVO vo = studyAgentUtil.getStudyAgentJson(String.valueOf(nodeId));
      for (Long id : this.nodeIds) {
        NodeVO node = new StudyAgentUtil().searchNode(vo, id);
        if (node == null) {
          //System.out.println("ERROR: idNode - "+id);
          isError = true;
        } else {
          //System.out.println("Found "+id);
        }
      }
      if (isError) {
        System.out.println("ERROR: idNode - " + nodeId);
      }
      this.nodeIds = new ArrayList<Long>();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  }

  @Override
  public FragmentVO getFragmentNodesWithAgents(FragmentVO vo, Long idAgent) {
    List<PossibleAnswerVO> posAnsWithStudyAgentsList = getAnswersWithAgents(vo, idAgent);
    vo.setChildNodes(buildChildNodesWithStudyAgents(posAnsWithStudyAgentsList));
    if (vo.getChildNodes().isEmpty()) {
      return null;
    }
    return vo;
  }


}
