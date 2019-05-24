package org.occideas.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class NodeNodeLanguageMod implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String primaryKey;
  private long idNode;
  private long topNodeId;
  private String name;
  private String flag;
  private String description;
  private long languageId;
  private int current;
  private int total;

  public long getLanguageId() {
    return languageId;
  }

  public void setLanguageId(long languageId) {
    this.languageId = languageId;
  }

  public String getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(String primaryKey) {
    this.primaryKey = primaryKey;
  }

  public long getTopNodeId() {
    return topNodeId;
  }

  public void setTopNodeId(long topNodeId) {
    this.topNodeId = topNodeId;
  }

  public long getIdNode() {
    return idNode;
  }

  public void setIdNode(long idNode) {
    this.idNode = idNode;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public int getCurrent() {
    return current;
  }

  public void setCurrent(int current) {
    this.current = current;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
