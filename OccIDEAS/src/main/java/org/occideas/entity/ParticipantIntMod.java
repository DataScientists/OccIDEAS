package org.occideas.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ParticipantIntMod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long idParticipant;
	private String reference;
	private String assessedStatus;
	private Integer status;
	private Date lastUpdated;
	private int deleted;
	private Long idinterview;
	private transient String statusDescription;
	private Long idModule;	
	private String interviewModuleName;
		
		
	public Long getIdModule() {
		return idModule;
	}

	public void setIdModule(Long idModule) {
		this.idModule = idModule;
	}

	public String getInterviewModuleName() {
		return interviewModuleName;
	}

	public void setInterviewModuleName(String interviewModuleName) {
		this.interviewModuleName = interviewModuleName;
	}			

	public Long getIdParticipant() {
		return idParticipant;
	}

	public void setIdParticipant(Long idParticipant) {
		this.idParticipant = idParticipant;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public Long getIdinterview() {
		return idinterview;
	}

	public void setIdinterview(Long idinterview) {
		this.idinterview = idinterview;
	}

	public String getStatusDescription() {

		if (status == 0) {
			statusDescription = "Running";
		} else if (status == 1) {
			statusDescription = "Partial";
		} else if (status == 2) {
			statusDescription = "Completed";
		}
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getAssessedStatus() {
		return assessedStatus;
	}

	public void setAssessedStatus(String assessedStatus) {
		this.assessedStatus = assessedStatus;
	}

}
