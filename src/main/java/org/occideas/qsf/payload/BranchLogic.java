package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

import java.util.List;

public class BranchLogic extends BaseQSF {

    private List<Condition> logic;
    @JsonProperty(value = "Type")
    private String type;

    public BranchLogic() {
    }

    public BranchLogic(List<Condition> logic, String type) {
        this.logic = logic;
        this.type = type;
    }

    public List<Condition> getLogic() {
        return logic;
    }

    public void setLogic(List<Condition> logic) {
        this.logic = logic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
