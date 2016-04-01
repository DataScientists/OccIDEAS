package org.occideas.vo;

import java.math.BigInteger;
import java.util.List;

public class InterviewQuestionVO {

	private long idInterview;
	private long questionId;
	private String name;
	private String description;
	private String nodeClass;
	private String number;
	private String type;
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

}
