package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantVO {

  private long idParticipant;

  private List<InterviewVO> interviews;
  private List<ParticipantDetailsVO> participantDetails;
  private List<NoteVO> notes;
  private int status;
  private String reference;
  private String statusDescription;
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

	public String getStatusDescription() {

		if (status == 0) {
			statusDescription = "";
		} else if (status == 1) {
			statusDescription = "To be updated";
		} else if (status == 2) {
			statusDescription = "To be reviewed";
		} else if (status == 3) {
			statusDescription = "Ready for interview";
		} else if (status == 4) {
            statusDescription = "Interviews complete";
        } else if (status == 4) {
            statusDescription = "No further contact please";
        }
		return statusDescription;
	}

  public void setStatusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
  }

  public List<NoteVO> getNotes() {
    if (notes == null) {
      ArrayList<NoteVO> allnotes = new ArrayList<NoteVO>();
      if (this.getInterviews() != null) {
        for (InterviewVO interview : this.getInterviews()) {
          allnotes.addAll(interview.getNotes());
        }
      }
        this.notes = allnotes;
    }

      return notes;
  }

    public void setNotes(List<NoteVO> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ParticipantVO [idParticipant=" + idParticipant + ", status=" + status + ", reference=" + reference
                + ", statusDescription=" + statusDescription + "]";
    }

	public List<ParticipantDetailsVO> getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(List<ParticipantDetailsVO> details) {
		this.participantDetails = details;
	}


}
