package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class SurveyListResponse extends BaseResponse {

    @JsonProperty(value = "result")
    private SurveyListResult result;

    public SurveyListResult getResult() {
        return result;
    }

    public void setResult(SurveyListResult result) {
        this.result = result;
    }
}
