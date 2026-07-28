package org.occideas.vo;

import java.util.List;

public class ReportTreeNodeVO {

  private String header;
  private String number;
  private String name;
  private String nodeclass;
  private List<ReportTreeNodeVO> nodes;

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNodeclass() {
    return nodeclass;
  }

  public void setNodeclass(String nodeclass) {
    this.nodeclass = nodeclass;
  }

  public List<ReportTreeNodeVO> getNodes() {
    return nodes;
  }

  public void setNodes(List<ReportTreeNodeVO> nodes) {
    this.nodes = nodes;
  }
}
