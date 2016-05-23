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
import org.hibernate.annotations.Where;

@Entity
@Table(name = "Interview_Display")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class InterviewDisplay implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "idinterview")
	private long idinterview;

	@Column(name = "idNode")
	private long idNode;

	@Column(name = "name")
	private String name;

	@Column(name = "number")
	private String number;

	@Column(name = "type")
	private String type;

	@Column(name = "question_id")
	private long questionId;

	@Column(name = "count")
	private Integer count;

	@Column(name = "deleted")
	private Integer deleted;

	@Column(name = "sequence")
	private Integer sequence;

	@OneToMany(fetch = FetchType.LAZY)
	@Where(clause = "deleted = 0")
	@JoinColumns({ @JoinColumn(name = "parentQuestionId", referencedColumnName = "question_id"
				,insertable=false,updatable=false),
			@JoinColumn(name = "idinterview", referencedColumnName = "idinterview"
				,insertable=false,updatable=false),
			@JoinColumn(name = "modCount", referencedColumnName = "count"
			,insertable=false,updatable=false)})
	private List<InterviewAnswer> answers;

	@Column(name = "lastUpdated")
	private Date lastUpdated;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdInterview() {
		return idinterview;
	}

	public void setIdInterview(long idinterview) {
		this.idinterview = idinterview;
	}

	public long getIdNode() {
		return idNode;
	}

	public void setIdNode(long idNode) {
		this.idNode = idNode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<InterviewAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<InterviewAnswer> answers) {
		this.answers = answers;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
