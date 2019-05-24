package org.occideas.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Interview_Display")
public class InterviewDisplay implements java.io.Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "idinterview")
  private long idinterview;

  @Column(name = "name")
  private String name;

  @Column(name = "number")
  private String number;

  @Column(name = "type")
  private String type;

  @Column(name = "question_id")
  private long questionId;

  @Column(name = "deleted")
  private Integer deleted;

  @Column(name = "sequence")
  private Integer sequence;

  @Column(name = "header")
  private String header;

  @Column(name = "parentModuleId")
  private long parentModuleId;

  @Column(name = "topNodeId")
  private long topNodeId;

  @Column(name = "parentAnswerId")
  private long parentAnswerId;

  @Column(name = "link")
  private long link;

  @Column(name = "description")
  private String description;

  @Column(name = "nodeClass")
  private String nodeClass;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(name = "interviewDisplayId", referencedColumnName = "id", insertable = false, updatable = false)})
  private List<InterviewDisplayAnswer> answers;

  @Column(name = "lastUpdated")
  private Date lastUpdated;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getIdInterview() {
    return idinterview;
  }

  public void setIdInterview(long idinterview) {
    this.idinterview = idinterview;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(long questionId) {
    this.questionId = questionId;
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

  public List<InterviewDisplayAnswer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<InterviewDisplayAnswer> answers) {
    this.answers = answers;
  }

  public Integer getSequence() {
    return sequence;
  }

  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public long getIdinterview() {
    return idinterview;
  }

  public void setIdinterview(long idinterview) {
    this.idinterview = idinterview;
  }

  public long getParentModuleId() {
    return parentModuleId;
  }

  public void setParentModuleId(long parentModuleId) {
    this.parentModuleId = parentModuleId;
  }

  public long getTopNodeId() {
    return topNodeId;
  }

  public void setTopNodeId(long topNodeId) {
    this.topNodeId = topNodeId;
  }

  public long getParentAnswerId() {
    return parentAnswerId;
  }

  public void setParentAnswerId(long parentAnswerId) {
    this.parentAnswerId = parentAnswerId;
  }

  public long getLink() {
    return link;
  }

  public void setLink(long link) {
    this.link = link;
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

}
