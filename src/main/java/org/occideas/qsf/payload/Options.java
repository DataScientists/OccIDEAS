package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class Options extends BaseQSF {

    @JsonProperty(value = "Advanced")
    private String advanced;
    @JsonProperty(value = "SurveyTermination")
    private String surveyTermination;
    @JsonProperty(value = "EOSRedirectURL")
    private String redirectUrl;

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
}
