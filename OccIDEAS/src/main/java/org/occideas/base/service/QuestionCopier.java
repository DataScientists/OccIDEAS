package org.occideas.base.service;

import java.util.List;

import org.occideas.vo.BaseReportVO;
import org.occideas.vo.ModuleRuleVO;
import org.occideas.vo.NodeRuleHolder;
import org.occideas.vo.NodeRuleVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.RuleVO;
import org.springframework.stereotype.Component;

@Component
public class QuestionCopier implements IQuestionCopier{

	public void populateQuestionsWithIdNode(Long idNode, List<QuestionVO> childNodes,
			NodeRuleHolder idNodeRuleHolder) {
		idNodeRuleHolder.setLastIdNode(idNode);
		for (QuestionVO vo : childNodes) {
			vo.setParentId(String.valueOf(idNode));
			vo.setTopNodeId(idNodeRuleHolder.getTopNodeId());
			Long qsIdNode = idNodeRuleHolder.getLastIdNode() + 1;
			idNodeRuleHolder.setLastIdNode(qsIdNode);
			vo.setIdNode(qsIdNode);
			if (!vo.getChildNodes().isEmpty()) {
				populateAnswerWithIdNode(qsIdNode, vo.getChildNodes(), idNodeRuleHolder);
			}
			if (qsIdNode > idNodeRuleHolder.getLastIdNode()) {
				idNodeRuleHolder.setLastIdNode(qsIdNode);
			}
		}
	}
	
	public void populateQuestionsWithIdNode(Long idNode, List<QuestionVO> childNodes,
			NodeRuleHolder idNodeRuleHolder,BaseReportVO report) {
		idNodeRuleHolder.setLastIdNode(idNode);
		for (QuestionVO vo : childNodes) {
			vo.setParentId(String.valueOf(idNode));
			vo.setTopNodeId(idNodeRuleHolder.getTopNodeId());
			Long qsIdNode = idNodeRuleHolder.getLastIdNode() + 1;
			idNodeRuleHolder.setLastIdNode(qsIdNode);
			vo.setIdNode(qsIdNode);
			report.setTotalQuestions(report.getTotalQuestions() + 1);
			if (!vo.getChildNodes().isEmpty()) {
				populateAnswerWithIdNode(qsIdNode, vo.getChildNodes(), idNodeRuleHolder,report);
			}
			if (qsIdNode > idNodeRuleHolder.getLastIdNode()) {
				idNodeRuleHolder.setLastIdNode(qsIdNode);
			}
		}
	}

	private void populateAnswerWithIdNode(Long qsIdNode, List<PossibleAnswerVO> childNodes,
			NodeRuleHolder idNodeRuleHolder) {
		int count = 1;
		for (PossibleAnswerVO vo : childNodes) {
			vo.setParentId(String.valueOf(qsIdNode));
			vo.setTopNodeId(idNodeRuleHolder.getTopNodeId());
			Long asIdNode = idNodeRuleHolder.getLastIdNode() + count;
			vo.setIdNode(asIdNode);
			idNodeRuleHolder.setLastIdNode(asIdNode);
			if (!vo.getModuleRule().isEmpty()) {
				populateRulesWithIdRule(vo, vo.getModuleRule(), idNodeRuleHolder);
			}
			if (!vo.getChildNodes().isEmpty()) {
				populateQuestionsWithIdNode(asIdNode, vo.getChildNodes(), idNodeRuleHolder);
			}
			count++;
		}
	}
	
	private void populateAnswerWithIdNode(Long qsIdNode, List<PossibleAnswerVO> childNodes,
			NodeRuleHolder idNodeRuleHolder,BaseReportVO report) {
		int count = 1;
		for (PossibleAnswerVO vo : childNodes) {
			vo.setParentId(String.valueOf(qsIdNode));
			vo.setTopNodeId(idNodeRuleHolder.getTopNodeId());
			Long asIdNode = idNodeRuleHolder.getLastIdNode() + count;
			vo.setIdNode(asIdNode);
			report.setTotalAnswers(report.getTotalAnswers()+1);
			idNodeRuleHolder.setLastIdNode(asIdNode);
			if (!vo.getModuleRule().isEmpty()) {
				populateRulesWithIdRule(vo, vo.getModuleRule(), idNodeRuleHolder,report);
			}
			if (!vo.getChildNodes().isEmpty()) {
				populateQuestionsWithIdNode(asIdNode, vo.getChildNodes(), idNodeRuleHolder,report);
			}
			count++;
		}
	}
	
	private void populateRulesWithIdRule(PossibleAnswerVO possibleAnswer, List<ModuleRuleVO> moduleRule,
			NodeRuleHolder idNodeRuleHolder,BaseReportVO report) {
		boolean ruleExist = false;
		for (ModuleRuleVO ruleVo : moduleRule) {
			if(idNodeRuleHolder.getRuleIdStorage().
					containsKey(ruleVo.getRule().getIdRule())){
				ruleVo.getRule().setIdRule(idNodeRuleHolder.
						getRuleIdStorage().get(ruleVo.getRule().getIdRule()));
				ruleExist = true;
			}else if (ruleVo.getRule().getIdRule() <= idNodeRuleHolder.getFirstIdRuleGenerated()) {
				idNodeRuleHolder.setLastIdRule(idNodeRuleHolder.getLastIdRule() + 1);
				idNodeRuleHolder.getRuleIdStorage().put(ruleVo.getRule().getIdRule(), 
						idNodeRuleHolder.getLastIdRule());
				ruleVo.getRule().setIdRule(idNodeRuleHolder.getLastIdRule());
				report.setTotalRules(report.getTotalRules()+1);
				ruleExist = false;
			}
			populateRuleCondition(possibleAnswer, ruleVo.getRule().getConditions(), ruleVo.getRule(), 
					idNodeRuleHolder,ruleExist);
		}
	}

	private void populateRulesWithIdRule(PossibleAnswerVO possibleAnswer, List<ModuleRuleVO> moduleRule,
			NodeRuleHolder idNodeRuleHolder) {
		boolean ruleExist = false;
		for (ModuleRuleVO ruleVo : moduleRule) {
			if(idNodeRuleHolder.getRuleIdStorage().
					containsKey(ruleVo.getRule().getIdRule())){
				ruleVo.getRule().setIdRule(idNodeRuleHolder.
						getRuleIdStorage().get(ruleVo.getRule().getIdRule()));
				ruleExist = true;
			}else if (ruleVo.getRule().getIdRule() <= idNodeRuleHolder.getFirstIdRuleGenerated()) {
				idNodeRuleHolder.setLastIdRule(idNodeRuleHolder.getLastIdRule() + 1);
				idNodeRuleHolder.getRuleIdStorage().put(ruleVo.getRule().getIdRule(), 
						idNodeRuleHolder.getLastIdRule());
				ruleVo.getRule().setIdRule(idNodeRuleHolder.getLastIdRule());
				ruleExist = false;
			}
			populateRuleCondition(possibleAnswer, ruleVo.getRule().getConditions(), ruleVo.getRule(), 
					idNodeRuleHolder,ruleExist);
		}
	}

	private void populateRuleCondition(PossibleAnswerVO possibleAnswer, List<PossibleAnswerVO> conditions, RuleVO ruleVO,
			NodeRuleHolder idNodeRuleHolder,boolean ruleExist) {
		boolean readyToSaveRule = true;
		boolean hasIdNodeBeenSet = false;
		for (PossibleAnswerVO condition : conditions) {
			if (condition.getNumber().equalsIgnoreCase(possibleAnswer.getNumber()) && condition.getName().equalsIgnoreCase(possibleAnswer.getName())) {
				// here we know that the condition rule in rulevo 
				// pertains to the possible answer ->vo
				NodeRuleVO nodeRule = new NodeRuleVO();
				nodeRule.setIdNode(possibleAnswer.getIdNode());
				nodeRule.setIdRule(ruleVO.getIdRule());
				idNodeRuleHolder.getNodeRuleList().add(nodeRule);
				hasIdNodeBeenSet = true;
			}
			if (idNodeRuleHolder.getFirstIdRuleGenerated() > ruleVO.getIdRule()) {
				readyToSaveRule = false;
				if (hasIdNodeBeenSet) {
					break;
				}
			}
		}
		if (readyToSaveRule && !ruleExist) {
			ruleVO.getConditions().clear();
			idNodeRuleHolder.getRuleList().add(ruleVO);
		}
	}
	
}
