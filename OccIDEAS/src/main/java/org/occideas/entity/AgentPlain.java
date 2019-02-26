package org.occideas.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AgentInfo")
public class AgentPlain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long idAgent;
	@Column(name="agent_discriminator")
	private String discriminator;
	private String name;
	private String description;
	private Date lastUpdated;
	private int deleted;
	@Column(name = "agentGroup_idAgent")
	private long agentGroupId;

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

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public long getAgentGroupId() {
		return agentGroupId;
	}

	public void setAgentGroupId(long agentGroupId) {
		this.agentGroupId = agentGroupId;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	
}
