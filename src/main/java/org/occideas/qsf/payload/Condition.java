package org.occideas.qsf.payload;

import org.occideas.qsf.BaseQSF;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Condition extends BaseQSF implements Payload{

	@JsonProperty(value = "0")
	private Logic logic;
	@JsonProperty(value = "Type")
	private String type;

	public Condition() {
		super();
	}

	public Condition(Logic logic,String type) {
		super();
		this.type = type;
		this.logic = logic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Logic getLogic() {
		return logic;
	}

	public void setLogic(Logic logic) {
		this.logic = logic;
	}

}
