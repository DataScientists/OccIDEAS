package org.occideas.systemproperty.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.occideas.entity.SystemProperty;
import org.occideas.mapper.SystemPropertyMapper;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleRuleVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SystemPropertyServiceImpl_backup {

//	@Autowired
//	private SystemPropertyMapper mapper;
//	@Autowired
//	private SystemPropertyDao dao;
//
//	@Override
//	public SystemPropertyVO save(SystemPropertyVO sysProp) {
//		SystemProperty systemProperty = dao.save(mapper.convertSytemPropertyVOtoSystemProperty(sysProp));
//		return mapper.convertSytemPropertyToSystemPropertyVO(systemProperty);
//	}
//
//	@Override
//	public SystemPropertyVO getById(long id) {
//		SystemProperty sysProp = dao.getById(id);
//		return mapper.convertSytemPropertyToSystemPropertyVO(sysProp);
//	}
//
//	@Override
//	public List<SystemPropertyVO> getAll() {
//		List<SystemProperty> list = dao.getAll();
//		return mapper.convertSystemPropertyListToSystemPropertyVOList(list);
//	}
//
//	@Override
//	public void delete(SystemPropertyVO vo) {
//		dao.delete(mapper.convertSytemPropertyVOtoSystemProperty(vo));
//	}
//
//	@Override
//	public SystemPropertyVO getByName(String name) {
//		SystemProperty sysProp = dao.getByName(name);
//		return mapper.convertSytemPropertyToSystemPropertyVO(sysProp);
//	}
//
//	@Override
//	public List<SystemPropertyVO> getByType(String type) {
//		List<SystemProperty> list = dao.getByType(type);
//		return mapper.convertSystemPropertyListToSystemPropertyVOList(list);
//	}
//
//	@Override
//	public FragmentVO getFragmentNodesWithStudyAgents(FragmentVO vo) {
//		List<Long> listOfIdNodesWithStudyAgents = new ArrayList<>();
//		List<ModuleRuleVO> moduleRuleList = vo.getModuleRule();
//		for (ModuleRuleVO mod : moduleRuleList) {
//			if (dao.isStudyAgent(mod.getRule().getAgentId())) {
//				listOfIdNodesWithStudyAgents.add(mod.getIdNode());
//			}
//		}
//		return filterFragmentNodesWithStudyAgents(vo, listOfIdNodesWithStudyAgents);
//	}
//
//	private FragmentVO filterFragmentNodesWithStudyAgents(FragmentVO vo, List<Long> listOfIdNodesWithStudyAgents) {
//		List<QuestionVO> questionVOList = vo.getChildNodes();
//		List<QuestionVO> newQuestionVOList = new ArrayList<>();
//		filterQuestionNodesWithStudyAgents(questionVOList, listOfIdNodesWithStudyAgents, newQuestionVOList);
//		if (newQuestionVOList.isEmpty()) {
//			vo.setChildNodes(null);
//		} else {
//			vo.setChildNodes(newQuestionVOList);
//		}
//		return vo;
//	}
//
//	private List<QuestionVO> filterQuestionNodesWithStudyAgents(List<QuestionVO> questionVOList,
//			List<Long> listOfIdNodesWithStudyAgents, List<QuestionVO> newQuestionVOList) {
//		for (QuestionVO qVo : questionVOList) {
//			List<PossibleAnswerVO> newPossibleAnswerList = new ArrayList<>();
//			filterAnswerNodesWithStudyAgents(qVo.getChildNodes(), listOfIdNodesWithStudyAgents, newPossibleAnswerList);
//			if (!newPossibleAnswerList.isEmpty()) {
//				qVo.setChildNodes(newPossibleAnswerList);
//				newQuestionVOList.add(qVo);
//			}
//		}
//
//		return newQuestionVOList;
//	}
//
//	private void filterAnswerNodesWithStudyAgents(List<PossibleAnswerVO> childNodes,
//			List<Long> listOfIdNodesWithStudyAgents, List<PossibleAnswerVO> newPossibleAnswerList) {
//		for (PossibleAnswerVO aVo : childNodes) {
//			boolean isPushed = false;
//			if (listOfIdNodesWithStudyAgents.contains(aVo.getIdNode())) {
//				newPossibleAnswerList.add(aVo);
//				isPushed = true;
//				// if an answer is identified with rules which is a study agent , 
//				// we must add the other answers to this question
//			}
//			// additional check for child questions, verify if its answers has rules with study agent and
//			// add to the hierarchy accordingly
//			if (!isPushed && !aVo.getChildNodes().isEmpty()) {
//				List<QuestionVO> newQuestionVOList = new ArrayList<>();
//				filterQuestionNodesWithStudyAgents(aVo.getChildNodes(), listOfIdNodesWithStudyAgents,
//						newQuestionVOList);
//				if (!newQuestionVOList.isEmpty()) {
//					aVo.setChildNodes(newQuestionVOList);
//				} else {
//					aVo.setChildNodes(null);
//				}
//			}
//		}
//
//	}
//
//	@Override
//	public ModuleVO filterModulesNodesWithStudyAgents(ModuleVO vo) {
//		List<Long> listOfIdNodesWithStudyAgents = new ArrayList<>();
//		List<ModuleRuleVO> moduleRuleList = vo.getModuleRule();
//		List<ModuleRuleVO> newModuleRuleList = new ArrayList<>();
//		for (ModuleRuleVO mod : moduleRuleList) {
//			if (dao.isStudyAgent(mod.getRule().getAgentId())) {
//				listOfIdNodesWithStudyAgents.add(mod.getIdNode());
//				newModuleRuleList.add(mod);
//			}
//		}
//		vo.setModuleRule(newModuleRuleList);
//		return filterModuleNodesWithStudyAgents(vo, listOfIdNodesWithStudyAgents);
//	}
//	
//	private ModuleVO filterModuleNodesWithStudyAgents(ModuleVO vo, List<Long> listOfIdNodesWithStudyAgents) {
//		List<QuestionVO> questionVOList = vo.getChildNodes();
//		List<QuestionVO> newQuestionVOList = new ArrayList<>();
//		filterQuestionNodesWithStudyAgents(questionVOList, listOfIdNodesWithStudyAgents, newQuestionVOList);
//		if (newQuestionVOList.isEmpty()) {
//			vo.setChildNodes(null);
//		} else {
//			vo.setChildNodes(newQuestionVOList);
//		}
//		return vo;
//	}


}
