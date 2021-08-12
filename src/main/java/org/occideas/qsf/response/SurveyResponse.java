package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class SurveyResponse extends BaseResponse {

    @JsonProperty(value = "result")
    private SurveyResult result;

    public SurveyResult getResult() {
        return result;
    }

    public void setResult(SurveyResult result) {
        this.result = result;
    }
}
