package org.occideas.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Interview_DisplayAnswer")
public class InterviewDisplayAnswer implements java.io.Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private long id;
  @Column(name = "interviewDisplayId")
  private long interviewDisplayId;
  @Column(name = "answerId")
  private long answerId;
  @Column(name = "name")
  private String name;
  @Column(name = "answerFreetext")
  private String answerFreetext;
  @Column(name = "nodeClass")
  private String nodeClass;
  @Column(name = "number")
  private String number;
  @Column(name = "type")
  private String type;
  @Column(name = "deleted")
  private Integer deleted;
  @Column(name = "lastUpdated")
  private Date lastUpdated;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getInterviewDisplayId() {
    return interviewDisplayId;
  }

  public void setInterviewDisplayId(long interviewDisplayId) {
    this.interviewDisplayId = interviewDisplayId;
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

  public String getAnswerFreetext() {
    return answerFreetext;
  }

  public void setAnswerFreetext(String answerFreetext) {
    this.answerFreetext = answerFreetext;
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

}
