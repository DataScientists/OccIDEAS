package org.occideas.vo;

public class InterviewLinkedVO {

  private long idInterview;
  private long parentQuestionId;
  private long linkedId;
  private String name;
  private String description;
  private Integer deleted;

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

  public long getLinkedId() {
    return linkedId;
  }

  public void setLinkedId(long linkedId) {
    this.linkedId = linkedId;
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

  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(Integer deleted) {
    this.deleted = deleted;
  }

}
