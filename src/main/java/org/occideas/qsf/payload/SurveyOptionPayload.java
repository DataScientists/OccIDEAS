package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.occideas.qsf.BaseQSF;
import org.occideas.qsf.serializer.AvailableLanguageSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SurveyOptionPayload extends BaseQSF implements Payload{

    @JsonProperty(value = "BackButton")
    private boolean backButton;
    @JsonProperty(value = "SaveAndContinue")
    private boolean saveAndContinue;
    @JsonProperty(value = "SurveyProtection")
    private String surveyProtection;
    @JsonProperty(value = "BallotBoxStuffingPrevention")
    private String ballotBoxStuffingPrevention;
    @JsonProperty(value = "NoIndex")
    private String noIndex;
    @JsonProperty(value = "SecureResponseFiles")
    private String secureResponseFiles;
    @JsonProperty(value = "SurveyExpiration")
    private String surveyExpiration;
    @JsonProperty(value = "SurveyTermination")
    private String surveyTermination;
    @JsonProperty(value = "EOSRedirectURL")
    private String eosRedirectURL;
    @JsonProperty(value = "SurveyTitle")
    private String surveyTitle;
    @JsonProperty(value = "Header")
    private String header;
    @JsonProperty(value = "Footer")
    private String footer;
    @JsonProperty(value = "ProgressBarDisplay")
    private String progressBarDisplay;
    @JsonProperty(value = "PartialData")
    private String partialData;
    @JsonProperty(value = "ValidationMessage")
    private String validationMessage;
    @JsonProperty(value = "PreviousButton")
    private String previousButton;
    @JsonProperty(value = "NextButton")
    private String nextButton;
    @JsonProperty(value = "SkinLibrary")
    private String skinLibrary;
    @JsonProperty(value = "SkinType")
    private String skinType;
    @JsonProperty(value = "Skin")
    private String skin;
    @JsonProperty(value = "NewScoring")
    private int newScoring;
    @JsonProperty(value = "AvailableLanguages")
    @JsonSerialize(using = AvailableLanguageSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AvailableLanguage availableLanguages;

    public SurveyOptionPayload() {
    }

    public SurveyOptionPayload(boolean backButton, boolean saveAndContinue, String surveyProtection, String ballotBoxStuffingPrevention, String noIndex, String secureResponseFiles, String surveyExpiration, String surveyTermination, String header, String footer, String progressBarDisplay, String partialData, String validationMessage, String previousButton, String nextButton, String skinLibrary, String skinType, String skin, int newScoring) {
        this.backButton = backButton;
        this.saveAndContinue = saveAndContinue;
        this.surveyProtection = surveyProtection;
        this.ballotBoxStuffingPrevention = ballotBoxStuffingPrevention;
        this.noIndex = noIndex;
        this.secureResponseFiles = secureResponseFiles;
        this.surveyExpiration = surveyExpiration;
        this.surveyTermination = surveyTermination;
        this.header = header;
        this.footer = footer;
        this.progressBarDisplay = progressBarDisplay;
        this.partialData = partialData;
        this.validationMessage = validationMessage;
        this.previousButton = previousButton;
        this.nextButton = nextButton;
        this.skinLibrary = skinLibrary;
        this.skinType = skinType;
        this.skin = skin;
        this.newScoring = newScoring;
    }

    public boolean isBackButton() {
        return backButton;
    }

    public void setBackButton(boolean backButton) {
        this.backButton = backButton;
    }

    public boolean isSaveAndContinue() {
        return saveAndContinue;
    }

    public void setSaveAndContinue(boolean saveAndContinue) {
        this.saveAndContinue = saveAndContinue;
    }

    public String getSurveyProtection() {
        return surveyProtection;
    }

    public void setSurveyProtection(String surveyProtection) {
        this.surveyProtection = surveyProtection;
    }

    public String getBallotBoxStuffingPrevention() {
        return ballotBoxStuffingPrevention;
    }

    public void setBallotBoxStuffingPrevention(String ballotBoxStuffingPrevention) {
        this.ballotBoxStuffingPrevention = ballotBoxStuffingPrevention;
    }

    public String getNoIndex() {
        return noIndex;
    }

    public void setNoIndex(String noIndex) {
        this.noIndex = noIndex;
    }

    public String getSecureResponseFiles() {
        return secureResponseFiles;
    }

    public void setSecureResponseFiles(String secureResponseFiles) {
        this.secureResponseFiles = secureResponseFiles;
    }

    public String getSurveyExpiration() {
        return surveyExpiration;
    }

    public void setSurveyExpiration(String surveyExpiration) {
        this.surveyExpiration = surveyExpiration;
    }

    public String getSurveyTermination() {
        return surveyTermination;
    }

    public void setSurveyTermination(String surveyTermination) {
        this.surveyTermination = surveyTermination;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getProgressBarDisplay() {
        return progressBarDisplay;
    }

    public void setProgressBarDisplay(String progressBarDisplay) {
        this.progressBarDisplay = progressBarDisplay;
    }

    public String getPartialData() {
        return partialData;
    }

    public void setPartialData(String partialData) {
        this.partialData = partialData;
    }

    public String getPreviousButton() {
        return previousButton;
    }

    public void setPreviousButton(String previousButton) {
        this.previousButton = previousButton;
    }

    public String getNextButton() {
        return nextButton;
    }

    public void setNextButton(String nextButton) {
        this.nextButton = nextButton;
    }

    public String getSkinLibrary() {
        return skinLibrary;
    }

    public void setSkinLibrary(String skinLibrary) {
        this.skinLibrary = skinLibrary;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public int getNewScoring() {
        return newScoring;
    }

    public void setNewScoring(int newScoring) {
        this.newScoring = newScoring;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public AvailableLanguage getAvailableLanguages() {
        return availableLanguages;
    }

    public void setAvailableLanguages(AvailableLanguage availableLanguages) {
        this.availableLanguages = availableLanguages;
    }

    public String getEosRedirectURL() {
        return eosRedirectURL;
    }

    public void setEosRedirectURL(String eosRedirectURL) {
        this.eosRedirectURL = eosRedirectURL;
    }

}
