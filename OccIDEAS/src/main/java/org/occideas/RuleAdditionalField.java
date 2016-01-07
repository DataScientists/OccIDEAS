package org.occideas;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Rule_AdditionalField")
public class RuleAdditionalField  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private long idRuleAdditionalField;
	@ManyToOne
    @JoinColumn(name = "idRule")
	private Rule rule;
	@ManyToOne
    @JoinColumn(name = "idAdditionalField")
	private AdditionalField additionalfield;
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
