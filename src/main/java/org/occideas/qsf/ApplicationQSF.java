package org.occideas.qsf;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties
public class ApplicationQSF extends BaseQSF{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "SurveyEntry")
    private SurveyEntry surveyEntry;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "SurveyElements")
    private List<SurveyElement> surveyElementsList;

    public ApplicationQSF() {
    }

    public ApplicationQSF(SurveyEntry surveyEntry, List<SurveyElement> surveyElementsList) {
        this.surveyEntry = surveyEntry;
        this.surveyElementsList = surveyElementsList;
    }

    public SurveyEntry getSurveyEntry() {
        return surveyEntry;
    }

    public void setSurveyEntry(SurveyEntry surveyEntry) {
        this.surveyEntry = surveyEntry;
    }

    public List<SurveyElement> getSurveyElementsList() {
        return surveyElementsList;
    }

    public void setSurveyElementsList(List<SurveyElement> surveyElementsList) {
        this.surveyElementsList = surveyElementsList;
    }

}
