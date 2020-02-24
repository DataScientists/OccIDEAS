package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class SurveyExportResponse extends BaseResponse {

    @JsonProperty(value = "result")
    private SurveyExportResponseResult result;

    public SurveyExportResponse() {
    }

    public SurveyExportResponse(SurveyExportResponseResult result, Meta meta) {
        this.result = result;
        this.meta = meta;
    }

    public SurveyExportResponseResult getResult() {
        return result;
    }

    public void setResult(SurveyExportResponseResult result) {
        this.result = result;
    }

}
