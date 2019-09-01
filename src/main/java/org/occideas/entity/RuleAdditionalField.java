package org.occideas.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;

@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
@Table(name = "Rule_AdditionalField")
public class RuleAdditionalField implements java.io.Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long idRuleAdditionalField;
  private long idRule;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idadditionalfield", referencedColumnName = "idadditionalfield")
  private AdditionalField additionalfield;
  @Column(name = "value", nullable = false)
  private String value;

  public RuleAdditionalField() {
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

  public Long getIdRuleAdditionalField() {
    return idRuleAdditionalField;
  }

  public void setIdRuleAdditionalField(Long idRuleAdditionalField) {
    this.idRuleAdditionalField = idRuleAdditionalField;
  }

  public long getIdRule() {
    return idRule;
  }

  public void setIdRule(long idRule) {
    this.idRule = idRule;
  }


}
