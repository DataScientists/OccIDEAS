package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class BlockCreateResponse extends BaseResponse {

    @JsonProperty(value = "result")
    private BlockCreateResult result;

    public BlockCreateResult getResult() {
        return result;
    }

    public void setResult(BlockCreateResult result) {
        this.result = result;
    }
}
