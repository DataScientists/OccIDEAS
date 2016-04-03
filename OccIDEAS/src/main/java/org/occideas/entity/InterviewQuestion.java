package org.occideas.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "Interview_Question")
public class InterviewQuestion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "idinterview")
	private long idInterview;

	@Column(name = "question_id")
	private long questionId;
	
	@Column(name = "parentId")
	private long parentId;

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	@JoinColumns({ @JoinColumn(name = "topQuestionId", referencedColumnName = "question_id"),
			@JoinColumn(name = "idinterview", referencedColumnName = "idinterview") })
	private List<InterviewAnswer> answers;

	@OneToOne(fetch = FetchType.LAZY)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	@JoinColumns({ @JoinColumn(name = "question_id", referencedColumnName = "parentQuestionId"
			,insertable=false,updatable=false),
			@JoinColumn(name = "idinterview", referencedColumnName = "idinterview"
					,insertable=false,updatable=false) })
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	
}
