package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class InterviewDisplayAnswerVO {

  private long id;
  private long interviewDisplayId;
  private long answerId;
  private String name;
  private String answerFreetext;
  private String nodeClass;
  private String number;
  private String type;
  private Integer deleted;
  @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
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
