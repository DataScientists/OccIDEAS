package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DummyDisplayLogic {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("0")
    private DummyCondition condition;
    @JsonProperty(value = "Type")
    private String type;
    @JsonProperty(value = "inPage")
    private boolean inPage;


    public DummyDisplayLogic() {
    }

    public DummyDisplayLogic(String type, boolean inPage, DummyCondition condition) {
        this.type = type;
        this.inPage = inPage;
        this.condition = condition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isInPage() {
        return inPage;
    }

    public void setInPage(boolean inPage) {
        this.inPage = inPage;
    }

    public DummyCondition getCondition() {
        return condition;
    }

    public void setCondition(DummyCondition condition) {
        this.condition = condition;
    }

}
