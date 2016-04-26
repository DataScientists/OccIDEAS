package org.occideas.entity;

import java.io.Serializable;

public class InterviewAnswerPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long idInterview;
	private long topQuestionId;
	private long parentQuestionId;
	private long answerId;

	public InterviewAnswerPK() {
	}

	public InterviewAnswerPK(long idInterview, long topQuestionId, long parentQuestionId, long answerId) {
		super();
		this.idInterview = idInterview;
		this.topQuestionId = topQuestionId;
		this.parentQuestionId = parentQuestionId;
		this.answerId = answerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (answerId ^ (answerId >>> 32));
		result = prime * result + (int) (idInterview ^ (idInterview >>> 32));
		result = prime * result + (int) (parentQuestionId ^ (parentQuestionId >>> 32));
		result = prime * result + (int) (topQuestionId ^ (topQuestionId >>> 32));
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
		InterviewAnswerPK other = (InterviewAnswerPK) obj;
		if (answerId != other.answerId)
			return false;
		if (idInterview != other.idInterview)
			return false;
		if (parentQuestionId != other.parentQuestionId)
			return false;
		if (topQuestionId != other.topQuestionId)
			return false;
		return true;
	}

}
