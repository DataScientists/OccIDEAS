package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class InterviewAnswerVO {

  private long id;
  private long idInterview;
  private long interviewQuestionId;
  private long topNodeId;
  private long parentQuestionId;
  private long answerId;
  private long link;
  private String name;
  private String description;
  private String answerFreetext;
  private String nodeClass;
  private String number;
  private String type;
  private Integer deleted;
  private Integer modCount;
  private boolean isProcessed;
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private Date lastUpdated;

  private List<ModuleRuleVO> rules;

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

  public List<ModuleRuleVO> getRules() {
    return rules;
  }

  public void setRules(List<ModuleRuleVO> rules) {
    this.rules = rules;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Integer getModCount() {
    return modCount;
  }

  public void setModCount(Integer modCount) {
    this.modCount = modCount;
  }

  public boolean getIsProcessed() {
    return isProcessed;
  }

  public void setIsProcessed(boolean isProcessed) {
    this.isProcessed = isProcessed;
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

  @Override
  public String toString() {
    return "InterviewAnswerVO [id=" + id + ", idInterview=" + idInterview + ", interviewQuestionId="
      + interviewQuestionId + ", topNodeId=" + topNodeId + ", parentQuestionId=" + parentQuestionId
      + ", answerId=" + answerId + ", link=" + link + ", name=" + name + ", description=" + description
      + ", answerFreetext=" + answerFreetext + ", nodeClass=" + nodeClass + ", number=" + number + ", type="
      + type + ", deleted=" + deleted + ", isProcessed=" + isProcessed + "]";
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }


}
