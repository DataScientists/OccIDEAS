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
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name = "Interview_Question")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
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

	@Column(name = "parentModuleId")
	private long parentModuleId;

	@Column(name = "topNodeId")
	private long topNodeId;

	@Column(name = "modCount")
	private Integer modCount;

	@Column(name = "parentAnswerId")
	private long parentAnswerId;

	@Column(name = "link")
	private long link;
	
	@Column(name = "isProcessed")
	private boolean isProcessed;
	

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "interviewQuestionId", referencedColumnName = "id"
				,insertable=false,updatable=false)})
	private List<InterviewAnswer> answers;

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
	
	@Column(name = "intQuestionSequence")
	private Integer intQuestionSequence;

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

	public long getParentModuleId() {
		return parentModuleId;
	}

	public void setParentModuleId(long parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	
	

}
