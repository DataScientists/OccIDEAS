package org.occideas.entity;

import java.io.Serializable;

public class NodeRulePK implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  protected long idRule;
  protected long idNode;

  public NodeRulePK() {
  }

  public NodeRulePK(long idRule, long idNode) {
    this.idRule = idRule;
    this.idNode = idNode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (idNode ^ (idNode >>> 32));
    result = prime * result + (int) (idRule ^ (idRule >>> 32));
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
    NodeRulePK other = (NodeRulePK) obj;
    if (idNode != other.idNode)
      return false;
    return idRule == other.idRule;
  }

}
