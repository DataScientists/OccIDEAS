package org.occideas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "sys", name = "sys_config")
public class SystemProperty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String variable;
	@Column(name = "value")
	private String value;
	@Column(name = "set_by")
	private String setBy;

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSetBy() {
		return setBy;
	}

	public void setSetBy(String setBy) {
		this.setBy = setBy;
	}

}
