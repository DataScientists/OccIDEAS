package org.occideas.qsf.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class SurveyCreateRequest extends BaseQSF {

    @JsonProperty(value = "SurveyName")
    private String surveyName;
    @JsonProperty(value = "Language")
    private String language;
    @JsonProperty(value = "ProjectCategory")
    private String projectCategory;

    public SurveyCreateRequest() {
    }

    public SurveyCreateRequest(String surveyName, String language, String projectCategory) {
        this.surveyName = surveyName;
        this.language = language;
        this.projectCategory = projectCategory;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }
}
