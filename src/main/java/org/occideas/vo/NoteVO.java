package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties({"isEditing"})
public class NoteVO {

  private long idNote;
  @JsonInclude(Include.NON_EMPTY)
  @JsonProperty(value = "nodes")
  private NodeVO nodeVO;
  private long interviewId;
  private String type;
  private String text;
  private Date lastUpdated;
  private Integer deleted;

  public long getIdNote() {
    return idNote;
  }

  public void setIdNote(long idNote) {
    this.idNote = idNote;
  }

  public NodeVO getNodeVO() {
    return nodeVO;
  }

  public void setNodeVO(NodeVO nodeVO) {
    this.nodeVO = nodeVO;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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
    if (this.deleted == null) {
      deleted = 0;
    }
    return deleted;
  }

  public void setDeleted(Integer deleted) {
    this.deleted = deleted;
  }

  public long getInterviewId() {
    return interviewId;
  }

  public void setInterviewId(long interviewId) {
    this.interviewId = interviewId;
  }

}
