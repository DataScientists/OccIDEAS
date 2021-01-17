package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionSettings {

    @JsonProperty(value = "CaseMode")
    private String caseMode;

    @JsonProperty(value = "DisplayOrder")
    private String displayOrder;

    @JsonProperty(value = "AnswerRequired")
    private String answerRequired;

    @JsonProperty(value = "EnableMobileRendering")
    private Boolean enableMobileRendering;

    public QuestionSettings() {
        this.answerRequired = "No";
    }

    public String getCaseMode() {
        return caseMode;
    }

    public void setCaseMode(String caseMode) {
        this.caseMode = caseMode;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getAnswerRequired() {
        return answerRequired;
    }

    public void setAnswerRequired(String answerRequired) {
        this.answerRequired = answerRequired;
    }
}
