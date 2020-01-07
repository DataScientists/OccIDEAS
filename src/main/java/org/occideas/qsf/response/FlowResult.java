package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;
import org.occideas.qsf.payload.Flow;
import org.occideas.qsf.payload.Properties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowResult extends BaseQSF {

    @JsonProperty(value = "Flow")
    private List<Flow> flows;
    @JsonProperty(value = "FlowID")
    private String flowId;
    @JsonProperty(value = "Properties")
    private Properties properties;
    @JsonProperty(value = "Type")
    private String type;

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

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
