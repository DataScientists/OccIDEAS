package org.occideas.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@DiscriminatorValue("F")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class Fragment extends Node<Question> implements Serializable {

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "idModule", referencedColumnName = "idNode", updatable = false)
  protected List<ModuleRule> moduleRule;

  public Fragment() {
    super();
  }

  public Fragment(Node node) {
    super(node);
  }

  public Fragment(Long idNode) {
    super();
    this.setIdNode(idNode);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public List<ModuleRule> getModuleRule() {
    return moduleRule;
  }

  public void setModuleRule(List<ModuleRule> moduleRule) {
    this.moduleRule = moduleRule;
  }
}
