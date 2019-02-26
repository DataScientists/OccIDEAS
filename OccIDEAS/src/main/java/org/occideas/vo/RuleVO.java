package org.occideas.vo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties({"agentName"})
public class RuleVO {

	private long idRule;
	private long agentId;
	private AgentVO agent;
	private String type;
	private String level;
	private Date lastUpdated;
	@JsonInclude(Include.NON_NULL)
	private List<PossibleAnswerVO> conditions;
	private Long legacyRuleId;
	@JsonInclude(Include.NON_NULL)
	private List<RuleAdditionalFieldVO> ruleAdditionalfields = new ArrayList<>();
	private int levelValue;
	private Integer deleted;
	private Double noiseHours;
	private Double noisePartialExposure;
	private String backgroundFlag;
	private boolean isRatio;
	
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

	public List<RuleAdditionalFieldVO> getRuleAdditionalfields() {
		return ruleAdditionalfields;
	}

	public void setRuleAdditionalfields(List<RuleAdditionalFieldVO> ruleAdditionalfields) {
		this.ruleAdditionalfields = ruleAdditionalfields;
	}
	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).toHashCode();
    }
	@Override
    public boolean equals(Object o)
    {
        return this.getIdRule()==((RuleVO)o).getIdRule();
    }

	public AgentVO getAgent() {
		return agent;
	}

	public void setAgent(AgentVO agent) {
		this.agent = agent;
	}

	public int getLevelValue() {
		return levelValue;
	}

	public void setLevelValue(int levelValue) {
		this.levelValue = levelValue;
	}

	public Integer getDeleted() {
		if(deleted==null){
			deleted=0;
		}
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Double getNoiseHours() {
		return noiseHours;
	}

	public void setNoiseHours(Double noiseHours) {
		this.noiseHours = noiseHours;
	}

	public Double getNoisePartialExposure() {
		return noisePartialExposure;
	}

	public void setNoisePartialExposure(Double noisePartialExposure) {
		this.noisePartialExposure = noisePartialExposure;
	}

	public boolean isRatio() {
		return isRatio;
	}

	public void setRatio(boolean isRatio) {
		this.isRatio = isRatio;
	}

	public String getBackgroundFlag() {
		return backgroundFlag;
	}

	public void setBackgroundFlag(String backgroundFlag) {
		this.backgroundFlag = backgroundFlag;
	}

	@Override
	public String toString() {
		return "RuleVO [idRule=" + idRule + ", agentId=" + agentId + ", agent=" + agent + ", type=" + type + ", level="
				+ level + ", lastUpdated=" + lastUpdated + ", conditions=" + conditions + ", legacyRuleId="
				+ legacyRuleId + ", ruleAdditionalfields=" + ruleAdditionalfields + ", levelValue=" + levelValue
				+ ", deleted=" + deleted + ", noiseHours=" + noiseHours + ", noisePartialExposure="
				+ noisePartialExposure + ", backgroundFlag=" + backgroundFlag + ", isRatio=" + isRatio + "]";
	}

	
	
}
