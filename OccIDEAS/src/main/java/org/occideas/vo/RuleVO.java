package org.occideas.vo;

import java.sql.Date;
import java.util.List;

import org.occideas.entity.AdditionalField;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class RuleVO {

	private long idRule;
	private long agentId;
	private String type;
	private int level;
	private Date lastUpdated;
	@JsonInclude(Include.NON_NULL)
	private List<NodeVO> conditions;
	private Long legacyRuleId;
	@JsonInclude(Include.NON_NULL)
	private List<AdditionalField> additionalfields;

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

	public List<NodeVO> getConditions() {
		return conditions;
	}

	public void setConditions(List<NodeVO> conditions) {
		this.conditions = conditions;
	}

	public Long getLegacyRuleId() {
		return legacyRuleId;
	}

	public void setLegacyRuleId(Long legacyRuleId) {
		this.legacyRuleId = legacyRuleId;
	}

	public List<AdditionalField> getAdditionalfields() {
		return additionalfields;
	}

	public void setAdditionalfields(List<AdditionalField> additionalfields) {
		this.additionalfields = additionalfields;
	}

}