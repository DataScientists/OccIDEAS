package org.occideas.vo;

public class NodeRuleVO {

	private long idRule;
	private long idNode;

	public long getIdRule() {
		return idRule;
	}

	public void setIdRule(long idRule) {
		this.idRule = idRule;
	}

	public long getIdNode() {
		return idNode;
	}

	public void setIdNode(long idNode) {
		this.idNode = idNode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idNode ^ (idNode >>> 32));
		result = prime * result + (int) (idRule ^ (idRule >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeRuleVO other = (NodeRuleVO) obj;
		if (idNode != other.idNode)
			return false;
		if (idRule != other.idRule)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NodeRuleVO [idRule=" + idRule + ", idNode=" + idNode + "]";
	}

}
