package org.occideas.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
@Table(name = "InterviewIntroModule_Module")
public class InterviewIntroModuleModule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String primaryKey;
	private long idModule;
	private String introModuleNodeName;
	private long interviewPrimaryKey;
	private long interviewId;
	private String interviewModuleName;

	public long getIdModule() {
		return idModule;
	}

	public void setIdModule(long idModule) {
		this.idModule = idModule;
	}

	public String getIntroModuleNodeName() {
		return introModuleNodeName;
	}

	public void setIntroModuleNodeName(String introModuleNodeName) {
		this.introModuleNodeName = introModuleNodeName;
	}

	public long getInterviewId() {
		return interviewId;
	}

	public void setInterviewId(long interviewId) {
		this.interviewId = interviewId;
	}

	public String getInterviewModuleName() {
		return interviewModuleName;
	}

	public void setInterviewModuleName(String interviewModuleName) {
		this.interviewModuleName = interviewModuleName;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public long getInterviewPrimaryKey() {
		return interviewPrimaryKey;
	}

	public void setInterviewPrimaryKey(long interviewPrimaryKey) {
		this.interviewPrimaryKey = interviewPrimaryKey;
	}

}