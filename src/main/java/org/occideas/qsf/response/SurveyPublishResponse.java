package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class SurveyPublishResponse extends BaseResponse {

    @JsonProperty(value = "result")
    private SurveyPublishResult result;

    public SurveyPublishResponse() {
    }

    public SurveyPublishResult getResult() {
        return result;
    }

    public void setResult(SurveyPublishResult result) {
        this.result = result;
    }
}
