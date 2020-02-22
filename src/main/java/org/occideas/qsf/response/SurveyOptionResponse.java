package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;
import org.occideas.qsf.payload.SurveyOptionPayload;

public class SurveyOptionResponse extends BaseResponse {

    private String requestId;
    @JsonProperty("result")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SurveyOptionPayload result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public SurveyOptionPayload getResult() {
        return result;
    }

    public void setResult(SurveyOptionPayload result) {
        this.result = result;
    }
}
