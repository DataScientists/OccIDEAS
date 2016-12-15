package org.occideas.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
public class ModuleRule  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String primaryKey;
	
	private long idModule;
	private String moduleName;
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idRule",referencedColumnName="idRule",insertable=false,updatable=false)
	private Rule rule;
	private String idRule;
	private long idAgent;
	private String agentName;
	private String ruleLevel;
	
	private long idNode;
	private String nodeNumber;	

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

	public ModuleRule() {
		super();
	}
	
	public ModuleRule(String primaryKey) {
		super();
		this.setPrimaryKey(primaryKey);
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public long getIdModule() {
		return idModule;
	}

	public void setIdModule(long idModule) {
		this.idModule = idModule;
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

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public String getIdRule() {
		return idRule;
	}

	public void setIdRule(String idRule) {
		this.idRule = idRule;
	}

	public String getRuleLevel() {
		return ruleLevel;
	}

	public void setRuleLevel(String ruleLevel) {
		this.ruleLevel = ruleLevel;
	}
	
	
	
}
