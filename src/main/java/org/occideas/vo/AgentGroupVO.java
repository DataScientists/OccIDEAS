package org.occideas.vo;

import org.occideas.entity.Agent;

import java.util.Date;
import java.util.List;

public class AgentGroupVO {

  private long idAgent;
  private String name;
  private String description;
  private List<Agent> agents;
  private Date lastUpdated;
  private Integer total = 0;
  private Integer deleted = 0;

  public AgentGroupVO() {
  }

  public AgentGroupVO(long idAgent) {
    this.idAgent = idAgent;
  }

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

  public List<Agent> getAgents() {
    return agents;
  }

  public void setAgents(List<Agent> agents) {
    this.agents = agents;
  }

}
