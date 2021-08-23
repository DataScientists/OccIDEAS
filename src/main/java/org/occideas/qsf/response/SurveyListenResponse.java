package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class SurveyListenResponse extends BaseResponse {

    @JsonProperty("result")
    private SurveyListenResult surveyListenResult;

    public SurveyListenResult getSurveyListenResult() {
        return surveyListenResult;
    }

    public void setSurveyListenResult(SurveyListenResult surveyListenResult) {
        this.surveyListenResult = surveyListenResult;
    }

    @Override
    public String toString() {
        return "SurveyListenResponse{" +
                "surveyListenResult=" + surveyListenResult +
                '}';
    }
}
