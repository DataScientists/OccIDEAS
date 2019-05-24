package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.occideas.entity.Note;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericNodeVO {

  private long idNode;
  private String name;
  private String description;
  private String type;
  private int sequence;
  private String number;
  private String parentId;
  private long link;
  private long topNodeId;
  private Date lastUpdated;
  private List<Note> notes;
  private long originalId;
  private Integer deleted = 0;
  private String nodeclass;

  public long getIdNode() {
    return idNode;
  }

  public void setIdNode(long idNode) {
    this.idNode = idNode;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getSequence() {
    return sequence;
  }

  public void setSequence(int sequence) {
    this.sequence = sequence;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public long getLink() {
    return link;
  }

  public void setLink(long link) {
    this.link = link;
  }

  public long getTopNodeId() {
    return topNodeId;
  }

  public void setTopNodeId(long topNodeId) {
    this.topNodeId = topNodeId;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public List<Note> getNotes() {
    return notes;
  }

  public void setNotes(List<Note> notes) {
    this.notes = notes;
  }

  public long getOriginalId() {
    return originalId;
  }

  public void setOriginalId(long originalId) {
    this.originalId = originalId;
  }

  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(Integer deleted) {
    this.deleted = deleted;
  }

  public String getNodeclass() {
    return nodeclass;
  }

  public void setNodeclass(String nodeclass) {
    this.nodeclass = nodeclass;
  }

}
