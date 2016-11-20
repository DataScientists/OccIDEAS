package org.occideas.vo;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ParticipantFilterVO extends GenericFilterVO{

	private Long idParticipant;
	private Long interviewId;
	private String reference;
	private Integer status;
	public Long getIdParticipant() {
		return idParticipant;
	}
	public void setIdParticipant(Long idParticipant) {
		this.idParticipant = idParticipant;
	}
	public Long getInterviewId() {
		return interviewId;
	}
	public void setInterviewId(Long interviewId) {
		this.interviewId = interviewId;
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
	@Override
	public void applyFilter(GenericFilterVO filter, SQLQuery sqlQuery) {
		if (filter != null && filter instanceof ParticipantFilterVO) {
			ParticipantFilterVO participantFilter = (ParticipantFilterVO) filter;
			if (participantFilter.getIdParticipant() != null) {
				sqlQuery.setParameter("idParticipant", "%"+participantFilter.getIdParticipant()+"%");
			}else{
				sqlQuery.setParameter("idParticipant", "%%");
			}
			if (!StringUtils.isEmpty(participantFilter.getReference())) {
				sqlQuery.setParameter("reference", "%"+participantFilter.getReference()+"%");
			}else{
				sqlQuery.setParameter("reference", "%%");
			}
			if (participantFilter.getStatus() != null) {
				sqlQuery.setParameter("status", "%"+participantFilter.getStatus()+"%");
			}else{
				sqlQuery.setParameter("status", "%%");
			}
			if (participantFilter.getInterviewId() != null) {
				sqlQuery.setParameter("idinterview", "%"+participantFilter.getInterviewId()+"%");
			}else{
				sqlQuery.setParameter("idinterview", "%%");
			}
		}
	} 
	
	
}
