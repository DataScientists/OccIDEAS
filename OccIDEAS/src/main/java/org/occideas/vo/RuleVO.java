package org.occideas.vo;

import java.sql.Date;
import java.util.List;

import org.occideas.entity.AdditionalField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties({"agentName"})
public class RuleVO {

	private long idRule;
	private long agentId;
	private String type;
	private String level;
	private Date lastUpdated;
	@JsonInclude(Include.NON_NULL)
	private List<PossibleAnswerVO> conditions;
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

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public List<PossibleAnswerVO> getConditions() {
		return conditions;
	}

	public void setConditions(List<PossibleAnswerVO> conditions) {
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
