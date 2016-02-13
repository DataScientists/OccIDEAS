package org.occideas.vo;

public class ModuleRuleVO {

	private long idModule;
	private String moduleName;
	private long idRule;
	private long idAgent;
	private String agentName;
	private long idNode;
	private String nodeNumber;
	private RuleVO rule;

	public RuleVO getRule() {
		return rule;
	}

	public void setRule(RuleVO rule) {
		this.rule = rule;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(String nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	public long getIdModule() {
		return idModule;
	}

	public void setIdModule(long idModule) {
		this.idModule = idModule;
	}

	public long getIdRule() {
		return idRule;
	}

	public void setIdRule(long idRule) {
		this.idRule = idRule;
	}

	public long getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(long idAgent) {
		this.idAgent = idAgent;
	}

	public long getIdNode() {
		return idNode;
	}

	public void setIdNode(long idNode) {
		this.idNode = idNode;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

}
