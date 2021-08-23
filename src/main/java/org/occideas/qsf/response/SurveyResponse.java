package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class SurveyResponse extends BaseResponse {

    @JsonProperty(value = "result")
    private Response result;

    public Response getResult() {
        return result;
    }

    public void setResult(Response result) {
        this.result = result;
    }
}
