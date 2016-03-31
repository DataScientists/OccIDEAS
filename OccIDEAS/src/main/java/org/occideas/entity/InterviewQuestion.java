package org.occideas.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "interview_question")
public class InterviewQuestion {

	@Id
	@Column(name = "id")
	private BigInteger id;

	@Column(name = "idinterview")
	private BigInteger idInterview;

	@Column(name = "question_id")
	private BigInteger questionId;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "question_id", referencedColumnName = "topQuestionId"),
			@JoinColumn(name = "idinterview", referencedColumnName = "idinterview") })
	private List<InterviewAnswer> answers;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "question_id", referencedColumnName = "parentQuestionId"),
			@JoinColumn(name = "idinterview", referencedColumnName = "idinterview") })
	private InterviewLinked linkingQuestion;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "nodeClass")
	private String nodeClass;

	@Column(name = "number")
	private String number;

	@Column(name = "type")
	private String type;

	@Column(name = "deleted")
	private Integer deleted;

	@Column(name = "lastUpdated")
	private Date lastUpdated;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getIdInterview() {
		return idInterview;
	}

	public void setIdInterview(BigInteger idInterview) {
		this.idInterview = idInterview;
	}

	public BigInteger getQuestionId() {
		return questionId;
	}

	public void setQuestionId(BigInteger questionId) {
		this.questionId = questionId;
	}

	public List<InterviewAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<InterviewAnswer> answers) {
		this.answers = answers;
	}
	
	public InterviewLinked getLinkingQuestion() {
		return linkingQuestion;
	}

	public void setLinkingQuestion(InterviewLinked linkingQuestion) {
		this.linkingQuestion = linkingQuestion;
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

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
