package org.occideas.qsf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SurveyElement extends BaseQSF{

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
    private Object payload;

    public SurveyElement() {
    }

    public SurveyElement(String surveyID, String element, String primaryAttribute, String secondaryAttribute, String tertiaryAttribute, Object payload) {
        this.surveyID = surveyID;
        this.element = element;
        this.primaryAttribute = primaryAttribute;
        this.secondaryAttribute = secondaryAttribute;
        this.tertiaryAttribute = tertiaryAttribute;
        this.payload = payload;
    }

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

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
