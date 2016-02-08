package org.occideas.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ModuleRule  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String primaryKey;
	
	private long idModule;
	private long idRule;
	private long idAgent;
	private long idNode;
	
	transient private Module module;
	transient private Agent agent;
	transient private Rule rule;
	//private PossibleAnswer possibleAnswer;
	
	public ModuleRule() {
		super();
	}
	
	public ModuleRule(String primaryKey) {
		super();
		this.setPrimaryKey(primaryKey);
	}
	
	public Module getModule() {
		module = new Module(this.idModule);
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	
	public Agent getAgent() {
		agent = new Agent(this.idAgent);
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	public Rule getRule() {
		rule = new Rule(this.idRule);
		return rule;
	}
	public void setRule(Rule rule) {
		this.rule = rule;
	}
	/*public PossibleAnswer getNode() {
		possibleAnswer = new PossibleAnswer(this.idNode);
		return possibleAnswer;
	}
	public void setPossibleAnswer(PossibleAnswer possibleAnswer) {
		this.possibleAnswer = possibleAnswer;
	}*/
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Long getIdModule() {
		return idModule;
	}

	public void setIdModule(long idModule) {
		this.idModule = idModule;
	}

	public Long getIdRule() {
		return idRule;
	}

	public void setIdRule(long idRule) {
		this.idRule = idRule;
	}

	public Long getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(long idAgent) {
		this.idAgent = idAgent;
	}

	public Long getIdNode() {
		return idNode;
	}

	public void setIdNode(long idNode) {
		this.idNode = idNode;
	}
	
	

}
