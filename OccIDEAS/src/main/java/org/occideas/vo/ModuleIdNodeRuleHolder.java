package org.occideas.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.occideas.entity.NodeRule;

public class ModuleIdNodeRuleHolder {

	private long idNode = 0L;
	private long lastIdNode = 0L;
	private long lastIdRule = 0L;
	private long firstIdRuleGenerated = 0L;
	private long topNodeId = 0L;
	private List<RuleVO> ruleList = new ArrayList<>();
	private List<NodeRule> nodeRuleList = new ArrayList<>();
	private Map<Long, Long> ruleIdStorage = new HashMap<>();

	public long getLastIdNode() {
		return lastIdNode;
	}

	public void setLastIdNode(long lastIdNode) {
		this.lastIdNode = lastIdNode;
	}

	public long getLastIdRule() {
		return lastIdRule;
	}

	public void setLastIdRule(long lastIdRule) {
		this.lastIdRule = lastIdRule;
	}

	public long getFirstIdRuleGenerated() {
		return firstIdRuleGenerated;
	}

	public void setFirstIdRuleGenerated(long firstIdRuleGenerated) {
		this.firstIdRuleGenerated = firstIdRuleGenerated;
	}

	public List<RuleVO> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<RuleVO> ruleList) {
		this.ruleList = ruleList;
	}

	public List<NodeRule> getNodeRuleList() {
		return nodeRuleList;
	}

	public void setNodeRuleList(List<NodeRule> nodeRuleList) {
		this.nodeRuleList = nodeRuleList;
	}

	public long getIdNode() {
		return idNode;
	}

	public void setIdNode(long idNode) {
		this.idNode = idNode;
	}

	public long getTopNodeId() {
		return topNodeId;
	}

	public void setTopNodeId(long topNodeId) {
		this.topNodeId = topNodeId;
	}

	public Map<Long, Long> getRuleIdStorage() {
		return ruleIdStorage;
	}

	public void setRuleIdStorage(Map<Long, Long> ruleIdStorage) {
		this.ruleIdStorage = ruleIdStorage;
	}

}