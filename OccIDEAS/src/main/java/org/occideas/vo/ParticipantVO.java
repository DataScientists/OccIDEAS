package org.occideas.vo;

import java.util.Date;
import java.util.List;

public class ParticipantVO {

private long idParticipant;
	
	private List<InterviewVO> interviews;
	private int status;
	private String reference;
	private Date lastUpdated;
	private Integer deleted;

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public long getIdParticipant() {
		return idParticipant;
	}

	public void setIdParticipant(long idParticipant) {
		this.idParticipant = idParticipant;
	}

	public List<InterviewVO> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<InterviewVO> interviews) {
		this.interviews = interviews;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

}