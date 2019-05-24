package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentVO {

  private String discriminator;
  private long idAgent;
  private String name;
  private String description;

  private AgentGroupVO agentGroup;

  private Date lastUpdated;
  private Integer total = 0;
  private Integer deleted = 0;

  private List<RuleVO> rules;

  public long getIdAgent() {
    return idAgent;
  }

  public void setIdAgent(long idAgent) {
    this.idAgent = idAgent;
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

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(Integer deleted) {
    this.deleted = deleted;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public AgentGroupVO getAgentGroup() {
    return agentGroup;
  }

  public void setAgentGroup(AgentGroupVO agentGroup) {
    this.agentGroup = agentGroup;
  }

  public List<RuleVO> getRules() {
    return rules;
  }

  public void setRules(List<RuleVO> rules) {
    this.rules = rules;
  }

  public String getDiscriminator() {
    return discriminator;
  }

  public void setDiscriminator(String discriminator) {
    this.discriminator = discriminator;
  }

  @Override
  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof AgentVO))
      return false;
    AgentVO castOther = (AgentVO) other;

    return (this.getIdAgent() == castOther.getIdAgent());

  }

}
