package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SurveyCreateResult extends Result {

    @JsonProperty(value = "id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonProperty(value = "SurveyID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String surveyId;

    @JsonProperty(value = "DefaultBlockID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String defaultBlockId;

    @JsonProperty(value = "QuestionID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String questionId;

    public SurveyCreateResult() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getDefaultBlockId() {
        return defaultBlockId;
    }

    public void setDefaultBlockId(String defaultBlockId) {
        this.defaultBlockId = defaultBlockId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
