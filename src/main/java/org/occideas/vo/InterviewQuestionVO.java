package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties({"isProcessed", "lQid", "idNode", "count"})
public class InterviewQuestionVO {

  private long id;
  private long idInterview;
  private long questionId;
  private long parentModuleId;
  private long parentAnswerId;
  private long topNodeId;
  private Integer modCount;
  private long link;
  private String name;
  private String description;
  private String nodeClass;
  private String number;
  private String type;
  private Integer intQuestionSequence;
  private Integer deleted;
  private List<InterviewAnswerVO> answers;
  private InterviewLinkedVO linkingQuestion;
  private boolean isProcessed;
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private Date lastUpdated;

  public long getIdInterview() {
    return idInterview;
  }

  public void setIdInterview(long idInterview) {
    this.idInterview = idInterview;
  }

  public long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(long questionId) {
    this.questionId = questionId;
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

  public List<InterviewAnswerVO> getAnswers() {
    if (answers == null) {
      answers = new ArrayList<InterviewAnswerVO>();
    }
    return answers;
  }

  public void setAnswers(List<InterviewAnswerVO> answers) {
    this.answers = answers;
  }

  public InterviewLinkedVO getLinkingQuestion() {
    return linkingQuestion;
  }

  public void setLinkingQuestion(InterviewLinkedVO linkingQuestion) {
    this.linkingQuestion = linkingQuestion;
  }


  public long getTopNodeId() {
    return topNodeId;
  }

  public void setTopNodeId(long topNodeId) {
    this.topNodeId = topNodeId;
  }

  public long getLink() {
    return link;
  }

  public void setLink(long link) {
    this.link = link;
  }

  public long getParentAnswerId() {
    return parentAnswerId;
  }

  public void setParentAnswerId(long parentAnswerId) {
    this.parentAnswerId = parentAnswerId;
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

  public Integer getIntQuestionSequence() {
    return intQuestionSequence;
  }

  public void setIntQuestionSequence(Integer intQuestionSequence) {
    this.intQuestionSequence = intQuestionSequence;
  }

  public long getParentModuleId() {
    return parentModuleId;
  }

  public void setParentModuleId(long parentModuleId) {
    this.parentModuleId = parentModuleId;
  }

  public boolean isProcessed() {
    return isProcessed;
  }

  public void setProcessed(boolean isProcessed) {
    this.isProcessed = isProcessed;
  }

  @Override
  public String toString() {
    return "InterviewQuestionVO [id=" + id + ", idInterview=" + idInterview + ", questionId=" + questionId
      + ", parentAnswerId=" + parentAnswerId + ", topNodeId=" + topNodeId
      + ", modCount=" + modCount + ", link=" + link + ", name=" + name + ", description=" + description
      + ", nodeClass=" + nodeClass + ", number=" + number + ", type=" + type + ", intQuestionSequence="
      + intQuestionSequence + ", deleted=" + deleted + ", answers=" + answers + ", linkingQuestion="
      + linkingQuestion + "]";
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + (int) (parentAnswerId ^ (parentAnswerId >>> 32));
    result = prime * result + (int) (questionId ^ (questionId >>> 32));
    result = prime * result + (int) (topNodeId ^ (topNodeId >>> 32));
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    InterviewQuestionVO other = (InterviewQuestionVO) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (parentAnswerId != other.parentAnswerId)
      return false;
    if (questionId != other.questionId)
      return false;
    if (topNodeId != other.topNodeId)
      return false;
    if (type == null) {
      return other.type == null;
    } else return type.equals(other.type);
  }


}
