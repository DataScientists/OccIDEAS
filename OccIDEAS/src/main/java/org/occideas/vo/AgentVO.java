package org.occideas.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"isEditing"})
public class AgentVO {

	private long idAgent;
	private String name;
	private String description;

	private AgentGroupVO agentGroup;
	
	private Date lastUpdated;
	private Integer total = 0;
	private Integer deleted = 0;

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

}
