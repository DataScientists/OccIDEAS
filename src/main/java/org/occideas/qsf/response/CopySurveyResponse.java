package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class CopySurveyResponse extends BaseResponse{

    @JsonProperty(value = "result")
    private CopySurveyResult result;

    public CopySurveyResponse() {
    }

    public CopySurveyResponse(CopySurveyResult result, Meta meta) {
        this.result = result;
        this.meta = meta;
    }

    public CopySurveyResult getResult() {
        return result;
    }

    public void setResult(CopySurveyResult result) {
        this.result = result;
    }

}
