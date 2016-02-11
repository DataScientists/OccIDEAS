package org.occideas.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Type;

@Entity 
public class Rule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private long idRule;
	private long agentId;
	private String type;
	private int level;
//	@Type(type="timestamp")
	private Date lastUpdated;
	
	@ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="Node_Rule", 
                joinColumns={@JoinColumn(name="idRule")}, 
                inverseJoinColumns={@JoinColumn(name="idNode")})
	private List<Node> conditions;
	private Long legacyRuleId;
	
	@ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="Rule_AdditionalField", 
                joinColumns={@JoinColumn(name="idRule")}, 
                inverseJoinColumns={@JoinColumn(name="idAdditionalField")})
	private List<AdditionalField> additionalfields;
	
	public Rule() {
		super();
	}
	public Rule(String idRule) {
		super();
		this.setIdRule(Long.parseLong(idRule));
	}
	public Rule(long ruleId) {
		this.setIdRule(ruleId);
	}
	public String getType() {
		if(type==null){
			type = "";
		}
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public long getIdRule() {
		return idRule;
	}
	public void setIdRule(long idRule) {
		this.idRule = idRule;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Long getLegacyRuleId() {
		return legacyRuleId;
	}
	public void setLegacyRuleId(Long legacyRuleId) {
		this.legacyRuleId = legacyRuleId;
	}	
	public long getAgentId() {
		return agentId;
	}
	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}
	/*public List<AdditionalField> getAdditionalfields() {
		return additionalfields;
	}
	public void setAdditionalfields(List<AdditionalField> additionalfields) {
		this.additionalfields = additionalfields;
	}*/
	public List<Node> getConditions() {
		if(conditions==null){
			conditions = new ArrayList<Node>();
		}
		return conditions;
	}
	public void setConditions(List<Node> conditions) {
		this.conditions = conditions;
	}
	
	public List<AdditionalField> getAdditionalfields() {
		return additionalfields;
	}
	public void setAdditionalfields(List<AdditionalField> additionalfields) {
		this.additionalfields = additionalfields;
	}
}
