package org.occideas.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Interview_Answer")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class InterviewAnswer implements java.io.Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(name = "idinterview")
  private long idInterview;
  @Column(name = "interviewQuestionId")
  private long interviewQuestionId;
  @Column(name = "topNodeId")
  private long topNodeId;
  @Column(name = "parentQuestionId")
  private long parentQuestionId;
  @Column(name = "answerId")
  private long answerId;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "answerFreetext")
  private String answerFreetext;
  @Column(name = "nodeClass")
  private String nodeClass;
  @Column(name = "number")
  private String number;
  @Column(name = "type")
  private String type;
  @Column(name = "link")
  private long link;
  @Column(name = "isProcessed")
  private boolean isProcessed;
  @Column(name = "modCount")
  private Integer modCount;
  @Column(name = "deleted")
  private Integer deleted;
  @Column(name = "lastUpdated")
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date lastUpdated;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getIdInterview() {
    return idInterview;
  }

  public void setIdInterview(long idInterview) {
    this.idInterview = idInterview;
  }


  public long getParentQuestionId() {
    return parentQuestionId;
  }

  public void setParentQuestionId(long parentQuestionId) {
    this.parentQuestionId = parentQuestionId;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public String getAnswerFreetext() {
    return answerFreetext;
  }

  public void setAnswerFreetext(String answerFreetext) {
    this.answerFreetext = answerFreetext;
  }

  public long getLink() {
    return link;
  }

  public void setLink(long link) {
    this.link = link;
  }

  public boolean isProcessed() {
    return isProcessed;
  }

  public void setProcessed(boolean isProcessed) {
    this.isProcessed = isProcessed;
  }

  public Integer getModCount() {
    return modCount;
  }

  public void setModCount(Integer modCount) {
    this.modCount = modCount;
  }

  public long getTopNodeId() {
    return topNodeId;
  }

  public void setTopNodeId(long topNodeId) {
    this.topNodeId = topNodeId;
  }

  public long getInterviewQuestionId() {
    return interviewQuestionId;
  }

  public void setInterviewQuestionId(long interviewQuestionId) {
    this.interviewQuestionId = interviewQuestionId;
  }


}
