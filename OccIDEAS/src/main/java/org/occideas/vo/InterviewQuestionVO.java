package org.occideas.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "isProcessed", "lQid","idNode","count" })
public class InterviewQuestionVO {

	private long id;
	private long idInterview;
	private long questionId;
	private long parentId;
	private long parentAnswerId;
	private long topNodeId;
	private Integer modCount;
	private long link;
	private String name;
	private String description;
	private String nodeClass;
	private String number;
	private String type;
	private Integer intQuestionSequence;
	private Integer deleted;
	private List<InterviewAnswerVO> answers;
	private InterviewLinkedVO linkingQuestion;

	public long getIdInterview() {
		return idInterview;
	}

	public void setIdInterview(long idInterview) {
		this.idInterview = idInterview;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
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

	public List<InterviewAnswerVO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<InterviewAnswerVO> answers) {
		this.answers = answers;
	}

	public InterviewLinkedVO getLinkingQuestion() {
		return linkingQuestion;
	}

	public void setLinkingQuestion(InterviewLinkedVO linkingQuestion) {
		this.linkingQuestion = linkingQuestion;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public long getTopNodeId() {
		return topNodeId;
	}

	public void setTopNodeId(long topNodeId) {
		this.topNodeId = topNodeId;
	}

	public long getLink() {
		return link;
	}

	public void setLink(long link) {
		this.link = link;
	}

	public long getParentAnswerId() {
		return parentAnswerId;
	}

	public void setParentAnswerId(long parentAnswerId) {
		this.parentAnswerId = parentAnswerId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getModCount() {
		return modCount;
	}

	public void setModCount(Integer modCount) {
		this.modCount = modCount;
	}

	public Integer getIntQuestionSequence() {
		return intQuestionSequence;
	}

	public void setIntQuestionSequence(Integer intQuestionSequence) {
		this.intQuestionSequence = intQuestionSequence;
	}

}
