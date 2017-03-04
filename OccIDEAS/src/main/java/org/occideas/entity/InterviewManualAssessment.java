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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name = "Interview_ManualAssessedRules")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class InterviewManualAssessment implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "idInterview_ManualAssessedRules")
	private long id;
	@Column(name = "idinterview")
	private long idInterview;
	@OneToOne(fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	@JoinColumn(name="idRule",referencedColumnName="idRule",insertable=true,updatable=true)
	private Rule rule;
	
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

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}
	
}
