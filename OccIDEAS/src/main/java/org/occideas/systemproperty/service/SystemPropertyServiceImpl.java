package org.occideas.systemproperty.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.occideas.entity.Module;
import org.occideas.entity.Node;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.entity.SystemProperty;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.PossibleAnswerMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.mapper.SystemPropertyMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
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
		if(vo.getChildNodes().isEmpty()){
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
		List<PossibleAnswerVO> posAnsWithStudyAgentsList = getAnswersWithStudyAgents(vo);
		vo.setChildNodes(buildChildNodesWithStudyAgents(posAnsWithStudyAgentsList));
		return vo;
	}

	private List<QuestionVO> buildChildNodesWithStudyAgents(List<PossibleAnswerVO> posAnsWithStudyAgentsList) {
		List<QuestionVO> nodeVOList = new ArrayList<>();
		for(PossibleAnswerVO ans:posAnsWithStudyAgentsList){
			// get parent until module is reached
			Node node = moduleDao.getNodeById(Long.valueOf(ans.getParentId()));
			if("Q".equals(node.getNodeclass())){
				//parent is a question
				QuestionVO questionVO = questionMapper.convertToQuestionVOReducedDetails((Question)node);
				nodeVOList.add(getQuestionUntilRootModule(questionVO.getParentId(),questionVO));
			}else if("F".equals(node.getNodeclass())){
				//parent is a link
			}
		}
		return nodeVOList;
	}

	private QuestionVO getQuestionUntilRootModule(String parentId, NodeVO nodeVO) {
		Node node = moduleDao.getNodeById(Long.valueOf(parentId));
		if("P".equals(node.getNodeclass())){
			//parent is a answer
			PossibleAnswerVO possibleAnswerVO = 
					posAnsMapper.convertToPossibleAnswerVO((PossibleAnswer)node, true);
			for(int i=0;i<possibleAnswerVO.getChildNodes().size();i++){
				QuestionVO qVO = possibleAnswerVO.getChildNodes().get(i);
				if(qVO.getIdNode() == nodeVO.getIdNode()){
					possibleAnswerVO.getChildNodes().set(i,(QuestionVO)nodeVO);
				}
			}
			return getQuestionUntilRootModule(possibleAnswerVO.getParentId(),possibleAnswerVO);
		}else if("Q".equals(node.getNodeclass())){
			//parent is a question
			QuestionVO questionVO = 
					questionMapper.convertToQuestionVOReducedDetails((Question)node);
			for(int i=0;i<questionVO.getChildNodes().size();i++){
				PossibleAnswerVO aVO = questionVO.getChildNodes().get(i);
				if(aVO.getIdNode() == nodeVO.getIdNode()){
					questionVO.getChildNodes().set(i,(PossibleAnswerVO)nodeVO);
				}
			}
			return getQuestionUntilRootModule(questionVO.getParentId(),questionVO);
		}else if("M".equals(node.getNodeclass())){
			return (QuestionVO)nodeVO;
		}
		return null;
	}

	private List<PossibleAnswerVO> getAnswersWithStudyAgents(NodeVO vo) {
		List<PossibleAnswer> posAnsWithStudyAgentsList = 
				dao.getPosAnsWithStudyAgentsByIdMod(vo.getIdNode());
		List<PossibleAnswerVO> newPosAnsWithStudyAgentsList = posAnsMapper.convertToPossibleAnswerVOExModRuleList(posAnsWithStudyAgentsList);
		return newPosAnsWithStudyAgentsList;
	}
	

}
