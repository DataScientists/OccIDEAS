package org.occideas.entity;

import java.io.Serializable;

public class InterviewQuestionPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long idInterview;
	private long questionId;
	private long parentId;
	private Integer modCount;
	private long parentAnswerId;

	public InterviewQuestionPK() {
	}

	public InterviewQuestionPK(long idInterview, long questionId, long parentId, Integer modCount,
			long parentAnswerId) {
		super();
		this.idInterview = idInterview;
		this.questionId = questionId;
		this.parentId = parentId;
		this.modCount = modCount;
		this.parentAnswerId = parentAnswerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idInterview ^ (idInterview >>> 32));
		result = prime * result + ((modCount == null) ? 0 : modCount.hashCode());
		result = prime * result + (int) (parentAnswerId ^ (parentAnswerId >>> 32));
		result = prime * result + (int) (parentId ^ (parentId >>> 32));
		result = prime * result + (int) (questionId ^ (questionId >>> 32));
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
		InterviewQuestionPK other = (InterviewQuestionPK) obj;
		if (idInterview != other.idInterview)
			return false;
		if (modCount == null) {
			if (other.modCount != null)
				return false;
		} else if (!modCount.equals(other.modCount))
			return false;
		if (parentAnswerId != other.parentAnswerId)
			return false;
		if (parentId != other.parentId)
			return false;
		if (questionId != other.questionId)
			return false;
		return true;
	}

}
