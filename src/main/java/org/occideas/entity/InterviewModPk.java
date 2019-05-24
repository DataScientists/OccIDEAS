package org.occideas.entity;

import java.io.Serializable;

public class InterviewModPk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long idNode;
	private long idinterview;
	private Integer count;

	public InterviewModPk() {
	}

	public InterviewModPk(long idNode, long idinterview, Integer count) {
		super();
		this.idNode = idNode;
		this.idinterview = idinterview;
		this.count = count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + (int) (idNode ^ (idNode >>> 32));
		result = prime * result + (int) (idinterview ^ (idinterview >>> 32));
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
		InterviewModPk other = (InterviewModPk) obj;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (idNode != other.idNode)
			return false;
		if (idinterview != other.idinterview)
			return false;
		return true;
	}

}
