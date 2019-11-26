package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class Flow extends BaseQSF {

    @JsonProperty(value = "ID")
    private String id;
    @JsonProperty(value = "Type")
    private String type;
    @JsonProperty(value = "FlowID")
    private String flowId;

    public Flow() {
    }

    public Flow(String id, String type, String flowId) {
        this.id = id;
        this.type = type;
        this.flowId = flowId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }
}
