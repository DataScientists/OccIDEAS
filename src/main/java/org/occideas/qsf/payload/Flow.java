package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.occideas.qsf.BaseQSF;
import org.occideas.qsf.serializer.BranchLogicSerializer;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flow extends BaseQSF {

    @JsonProperty(value = "ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonProperty(value = "Type")
    private String type;
    @JsonProperty(value = "FlowID")
    private String flowId;
    @JsonProperty(value = "BranchLogic")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = BranchLogicSerializer.class)
    private BranchLogic branchLogic;
    @JsonProperty(value = "Flow")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Flow> flows;
    @JsonProperty(value = "EndingType")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String endingType;
    @JsonProperty(value = "Options")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Options options;
    @JsonProperty(value = "Description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    public Flow() {
    }

    public Flow(String id, String type, String flowId) {
        this.id = id;
        this.type = type;
        this.flowId = flowId;
    }

    public Flow(String id, String type, String flowId, BranchLogic branchLogic, List<Flow> flows, String endingType, Options options,
                String description) {
        this.id = id;
        this.type = type;
        this.flowId = flowId;
        this.branchLogic = branchLogic;
        this.flows = flows;
        this.endingType = endingType;
        this.options = options;
        this.description = description;
    }

    public Flow(String description) {
        this.description = description;
    }

    public BranchLogic getBranchLogic() {
        return branchLogic;
    }

    public void setBranchLogic(BranchLogic branchLogic) {
        this.branchLogic = branchLogic;
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

    public List<Flow> getFlows() {
        return flows;
    }

    public void setFlows(List<Flow> flows) {
        this.flows = flows;
    }

    public String getEndingType() {
        return endingType;
    }

    public void setEndingType(String endingType) {
        this.endingType = endingType;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flow flow = (Flow) o;
        return description.equals(flow.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
