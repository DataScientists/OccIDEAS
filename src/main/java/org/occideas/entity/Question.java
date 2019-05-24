package org.occideas.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Q")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class Question extends Node<PossibleAnswer> {

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "idNode", referencedColumnName = "idNode", updatable = false)
  protected List<ModuleRule> moduleRule;

  public Question() {
    super();
  }

  public Question(Node<PossibleAnswer> node) {
    super(node);
  }

  public Question(long idNode) {
    super();
    this.setIdNode(idNode);
  }

  public Question(String idNode) {
    this.setIdNode(Integer.parseInt(idNode));
  }

  public List<ModuleRule> getModuleRule() {
    return moduleRule;
  }

  public void setModuleRule(List<ModuleRule> moduleRule) {
    this.moduleRule = moduleRule;
  }

  @Override
  public String toString() {
    return "Question [childNodes=" + childNodes + ", moduleRule=" + moduleRule + ", idNode=" + getIdNode() + ", type="
      + type + "]";
  }


}
