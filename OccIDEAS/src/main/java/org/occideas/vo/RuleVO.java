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
		if(level!=null){
			if(level.equalsIgnoreCase("0")){
				level = "probHigh";
			}else if(level.equalsIgnoreCase("1")){
				level = "probMedium";
			}else if(level.equalsIgnoreCase("2")){
				level = "probLow";
			}else if(level.equalsIgnoreCase("3")){
				level = "probUnknown";
			}else if(level.equalsIgnoreCase("4")){
				level = "possUnknown";
			}else if(level.equalsIgnoreCase("5")){
				level = "noExposure";
			}
		}
		return level;
	}

	public void setLevel(String level) {
		if(level!=null){
			if(level.equalsIgnoreCase("probHigh")){
				level = "0";
			}else if(level.equalsIgnoreCase("probMedium")){
				level = "1";
			}else if(level.equalsIgnoreCase("probLow")){
				level = "2";
			}else if(level.equalsIgnoreCase("probUnknown")){
				level = "3";
			}else if(level.equalsIgnoreCase("possUnknown")){
				level = "4";
			}else if(level.equalsIgnoreCase("noExposure")){
				level = "5";
			}
		}
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
