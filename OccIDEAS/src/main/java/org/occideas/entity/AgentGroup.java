package org.occideas.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("G")
public class AgentGroup extends AgentInfo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@OneToMany(mappedBy="agentGroup",fetch=FetchType.EAGER)
	@Column(name="agentGroup_idAgent")	
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
		this.setAgents(this.getAgents() == null?new ArrayList<Agent>():this.getAgents());
		this.getAgents().add(agent);
	}
	
	public List<Agent> getAgents() {
		return agents;
	}
	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}

}
