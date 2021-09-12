package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseResponse;

public class SurveyQuestionsResponse extends BaseResponse {

    @JsonProperty(value = "result")
    private SurveyQuestionsResults surveyQuestionsResults;

    public SurveyQuestionsResults getSurveyQuestionsResults() {
        return surveyQuestionsResults;
    }

    public void setSurveyQuestionsResults(SurveyQuestionsResults surveyQuestionsResults) {
        this.surveyQuestionsResults = surveyQuestionsResults;
    }


}
