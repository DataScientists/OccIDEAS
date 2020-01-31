package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class GetBlockElementResponse extends BaseResponse {

    @JsonProperty(value = "result")
    private GetBlockElementResult result;

    public GetBlockElementResult getResult() {
        return result;
    }

    public void setResult(GetBlockElementResult result) {
        this.result = result;
    }
}
