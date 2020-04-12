package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.occideas.qsf.BaseQSF;
import org.occideas.qsf.serializer.ConditionSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Condition extends BaseQSF implements Payload{

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Logic> logics;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Map<Integer,Logic> logic = new HashMap<>();
	@JsonProperty(value = "Type")
	private String type;

	@JsonValue
	@JsonSerialize(using = ConditionSerializer.class)
	public Condition getJsonValue(){
		ObjectMapper objectMapper = new ObjectMapper();
		return this;
	}

	public Condition() {
		super();
	}

	public Condition(List<Logic> logics, String type) {
		this.logics = logics;
		this.type = type;
	}

	public Condition(Map<Integer,Logic> logic, String type) {
		this.logic = logic;
		this.type = type;
	}

	public List<Logic> getLogics() {
		return logics;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setLogics(List<Logic> logics) {
		this.logics = logics;
	}

	public Map<Integer,Logic> getLogic() {
		return logic;
	}

	public void setLogic(Map<Integer,Logic> logic) {
		this.logic = logic;
	}
}
