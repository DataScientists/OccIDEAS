package org.occideas.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name = "Interview_Module")
@IdClass(InterviewModPk.class)
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class InterviewModule implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "name")
	private String name;
	@Id
	@Column(name = "idNode")
	private long idNode;
	@Column(name = "topNodeId")
	private long topNodeId;
	@Column(name = "parentNode")
	private long parentNode;
	@Column(name = "parentAnswerId")
	private long parentAnswerId;
	@Column(name = "answerNode")
	private long answerNode;
	@Column(name = "number")
	private String number;
	@Id
	@Column(name = "count")
	private Integer count;
	@Column(name = "deleted")
	private String deleted;
	@Column(name = "sequence")
	private Integer sequence;
	@Id
	@Column(name = "idinterview")
	private long idinterview;

	@OneToMany(fetch = FetchType.LAZY,targetEntity=InterviewQuestion.class)
	@JoinColumns({
		@JoinColumn(name = "topNodeId", referencedColumnName = "idNode",insertable=false,updatable=false),
		@JoinColumn(name = "idinterview", referencedColumnName = "idinterview",insertable=false,updatable=false),
		@JoinColumn(name = "modCount", referencedColumnName = "count",insertable=false,updatable=false)})
	private List<InterviewQuestion> questionsAsked;
	
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

	public long getAnswerNode() {
		return answerNode;
	}

	public void setAnswerNode(long answerNode) {
		this.answerNode = answerNode;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public long getIdInterview() {
		return idinterview;
	}

	public void setIdInterview(long idinterview) {
		this.idinterview = idinterview;
	}

	public List<InterviewQuestion> getQuestionsAsked() {
		return questionsAsked;
	}

	public void setQuestionsAsked(List<InterviewQuestion> questionsAsked) {
		this.questionsAsked = questionsAsked;
	}

}
