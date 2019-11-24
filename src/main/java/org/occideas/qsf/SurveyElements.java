package org.occideas.qsf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SurveyElements {

    @JsonProperty(value = "SurveyID")
    private String surveyID;
    @JsonProperty(value = "Element")
    private String element;
    @JsonProperty(value = "PrimaryAttribute")
    private String primaryAttribute;
    @JsonProperty(value = "SecondaryAttribute")
    private String secondaryAttribute;
    @JsonProperty(value = "TertiaryAttribute")
    private String tertiaryAttribute;
    @JsonProperty(value = "Payload")
    private List<Payload> payload;

    public String getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(String surveyID) {
        this.surveyID = surveyID;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getPrimaryAttribute() {
        return primaryAttribute;
    }

    public void setPrimaryAttribute(String primaryAttribute) {
        this.primaryAttribute = primaryAttribute;
    }

    public String getSecondaryAttribute() {
        return secondaryAttribute;
    }

    public void setSecondaryAttribute(String secondaryAttribute) {
        this.secondaryAttribute = secondaryAttribute;
    }

    public String getTertiaryAttribute() {
        return tertiaryAttribute;
    }

    public void setTertiaryAttribute(String tertiaryAttribute) {
        this.tertiaryAttribute = tertiaryAttribute;
    }

    public List<Payload> getPayload() {
        return payload;
    }

    public void setPayload(List<Payload> payload) {
        this.payload = payload;
    }
}
