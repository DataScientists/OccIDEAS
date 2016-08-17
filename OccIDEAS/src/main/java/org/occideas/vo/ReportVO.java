package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

public class ReportVO {

	private ModuleVO vo;
	private int totalQuestions;
	private int totalAnswers;
	private List<AgentVO> missingAgentsList = new ArrayList<>();
	private List<RuleVO> missingRuleAgentList = new ArrayList<>();

	public List<AgentVO> getMissingAgentsList() {
		return missingAgentsList;
	}

	public void setMissingAgentsList(List<AgentVO> missingAgentsList) {
		this.missingAgentsList = missingAgentsList;
	}

	public List<RuleVO> getMissingRuleAgentList() {
		return missingRuleAgentList;
	}

	public void setMissingRuleAgentList(List<RuleVO> missingRuleAgentList) {
		this.missingRuleAgentList = missingRuleAgentList;
	}

	public ModuleVO getVo() {
		return vo;
	}

	public void setVo(ModuleVO vo) {
		this.vo = vo;
	}

	public int getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	public int getTotalAnswers() {
		return totalAnswers;
	}

	public void setTotalAnswers(int totalAnswers) {
		this.totalAnswers = totalAnswers;
	}

}
