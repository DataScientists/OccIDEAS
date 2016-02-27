package org.occideas.vo;

public class RuleAdditionalFieldVO {

	private long idRuleAdditionalField;

	private AdditionalFieldVO additionalfield;
	
	private String value;

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

}
