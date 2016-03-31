package org.occideas.vo;

import java.math.BigInteger;

public class InterviewAnswerVO {

	private BigInteger idInterview;
	private BigInteger topQuestionId;
	private BigInteger parentQuestionId;
	private BigInteger answerId;
	private String name;
	private String description;
	private String nodeClass;
	private String number;
	private String type;
	private Integer deleted;

	public BigInteger getIdInterview() {
		return idInterview;
	}

	public void setIdInterview(BigInteger idInterview) {
		this.idInterview = idInterview;
	}

	public BigInteger getTopQuestionId() {
		return topQuestionId;
	}

	public void setTopQuestionId(BigInteger topQuestionId) {
		this.topQuestionId = topQuestionId;
	}

	public BigInteger getParentQuestionId() {
		return parentQuestionId;
	}

	public void setParentQuestionId(BigInteger parentQuestionId) {
		this.parentQuestionId = parentQuestionId;
	}

	public BigInteger getAnswerId() {
		return answerId;
	}

	public void setAnswerId(BigInteger answerId) {
		this.answerId = answerId;
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

	public String getNodeClass() {
		return nodeClass;
	}

	public void setNodeClass(String nodeClass) {
		this.nodeClass = nodeClass;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
