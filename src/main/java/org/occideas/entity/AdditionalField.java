package org.occideas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class AdditionalField implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  private long idadditionalfield;

  private String type;

  //	@AssociationOverride(
//	          name="value",
//	          joinTable=@JoinTable(
//	             name="value",
//	             joinColumns=@JoinColumn(name="idAdditionalField"),
//	             inverseJoinColumns=@JoinColumn(name="idadditionalfield")
//	          )
//	        )
  private String value;


  public AdditionalField() {

  }

  public AdditionalField(long idAdditionalField) {
    this.idadditionalfield = idAdditionalField;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public long getIdadditionalfield() {
    return idadditionalfield;
  }

  public void setIdadditionalfield(long idadditionalfield) {
    this.idadditionalfield = idadditionalfield;
  }


}