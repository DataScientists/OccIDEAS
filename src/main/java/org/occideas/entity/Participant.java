package org.occideas.entity;

import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class Participant implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long idParticipant;

  @OneToMany(mappedBy = "participant", fetch = FetchType.EAGER)
  @Where(clause = "parentId = 0")
  @Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.PERSIST})
  private List<Interview> interviews;
  private int status;
  private String reference;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
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
    if (deleted == null) {
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