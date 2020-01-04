package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class SurveyCreateResponse extends BaseResponse {

    @JsonProperty(value = "result")
    private SurveyCreateResult result;

    public SurveyCreateResponse() {
    }

    public SurveyCreateResponse(SurveyCreateResult result, Meta meta) {
        this.result = result;
        this.meta = meta;
    }

    public SurveyCreateResult getResult() {
        return result;
    }

    public void setResult(SurveyCreateResult result) {
        this.result = result;
    }

}
