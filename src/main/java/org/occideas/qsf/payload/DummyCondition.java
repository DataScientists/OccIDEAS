package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DummyCondition {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Logic> logics;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "0")
    private Logic logic;
    @JsonProperty(value = "Type")
    private String type;

    public DummyCondition() {
        super();
    }

    public DummyCondition(List<Logic> logics, String type) {
        this.logics = logics;
        this.type = type;
    }

    public DummyCondition(Logic logic, String type) {
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

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

}
