package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveySettings {

    @JsonProperty(value = "DISABLE_SMART_PHONE_MOBILE_SUPPORT")
    private boolean disableSmartPhoneMobileSupport;

    @JsonProperty(value = "DISABLE_SMART_PHONE_GRID_MOBILE_RENDERING")
    private boolean disableSmartPhoneGridMobileRendering;

    @JsonProperty(value = "DISABLE_TABLET_MOBILE_SUPPORT")
    private boolean disableTabletMobileSupport;

    @JsonProperty(value = "DISABLE_TABLET_GRID_MOBILE_RENDERING")
    private boolean disableTabletGridMobileRendering;

    @JsonProperty(value = "AUTOSYNCHRONIZEPANELDATA")
    private boolean autosynchronizepaneldata;

    @JsonProperty(value = "COLLECT_RESPONDENT_GEO_LOCATION")
    private boolean c1ollectRespondentGeoLocation;

    @JsonProperty(value = "EXECUTE_ADVANCED_ACTIONS_ON_RESUME")
    private boolean executeAdvancedActionsOnResume = true;

    @JsonProperty(value = "QUOTAS_ACTIVE")
    private boolean quotasActive;

    @JsonProperty(value = "QUOTAS_IN_PERCENT")
    private boolean quotasInPercent;

    @JsonProperty(value = "CLOSED_STRATA_RULE")
    private String closedStrataRule = "AllowInterviews";

    @JsonProperty(value = "QUOTAS_SYNCHRONIZE")
    private boolean quotasSynchronize;

    @JsonProperty(value = "INPUT_REQUIRED")
    private boolean inputRequired = true;

    @JsonProperty(value = "ALLOW_BACKWARD_JUMPS")
    private boolean allowBackwardJumps = true;

    @JsonProperty(value = "AUTO_DATA_CLEANING")
    private boolean autoDataCleaning = true;

    @JsonProperty(value = "CLEAN_QUESTION_PRELOAD")
    private boolean cleanQuestionPreload = true;

    @JsonProperty(value = "ALLOW_OPEN_END_LOWER_CASE")
    private boolean allowOpenEndLowerCase = true;

    @JsonProperty(value = "QUIT_BUTTON_ACTION")
    private String quitButtonAction = "ExitSurvey";

    @JsonProperty(value = "TERMINATION_ACTION")
    private String terminationAction = "DisplayMessage";

    @JsonProperty(value = "QUOTA_ACTION")
    private String quotaAction = "DisplayMessageAndExit";

    @JsonProperty(value = "COMPLETED_ACTION")
    private String completedAction = "None";

    @JsonProperty(value = "COMPLETED_ACTION_REDIRECT_URL")
    private String completedActionRedirectUrl;

    @JsonProperty(value = "INTERRUPTED_ACTION")
    private String interruptedAction = "None";

    @JsonProperty(value = "SCREENEDOUT_ACTION")
    private String screenedoutAction = "None";

    @JsonProperty(value = "INACTIVE_SURVEY_ACTION")
    private String inactiveSurveyAction = "DisplayMessage";

    @JsonProperty(value = "SURVEY_REACCESS_ACTION")
    private String surveyReaccessAction = "DisplayMessage";

    @JsonProperty(value = "SURVEY_ACCESS_MODE")
    private String surveyAccessMode = "OpenAccess";

    @JsonProperty(value = "AUTO_GENERATE_PIN")
    private boolean autoGeneratePin;

    @JsonProperty(value = "ALLOW_SAVE_AND_CONTINUE")
    private String allowSaveAndContinue = "Always";

    @JsonProperty(value = "PREVENT_SURVEY_INDEXING")
    private boolean preventSurveyIndexing;

    @JsonProperty(value = "SESSION_TIMEOUT")
    private int sessionTimeout = 30;

    @JsonProperty(value = "ALLOW_CASE_CREATION_ON_MOBILE_DEVICES")
    private boolean allowCaseCreationOnMobileDevices = true;

    @JsonProperty(value = "AUTO_ADVANCE_MODE")
    private String autoAdvanceMode = "ByBlock";

    @JsonProperty(value = "ENABLE_LEGACY_BROWSER_WARNING")
    private boolean enableLegacyBrowserWarning;

    public SurveySettings() {

    }

    public SurveySettings(String completedActionRedirectUrl) {
        this.completedAction = "RedirectToUrl";
        this.completedActionRedirectUrl = completedActionRedirectUrl;
    }

    public boolean isDisableSmartPhoneMobileSupport() {
        return disableSmartPhoneMobileSupport;
    }

    public void setDisableSmartPhoneMobileSupport(boolean disableSmartPhoneMobileSupport) {
        this.disableSmartPhoneMobileSupport = disableSmartPhoneMobileSupport;
    }

    public boolean isDisableSmartPhoneGridMobileRendering() {
        return disableSmartPhoneGridMobileRendering;
    }

    public void setDisableSmartPhoneGridMobileRendering(boolean disableSmartPhoneGridMobileRendering) {
        this.disableSmartPhoneGridMobileRendering = disableSmartPhoneGridMobileRendering;
    }

    public boolean isDisableTabletMobileSupport() {
        return disableTabletMobileSupport;
    }

    public void setDisableTabletMobileSupport(boolean disableTabletMobileSupport) {
        this.disableTabletMobileSupport = disableTabletMobileSupport;
    }

    public boolean isDisableTabletGridMobileRendering() {
        return disableTabletGridMobileRendering;
    }

    public void setDisableTabletGridMobileRendering(boolean disableTabletGridMobileRendering) {
        this.disableTabletGridMobileRendering = disableTabletGridMobileRendering;
    }

    public boolean isAutosynchronizepaneldata() {
        return autosynchronizepaneldata;
    }

    public void setAutosynchronizepaneldata(boolean autosynchronizepaneldata) {
        this.autosynchronizepaneldata = autosynchronizepaneldata;
    }

    public boolean isC1ollectRespondentGeoLocation() {
        return c1ollectRespondentGeoLocation;
    }

    public void setC1ollectRespondentGeoLocation(boolean c1ollectRespondentGeoLocation) {
        this.c1ollectRespondentGeoLocation = c1ollectRespondentGeoLocation;
    }

    public boolean isExecuteAdvancedActionsOnResume() {
        return executeAdvancedActionsOnResume;
    }

    public void setExecuteAdvancedActionsOnResume(boolean executeAdvancedActionsOnResume) {
        this.executeAdvancedActionsOnResume = executeAdvancedActionsOnResume;
    }

    public boolean isQuotasActive() {
        return quotasActive;
    }

    public void setQuotasActive(boolean quotasActive) {
        this.quotasActive = quotasActive;
    }

    public boolean isQuotasInPercent() {
        return quotasInPercent;
    }

    public void setQuotasInPercent(boolean quotasInPercent) {
        this.quotasInPercent = quotasInPercent;
    }

    public String getClosedStrataRule() {
        return closedStrataRule;
    }

    public void setClosedStrataRule(String closedStrataRule) {
        this.closedStrataRule = closedStrataRule;
    }

    public boolean isQuotasSynchronize() {
        return quotasSynchronize;
    }

    public void setQuotasSynchronize(boolean quotasSynchronize) {
        this.quotasSynchronize = quotasSynchronize;
    }

    public boolean isInputRequired() {
        return inputRequired;
    }

    public void setInputRequired(boolean inputRequired) {
        this.inputRequired = inputRequired;
    }

    public boolean isAllowBackwardJumps() {
        return allowBackwardJumps;
    }

    public void setAllowBackwardJumps(boolean allowBackwardJumps) {
        this.allowBackwardJumps = allowBackwardJumps;
    }

    public boolean isAutoDataCleaning() {
        return autoDataCleaning;
    }

    public void setAutoDataCleaning(boolean autoDataCleaning) {
        this.autoDataCleaning = autoDataCleaning;
    }

    public boolean isCleanQuestionPreload() {
        return cleanQuestionPreload;
    }

    public void setCleanQuestionPreload(boolean cleanQuestionPreload) {
        this.cleanQuestionPreload = cleanQuestionPreload;
    }

    public boolean isAllowOpenEndLowerCase() {
        return allowOpenEndLowerCase;
    }

    public void setAllowOpenEndLowerCase(boolean allowOpenEndLowerCase) {
        this.allowOpenEndLowerCase = allowOpenEndLowerCase;
    }

    public String getQuitButtonAction() {
        return quitButtonAction;
    }

    public void setQuitButtonAction(String quitButtonAction) {
        this.quitButtonAction = quitButtonAction;
    }

    public String getTerminationAction() {
        return terminationAction;
    }

    public void setTerminationAction(String terminationAction) {
        this.terminationAction = terminationAction;
    }

    public String getQuotaAction() {
        return quotaAction;
    }

    public void setQuotaAction(String quotaAction) {
        this.quotaAction = quotaAction;
    }

    public String getCompletedAction() {
        return completedAction;
    }

    public void setCompletedAction(String completedAction) {
        this.completedAction = completedAction;
    }

    public String getCompletedActionRedirectUrl() {
        return completedActionRedirectUrl;
    }

    public void setCompletedActionRedirectUrl(String completedActionRedirectUrl) {
        this.completedActionRedirectUrl = completedActionRedirectUrl;
    }

    public String getInterruptedAction() {
        return interruptedAction;
    }

    public void setInterruptedAction(String interruptedAction) {
        this.interruptedAction = interruptedAction;
    }

    public String getScreenedoutAction() {
        return screenedoutAction;
    }

    public void setScreenedoutAction(String screenedoutAction) {
        this.screenedoutAction = screenedoutAction;
    }

    public String getInactiveSurveyAction() {
        return inactiveSurveyAction;
    }

    public void setInactiveSurveyAction(String inactiveSurveyAction) {
        this.inactiveSurveyAction = inactiveSurveyAction;
    }

    public String getSurveyReaccessAction() {
        return surveyReaccessAction;
    }

    public void setSurveyReaccessAction(String surveyReaccessAction) {
        this.surveyReaccessAction = surveyReaccessAction;
    }

    public String getSurveyAccessMode() {
        return surveyAccessMode;
    }

    public void setSurveyAccessMode(String surveyAccessMode) {
        this.surveyAccessMode = surveyAccessMode;
    }

    public boolean isAutoGeneratePin() {
        return autoGeneratePin;
    }

    public void setAutoGeneratePin(boolean autoGeneratePin) {
        this.autoGeneratePin = autoGeneratePin;
    }

    public String getAllowSaveAndContinue() {
        return allowSaveAndContinue;
    }

    public void setAllowSaveAndContinue(String allowSaveAndContinue) {
        this.allowSaveAndContinue = allowSaveAndContinue;
    }

    public boolean isPreventSurveyIndexing() {
        return preventSurveyIndexing;
    }

    public void setPreventSurveyIndexing(boolean preventSurveyIndexing) {
        this.preventSurveyIndexing = preventSurveyIndexing;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public boolean isAllowCaseCreationOnMobileDevices() {
        return allowCaseCreationOnMobileDevices;
    }

    public void setAllowCaseCreationOnMobileDevices(boolean allowCaseCreationOnMobileDevices) {
        this.allowCaseCreationOnMobileDevices = allowCaseCreationOnMobileDevices;
    }

    public String getAutoAdvanceMode() {
        return autoAdvanceMode;
    }

    public void setAutoAdvanceMode(String autoAdvanceMode) {
        this.autoAdvanceMode = autoAdvanceMode;
    }

    public boolean isEnableLegacyBrowserWarning() {
        return enableLegacyBrowserWarning;
    }

    public void setEnableLegacyBrowserWarning(boolean enableLegacyBrowserWarning) {
        this.enableLegacyBrowserWarning = enableLegacyBrowserWarning;
    }
}
