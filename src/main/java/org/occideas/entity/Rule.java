package org.occideas.entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class Rule implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long idRule;
  private long agentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "agentId", referencedColumnName = "idAgent", insertable = false, updatable = false)
  private Agent agent;
  private String type;
  private int level;
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date lastUpdated;
  private int deleted;

  @ManyToMany(fetch = FetchType.LAZY)
  @Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.PERSIST})
  @JoinTable(name = "Node_Rule",
    joinColumns = {@JoinColumn(name = "idRule")},
    inverseJoinColumns = {@JoinColumn(name = "idNode")})
  private List<PossibleAnswer> conditions;
  private Long legacyRuleId;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "idRule")
  @Cascade(value = {CascadeType.ALL})
  private List<RuleAdditionalField> ruleAdditionalfields;

  public Rule() {
    super();
  }

  public Rule(String idRule) {
    super();
    this.setIdRule(Long.parseLong(idRule));
  }

  public Rule(long ruleId) {
    this.setIdRule(ruleId);
  }

  public String getType() {
    if (type == null) {
      type = "";
    }
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public long getIdRule() {
    return idRule;
  }

  public void setIdRule(long idRule) {
    this.idRule = idRule;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public Long getLegacyRuleId() {
    return legacyRuleId;
  }

  public void setLegacyRuleId(Long legacyRuleId) {
    this.legacyRuleId = legacyRuleId;
  }

  public long getAgentId() {
    return agentId;
  }

  public void setAgentId(long agentId) {
    this.agentId = agentId;
  }

  public List<PossibleAnswer> getConditions() {
    if (conditions == null) {
      conditions = new ArrayList<PossibleAnswer>();
    }
    return conditions;
  }

  public void setConditions(List<PossibleAnswer> conditions) {
    this.conditions = conditions;
  }

  public List<RuleAdditionalField> getRuleAdditionalfields() {
    return ruleAdditionalfields;
  }

  public void setRuleAdditionalfields(List<RuleAdditionalField> ruleAdditionalfields) {
    this.ruleAdditionalfields = ruleAdditionalfields;
  }

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public Agent getAgent() {
    return agent;
  }

  public void setAgent(Agent agent) {
    this.agent = agent;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).toHashCode();
  }

  @Override
  public boolean equals(Object o) {
    return this.getIdRule() == ((Rule) o).getIdRule();
  }
}
