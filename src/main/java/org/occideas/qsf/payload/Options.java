package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Options extends BaseQSF {

    @JsonProperty(value = "Advanced")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String advanced;
    @JsonProperty(value = "SurveyTermination")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String surveyTermination;
    @JsonProperty(value = "EOSRedirectURL")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String redirectUrl;
    @JsonProperty(value = "BlockLocking")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String blockLocking;
    @JsonProperty(value = "RandomizeQuestions")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String randomizeQuestions;
    @JsonProperty(value = "BlockVisibility")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String blockVisibility;

    public Options() {
    }

    public Options(String advanced, String surveyTermination, String redirectUrl) {
        this.advanced = advanced;
        this.surveyTermination = surveyTermination;
        this.redirectUrl = redirectUrl;
    }

    public String getAdvanced() {
        return advanced;
    }

    public void setAdvanced(String advanced) {
        this.advanced = advanced;
    }

    public String getSurveyTermination() {
        return surveyTermination;
    }

    public void setSurveyTermination(String surveyTermination) {
        this.surveyTermination = surveyTermination;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getBlockLocking() {
        return blockLocking;
    }

    public void setBlockLocking(String blockLocking) {
        this.blockLocking = blockLocking;
    }

    public String getRandomizeQuestions() {
        return randomizeQuestions;
    }

    public void setRandomizeQuestions(String randomizeQuestions) {
        this.randomizeQuestions = randomizeQuestions;
    }

    public String getBlockVisibility() {
        return blockVisibility;
    }

    public void setBlockVisibility(String blockVisibility) {
        this.blockVisibility = blockVisibility;
    }
}
