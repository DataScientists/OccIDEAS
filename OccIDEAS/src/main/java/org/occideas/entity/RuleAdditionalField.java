package org.occideas.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
@Table(name = "Rule_AdditionalField")
public class RuleAdditionalField  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private long idRuleAdditionalField;
	@Transient
	private Rule rule;
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idadditionalfield",referencedColumnName="idadditionalfield",insertable=false,updatable=false)
	private AdditionalField additionalfield;
	@Column(name = "value", nullable = false)
	private String value;
	
	public RuleAdditionalField() {
	}
	
	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public AdditionalField getAdditionalfield() {
		return additionalfield;
	}

	public void setAdditionalfield(AdditionalField additionalfield) {
		this.additionalfield = additionalfield;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	

}
