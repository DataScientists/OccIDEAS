package org.occideas.qsf;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@JsonIgnoreProperties
public class ApplicationQSF {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "SurveyEntry")
    private SurveyEntry surveyEntry;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "SurveyElements")
    private List<SurveyElements> surveyElementsList;

    public ApplicationQSF() {
    }

    public ApplicationQSF(SurveyEntry surveyEntry, List<SurveyElements> surveyElementsList) {
        this.surveyEntry = surveyEntry;
        this.surveyElementsList = surveyElementsList;
    }

    public SurveyEntry getSurveyEntry() {
        return surveyEntry;
    }

    public void setSurveyEntry(SurveyEntry surveyEntry) {
        this.surveyEntry = surveyEntry;
    }

    public List<SurveyElements> getSurveyElementsList() {
        return surveyElementsList;
    }

    public void setSurveyElementsList(List<SurveyElements> surveyElementsList) {
        this.surveyElementsList = surveyElementsList;
    }

    @Override
    public String toString() {
        String results = null;
        try {
            results = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return results;
        //return "{"+this.surveyEntry+"}";
    }
}
