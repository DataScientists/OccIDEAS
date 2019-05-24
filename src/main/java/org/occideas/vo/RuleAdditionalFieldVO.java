package org.occideas.vo;

public class RuleAdditionalFieldVO {

  private Long idRuleAdditionalField;

  private AdditionalFieldVO additionalfield;

  private String value;

  private long idRule;

  public Long getIdRuleAdditionalField() {
    return idRuleAdditionalField;
  }

  public void setIdRuleAdditionalField(Long idRuleAdditionalField) {
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
