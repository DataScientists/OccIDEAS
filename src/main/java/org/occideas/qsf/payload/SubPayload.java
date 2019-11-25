package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

import java.util.List;

public class SubPayload extends BaseQSF implements Payload {

    @JsonProperty(value = "Flow")
    private List<Flow> flows;
    @JsonProperty(value = "FlowID")
    private String flowId;
    @JsonProperty(value = "Type")
    private String type;

    public SubPayload() {
    }

    public SubPayload(List<Flow> flows, String flowId, String type) {
        this.flows = flows;
        this.flowId = flowId;
        this.type = type;
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public void setFlows(List<Flow> flows) {
        this.flows = flows;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
