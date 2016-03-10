package org.occideas.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@DiscriminatorValue("A")
public class Agent extends AgentInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.EAGER)
	/*@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})*/
	@Fetch(FetchMode.SELECT)
	private AgentGroup agentGroup;
	
	@OneToMany(mappedBy="agent",fetch=FetchType.LAZY)
	@Column(name="idAgent")	
	private List<Rule> rules;

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

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
}
