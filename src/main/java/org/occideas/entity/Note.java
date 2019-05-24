package org.occideas.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Note implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private long idNote;

  @ManyToOne(fetch = FetchType.LAZY)
  private Node node;
  private String type;

  private long interviewId;

  @Column(length = 2048)
  private String text;
  private Date lastUpdated;
  private Integer deleted;

  public Note() {

  }

  public Note(long idNote) {
    this.idNote = idNote;
  }

  public Note(String text) {
    this.text = text;
  }

  public long getIdNote() {
    return idNote;
  }

  public void setIdNote(long idNote) {
    this.idNote = idNote;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Node getNode() {
    return node;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public long getInterviewId() {
    return interviewId;
  }

  public void setInterviewId(long interviewId) {
    this.interviewId = interviewId;
  }

}