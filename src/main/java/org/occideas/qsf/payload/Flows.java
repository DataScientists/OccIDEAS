package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

import java.util.List;

public class Flows extends BaseQSF implements Payload{

    @JsonProperty(value = "Flow")
    private List<Flow> flowList;


    public List<Flow> getFlowList() {
        return flowList;
    }

    public void setFlowList(List<Flow> flowList) {
        this.flowList = flowList;
    }
}
