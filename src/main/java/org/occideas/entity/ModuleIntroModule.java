package org.occideas.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
@Table(name = "Module_IntroModule")
public class ModuleIntroModule implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @Id
  private String primaryKey;
  private long moduleId;
  private String moduleName;
  private long idNode;
  private String moduleLinkName;
  private String nodeNumber;
  private long moduleLinkId;

  public String getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(String primaryKey) {
    this.primaryKey = primaryKey;
  }

  public long getModuleId() {
    return moduleId;
  }

  public void setModuleId(long moduleId) {
    this.moduleId = moduleId;
  }

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public long getIdNode() {
    return idNode;
  }

  public void setIdNode(long idNode) {
    this.idNode = idNode;
  }

  public String getNodeNumber() {
    return nodeNumber;
  }

  public void setNodeNumber(String nodeNumber) {
    this.nodeNumber = nodeNumber;
  }

  public String getModuleLinkName() {
    return moduleLinkName;
  }

  public void setModuleLinkName(String moduleLinkName) {
    this.moduleLinkName = moduleLinkName;
  }

  public long getModuleLinkId() {
    return moduleLinkId;
  }

  public void setModuleLinkId(long moduleLinkId) {
    this.moduleLinkId = moduleLinkId;
  }

}
