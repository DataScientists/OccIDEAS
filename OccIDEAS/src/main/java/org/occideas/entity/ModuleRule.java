package org.occideas.entity;

import java.math.BigInteger;

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
	
	private BigInteger idModule;
	private BigInteger idRule;
	private BigInteger idAgent;
	private BigInteger idNode;
	
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
	
	public BigInteger getIdModule() {
		return idModule;
	}

	public void setIdModule(BigInteger idModule) {
		this.idModule = idModule;
	}

	public BigInteger getIdRule() {
		return idRule;
	}

	public void setIdRule(BigInteger idRule) {
		this.idRule = idRule;
	}

	public BigInteger getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(BigInteger idAgent) {
		this.idAgent = idAgent;
	}

	public BigInteger getIdNode() {
		return idNode;
	}

	public void setIdNode(BigInteger idNode) {
		this.idNode = idNode;
	}


}
