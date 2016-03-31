package org.occideas.vo;

import java.math.BigInteger;

public class InterviewLinkedVO {

	private BigInteger idInterview;
	private BigInteger parentQuestionId;
	private BigInteger linkedId;
	private String name;
	private String description;
	private Integer deleted;

	public BigInteger getIdInterview() {
		return idInterview;
	}

	public void setIdInterview(BigInteger idInterview) {
		this.idInterview = idInterview;
	}

	public BigInteger getParentQuestionId() {
		return parentQuestionId;
	}

	public void setParentQuestionId(BigInteger parentQuestionId) {
		this.parentQuestionId = parentQuestionId;
	}

	public BigInteger getLinkedId() {
		return linkedId;
	}

	public void setLinkedId(BigInteger linkedId) {
		this.linkedId = linkedId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
