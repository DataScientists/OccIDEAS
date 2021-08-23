package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class DistributionListResponse extends BaseResponse {

    @JsonProperty(value = "result")
    private DistributionListResult result;

    public DistributionListResult getResult() {
        return result;
    }

    public void setResult(DistributionListResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "DistributionListResponse{" +
                "result=" + result +
                '}';
    }
}
