package org.occideas.entity;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Rule")
public class RulePlain implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private long idRule;
  private long agentId;
  private String type;
  private int level;
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date lastUpdated;
  private int deleted;
  private Long legacyRuleId;

  public long getIdRule() {
    return idRule;
  }

  public void setIdRule(long idRule) {
    this.idRule = idRule;
  }

  public long getAgentId() {
    return agentId;
  }

  public void setAgentId(long agentId) {
    this.agentId = agentId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public Long getLegacyRuleId() {
    return legacyRuleId;
  }

  public void setLegacyRuleId(Long legacyRuleId) {
    this.legacyRuleId = legacyRuleId;
  }

}
