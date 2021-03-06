package org.occideas.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("G")
public class AgentGroup extends AgentInfo {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @OneToMany(mappedBy = "agentGroup", fetch = FetchType.EAGER)
  private List<Agent> agents;


  public AgentGroup() {
    super();
  }

  public AgentGroup(long idAgent) {
    this.setIdAgent(idAgent);
  }

  public AgentGroup(String idAgent) {
    this.setIdAgent(Long.parseLong(idAgent));
  }

  public void addAgent(Agent agent) {
    agent.setGroup(this);
    this.setAgents(this.getAgents() == null ? new ArrayList<Agent>() : this.getAgents());
    this.getAgents().add(agent);
  }

  public List<Agent> getAgents() {
    return agents;
  }

  public void setAgents(List<Agent> agents) {
    this.agents = agents;
  }

}
