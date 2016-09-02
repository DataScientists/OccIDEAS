package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseReportVO {

	private int totalQuestions;
	private int totalAnswers;
	private int totalRules;
	private List<AgentVO> missingAgentsList = new ArrayList<>();
	private List<RuleVO> missingRuleAgentList = new ArrayList<>();

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

	public int getTotalRules() {
		return totalRules;
	}

	public void setTotalRules(int totalRules) {
		this.totalRules = totalRules;
	}

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

}
