package org.occideas;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity 

public class AdditionalField implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private long idadditionalfield;

	private String type;

	private String value;

	

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

	public AdditionalField() {

	}

	public AdditionalField(long idAdditionalField) {
		this.idadditionalfield = idAdditionalField;
	}

	public long getIdadditionalfield() {
		return idadditionalfield;
	}

	public void setIdadditionalfield(long idadditionalfield) {
		this.idadditionalfield = idadditionalfield;
	}

	
}