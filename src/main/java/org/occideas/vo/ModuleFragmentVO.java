package org.occideas.vo;

public class ModuleFragmentVO {

  private String primaryKey;
  private long moduleId;
  private String moduleName;
  private long idNode;
  private String fragmentName;
  private String nodeNumber;
  private long fragmentId;

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

  public String getFragmentName() {
    return fragmentName;
  }

  public void setFragmentName(String fragmentName) {
    this.fragmentName = fragmentName;
  }

  public String getNodeNumber() {
    return nodeNumber;
  }

  public void setNodeNumber(String nodeNumber) {
    this.nodeNumber = nodeNumber;
  }

  public long getFragmentId() {
    return fragmentId;
  }

  public void setFragmentId(long fragmentId) {
    this.fragmentId = fragmentId;
  }

}
