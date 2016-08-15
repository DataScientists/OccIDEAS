package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

public class ReportVO {

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

}
