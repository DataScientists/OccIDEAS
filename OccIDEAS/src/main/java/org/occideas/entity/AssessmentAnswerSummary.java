package org.occideas.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class AssessmentAnswerSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String primaryKey;
	private long idParticipant;
	private String reference;
	private long idinterview;
	private long answerId;
	private String name;
	private String answerFreetext;
	private String type;
	private int status;
	private String assessedStatus;
	private transient String statusDescription;

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public long getIdParticipant() {
		return idParticipant;
	}

	public void setIdParticipant(long idParticipant) {
		this.idParticipant = idParticipant;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public long getIdinterview() {
		return idinterview;
	}

	public void setIdinterview(long idinterview) {
		this.idinterview = idinterview;
	}

	public long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(long answerId) {
		this.answerId = answerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnswerFreetext() {
		return answerFreetext;
	}

	public void setAnswerFreetext(String answerFreetext) {
		this.answerFreetext = answerFreetext;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusDescription() {

		if (status == 0) {
			statusDescription = "Running";
		} else if (status == 1) {
			statusDescription = "Partial";
		} else if (status == 2) {
			statusDescription = "Completed";
		}
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getAssessedStatus() {
		return assessedStatus;
	}

	public void setAssessedStatus(String assessedStatus) {
		this.assessedStatus = assessedStatus;
	}

}
