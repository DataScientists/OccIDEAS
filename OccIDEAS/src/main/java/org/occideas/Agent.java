package org.occideas;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;



@Entity
@DiscriminatorValue("A")
public class Agent extends AgentInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	
	private AgentGroup agentGroup;

	public Agent() {
		super();
	}

	public Agent(String idAgent) {
		super();
		this.setIdAgent(Long.parseLong(idAgent));
	}

	public Agent(long idAgent) {
		super();
		this.setIdAgent(idAgent);
	}

	@JsonIgnore
	public AgentGroup getGroup() {
		return agentGroup;
	}

	public void setGroup(AgentGroup group) {
		this.agentGroup = group;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Agent))
			return false;
		Agent castOther = (Agent) other;

		return (this.getIdAgent() == castOther.getIdAgent());

	}
}
