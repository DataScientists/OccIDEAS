package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockCreateResult  extends Result{

    @JsonProperty("BlockID")
    private String blockId;
    @JsonProperty("FlowID")
    private String flowId;

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }
}
