package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class SurveyCreateResponse extends BaseQSF {

    @JsonProperty(value = "result")
    private Result result;
    @JsonProperty(value = "meta")
    private Meta meta;

    public SurveyCreateResponse() {
    }

    public SurveyCreateResponse(Result result, Meta meta) {
        this.result = result;
        this.meta = meta;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
