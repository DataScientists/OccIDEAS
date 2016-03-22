package org.occideas.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Where;

@Entity
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
@SelectBeforeUpdate(value=true)
public class Participant implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private long idParticipant;
	
	@OneToMany(mappedBy="participant", fetch = FetchType.EAGER)
	@Where(clause = "parentId = 0")
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	private List<Interview> interviews;
	private int status;
	private String reference;
	private Date lastUpdated;
	private Integer deleted;

	public Participant() {

	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Integer getDeleted() {
		if(deleted==null){
			deleted = 0;
		}
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

	public List<Interview> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<Interview> interviews) {
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