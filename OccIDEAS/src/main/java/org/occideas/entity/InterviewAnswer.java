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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "Interview_Answer")
public class InterviewAnswer implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	@Column(name = "idinterview")
	private long idInterview;
	@Column(name = "topQuestionId")
	private long topQuestionId;
	@Column(name = "parentQuestionId")
	private long parentQuestionId;
	@Column(name = "answerId")
	private long answerId;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "answerFreetext")
	private String answerFreetext;
	@Column(name = "nodeClass")
	private String nodeClass;
	@Column(name = "number")
	private String number;
	@Column(name = "type")
	private String type;
	@Column(name = "link")
	private long link;
	@Column(name = "deleted")
	private Integer deleted;
	@Column(name = "lastUpdated")
	private Date lastUpdated;
	
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.SAVE_UPDATE, CascadeType.PERSIST })
	@JoinColumns({ @JoinColumn(name = "idNode", referencedColumnName = "answerId") })
	private List<ModuleRule> rules;

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

	public long getTopQuestionId() {
		return topQuestionId;
	}

	public void setTopQuestionId(long topQuestionId) {
		this.topQuestionId = topQuestionId;
	}

	public long getParentQuestionId() {
		return parentQuestionId;
	}

	public void setParentQuestionId(long parentQuestionId) {
		this.parentQuestionId = parentQuestionId;
	}

	public long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(long answerId) {
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

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getAnswerFreetext() {
		return answerFreetext;
	}

	public void setAnswerFreetext(String answerFreetext) {
		this.answerFreetext = answerFreetext;
	}

	public long getLink() {
		return link;
	}

	public void setLink(long link) {
		this.link = link;
	}

	public List<ModuleRule> getRules() {
		return rules;
	}

	public void setRules(List<ModuleRule> rules) {
		this.rules = rules;
	}

	
}
