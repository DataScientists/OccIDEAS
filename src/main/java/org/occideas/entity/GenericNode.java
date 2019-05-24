package org.occideas.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
@Table(name = "Node")
public class GenericNode implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1901018915031736717L;
  @Id
  protected long idNode;
  @Column(length = 2048)
  protected String name;
  @Column(length = 2048)
  protected String description;
  protected String type;
  protected int sequence;
  protected String number;
  @Column(name = "parent_idNode")
  protected String parentId;
  protected long link;
  protected long topNodeId;
  protected Date lastUpdated;
  @OneToMany(mappedBy = "node", fetch = FetchType.LAZY)
  @JsonInclude(Include.NON_EMPTY)
  protected List<Note> notes;
  protected long originalId;
  protected Integer deleted = 0;
  protected String nodeclass;
  public GenericNode() {
    super();
  }

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
