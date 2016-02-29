package org.occideas.vo;

public class RuleAdditionalFieldVO {

	private long idRuleAdditionalField;

	private AdditionalFieldVO additionalfield;
	
	private String value;

	private long idRule;
	
	public long getIdRuleAdditionalField() {
		return idRuleAdditionalField;
	}

	public void setIdRuleAdditionalField(long idRuleAdditionalField) {
		this.idRuleAdditionalField = idRuleAdditionalField;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public AdditionalFieldVO getAdditionalfield() {
		return additionalfield;
	}

	public void setAdditionalfield(AdditionalFieldVO additionalfield) {
		this.additionalfield = additionalfield;
	}

	public long getIdRule() {
		return idRule;
	}

	public void setIdRule(long idRule) {
		this.idRule = idRule;
	}
	
	
}
