package org.occideas.vo;

import java.math.BigInteger;

public class ModuleRuleVO {

	private BigInteger idModule;
	private BigInteger idRule;
	private BigInteger idAgent;
	private BigInteger idNode;
	
	
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
