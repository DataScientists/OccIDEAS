package org.occideas.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "anchorId","description","type","parentId","link","lastUpdated","originalId","nodeclass","moduleRule","nodes" })
public class InterviewModuleVO {

	private String name;
	private long idNode;
	private long topNodeId;
	private long parentNode;
	private long parentAnswerId;
	private long answerNode;
	private String number;
	private Integer count;
	private String deleted;
	private Integer sequence;
	private long idInterview;
	private String linkNum;
	private List<InterviewQuestionVO> questionsAsked;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getIdNode() {
		return idNode;
	}

	public void setIdNode(long idNode) {
		this.idNode = idNode;
	}

	public long getTopNodeId() {
		return topNodeId;
	}

	public void setTopNodeId(long topNodeId) {
		this.topNodeId = topNodeId;
	}

	public long getParentNode() {
		return parentNode;
	}

	public void setParentNode(long parentNode) {
		this.parentNode = parentNode;
	}

	public long getParentAnswerId() {
		return parentAnswerId;
	}

	public void setParentAnswerId(long parentAnswerId) {
		this.parentAnswerId = parentAnswerId;
	}

	public long getAnswerNode() {
		return answerNode;
	}

	public void setAnswerNode(long answerNode) {
		this.answerNode = answerNode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public long getIdInterview() {
		return idInterview;
	}

	public void setIdInterview(long idInterview) {
		this.idInterview = idInterview;
	}

	public String getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(String linkNum) {
		this.linkNum = linkNum;
	}

	public List<InterviewQuestionVO> getQuestionsAsked() {
		return questionsAsked;
	}

	public void setQuestionsAsked(List<InterviewQuestionVO> questionsAsked) {
		this.questionsAsked = questionsAsked;
	}

}
