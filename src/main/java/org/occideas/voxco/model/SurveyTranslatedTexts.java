package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SurveyTranslatedTexts {

    @JsonProperty(value = "ClosedStrata")
    private TranslatedTextContent closedStrata;

    @JsonProperty(value = "CompletedTerminationMessage")
    private TranslatedTextContent completedTerminationMessage;

    @JsonProperty(value = "FullQuota")
    private TranslatedTextContent fullQuota;

    @JsonProperty(value = "InactiveSurveyMessage")
    private TranslatedTextContent inactiveSurveyMessage;

    @JsonProperty(value = "IncompatibleChoices")
    private TranslatedTextContent incompatibleChoices;

    @JsonProperty(value = "IncompleteAnswer")
    private TranslatedTextContent incompleteAnswer;

    @JsonProperty(value = "InterruptedTerminationMessage")
    private TranslatedTextContent interruptedTerminationMessage;

    @JsonProperty(value = "InvalidAnswer")
    private TranslatedTextContent invalidAnswer;

    @JsonProperty(value = "InvalidCharactersMax")
    private TranslatedTextContent invalidCharactersMax;

    @JsonProperty(value = "InvalidCharactersMin")
    private TranslatedTextContent invalidCharactersMin;

    @JsonProperty(value = "InvalidChoicesMax")
    private TranslatedTextContent invalidChoicesMax;

    @JsonProperty(value = "InvalidChoicesMin")
    private TranslatedTextContent invalidChoicesMin;

    @JsonProperty(value = "InvalidCreditCard")
    private TranslatedTextContent invalidCreditCard;

    @JsonProperty(value = "InvalidDate")
    private TranslatedTextContent invalidDate;

    @JsonProperty(value = "InvalidEmailAddress")
    private TranslatedTextContent invalidEmailAddress;

    @JsonProperty(value = "InvalidFileSize")
    private TranslatedTextContent invalidFileSize;

    @JsonProperty(value = "InvalidFileType")
    private TranslatedTextContent invalidFileType;

    @JsonProperty(value = "InvalidNumber")
    private TranslatedTextContent invalidNumber;

    @JsonProperty(value = "InvalidRankingValue")
    private TranslatedTextContent invalidRankingValue;

    @JsonProperty(value = "InvalidRecordNumber")
    private TranslatedTextContent invalidRecordNumber;

    @JsonProperty(value = "InvalidTextFormat")
    private TranslatedTextContent invalidTextFormat;

    @JsonProperty(value = "InvalidTimeForDate")
    private TranslatedTextContent invalidTimeForDate;

    @JsonProperty(value = "LegacyBrowserWarning")
    private TranslatedTextContent legacyBrowserWarning;

    @JsonProperty(value = "LockedRecord")
    private TranslatedTextContent lockedRecord;

    @JsonProperty(value = "LowerBoundNotFulfilled")
    private TranslatedTextContent lowerBoundNotFulfilled;

    @JsonProperty(value = "MobileDeviceMessage")
    private TranslatedTextContent mobileDeviceMessage;

    @JsonProperty(value = "OutOfBounds")
    private TranslatedTextContent outOfBounds;

    @JsonProperty(value = "PageHasErrors")
    private TranslatedTextContent pageHasErrors;

    @JsonProperty(value = "PreviewModeMessage")
    private TranslatedTextContent previewModeMessage;

    @JsonProperty(value = "QuotaMetClosedMessage")
    private TranslatedTextContent quotaMetClosedMessage;

    @JsonProperty(value = "RespondentAccessExpired")
    private TranslatedTextContent respondentAccessExpired;

    @JsonProperty(value = "ScreenedOutTerminationMessage")
    private TranslatedTextContent screenedOutTerminationMessage;

    @JsonProperty(value = "SurveyLoadingMessage")
    private TranslatedTextContent surveyLoadingMessage;

    @JsonProperty(value = "SurveyReaccessMessage")
    private TranslatedTextContent surveyReaccessMessage;

    @JsonProperty(value = "TabletDeviceMessage")
    private TranslatedTextContent tabletDeviceMessage;

    @JsonProperty(value = "TerminationMessage")
    private TranslatedTextContent terminationMessage;

    @JsonProperty(value = "TimerTerminationMessage")
    private TranslatedTextContent timerTerminationMessage;

    @JsonProperty(value = "UnavailableDate")
    private TranslatedTextContent unavailableDate;

    @JsonProperty(value = "UnknownDeviceMessage")
    private TranslatedTextContent unknownDeviceMessage;

    @JsonProperty(value = "UnsupportedDeviceMessage")
    private TranslatedTextContent unsupportedDeviceMessage;

    @JsonProperty(value = "UpperBoundNotFulfilled")
    private TranslatedTextContent upperBoundNotFulfilled;

    @JsonProperty(value = "UnexpectedError")
    private TranslatedTextContent unexpectedError;

    public SurveyTranslatedTexts() {
        this.closedStrata = new TranslatedTextContent("Survey is currently closed");
        this.completedTerminationMessage = new TranslatedTextContent("You have successfully completed this survey. We thank you for your participation.");
        this.fullQuota = new TranslatedTextContent("Objective for this survey has been reached");
        this.inactiveSurveyMessage = new TranslatedTextContent("We are sorry but this survey is inactive.");
        this.incompatibleChoices = new TranslatedTextContent("Incompatible choices selected");
        this.incompleteAnswer = new TranslatedTextContent("Answer is incomplete");
        this.interruptedTerminationMessage = new TranslatedTextContent("The survey has been interrupted.");
        this.invalidAnswer = new TranslatedTextContent("Answer is invalid");
        this.invalidCharactersMax = new TranslatedTextContent("Please enter at most {max} characters");
        this.invalidCharactersMin = new TranslatedTextContent("Please enter at least {min} characters");
        this.invalidChoicesMax = new TranslatedTextContent("Please answer no more than {max} choices");
        this.invalidChoicesMin = new TranslatedTextContent("Please answer at least {min} choices");
        this.invalidCreditCard = new TranslatedTextContent("Credit card number is invalid");
        this.invalidDate = new TranslatedTextContent("Date is invalid");
        this.invalidEmailAddress = new TranslatedTextContent("Email address is invalid");
        this.invalidFileSize = new TranslatedTextContent("File size is invalid");
        this.invalidFileType = new TranslatedTextContent("File type is invalid");
        this.invalidNumber = new TranslatedTextContent("Number is invalid");
        this.invalidRankingValue = new TranslatedTextContent("Please assign a value from {min} to {max} for each item in your ranking. Values may not be repeated.");
        this.invalidRecordNumber = new TranslatedTextContent("Record number is invalid");
        this.invalidTextFormat = new TranslatedTextContent("Answer format is invalid");
        this.invalidTimeForDate = new TranslatedTextContent("The selected time is not available for this date");
        this.legacyBrowserWarning = new TranslatedTextContent("You have opened your survey in a legacy browser. We strongly recommend switching to a modern browser such as Chrome or Firefox for an optimal experience.");
        this.lockedRecord = new TranslatedTextContent("This record is currently locked by another user");
        this.lowerBoundNotFulfilled = new TranslatedTextContent("Answer value should be greater or equal to {min}");
        this.mobileDeviceMessage = new TranslatedTextContent("We are sorry but the device you are using is not supported.");
        this.outOfBounds = new TranslatedTextContent("Answer is out of bounds");
        this.pageHasErrors = new TranslatedTextContent("Page has errors");
        this.previewModeMessage = new TranslatedTextContent("No data is being recorded.");
        this.quotaMetClosedMessage = new TranslatedTextContent("We are sorry to inform you that this survey is currently closed. We thank you for your time.");
        this.respondentAccessExpired = new TranslatedTextContent("We are sorry but your access to this survey has expired.");
        this.screenedOutTerminationMessage = new TranslatedTextContent("We are sorry. You do not meet the criteria to complete this survey. We thank you for your time.");
        this.surveyLoadingMessage = new TranslatedTextContent("Please wait while your survey is being loaded. This page will refresh automatically and your survey will be displayed when ready.");
        this.surveyReaccessMessage = new TranslatedTextContent("We are sorry to inform you that you cannot re-access this survey.");
        this.tabletDeviceMessage = new TranslatedTextContent("We are sorry but the device you are using is not supported.");
        this.terminationMessage = new TranslatedTextContent("Thank you for your participation.");
        this.timerTerminationMessage = new TranslatedTextContent("The survey has timed out. We thank you for your participation.");
        this.unavailableDate = new TranslatedTextContent("Date is unavailable");
        this.unknownDeviceMessage = new TranslatedTextContent("We are sorry but the device you are using is not supported.");
        this.unsupportedDeviceMessage = new TranslatedTextContent("We are sorry but the device you are using is not supported.");
        this.upperBoundNotFulfilled = new TranslatedTextContent("Answer value should be smaller or equal to {max}");
        this.unexpectedError = new TranslatedTextContent("We are sorry, an unexpected error occurred.");
    }

    public TranslatedTextContent getClosedStrata() {
        return closedStrata;
    }

    public void setClosedStrata(TranslatedTextContent closedStrata) {
        this.closedStrata = closedStrata;
    }

    public TranslatedTextContent getCompletedTerminationMessage() {
        return completedTerminationMessage;
    }

    public void setCompletedTerminationMessage(TranslatedTextContent completedTerminationMessage) {
        this.completedTerminationMessage = completedTerminationMessage;
    }

    public TranslatedTextContent getFullQuota() {
        return fullQuota;
    }

    public void setFullQuota(TranslatedTextContent fullQuota) {
        this.fullQuota = fullQuota;
    }

    public TranslatedTextContent getInactiveSurveyMessage() {
        return inactiveSurveyMessage;
    }

    public void setInactiveSurveyMessage(TranslatedTextContent inactiveSurveyMessage) {
        this.inactiveSurveyMessage = inactiveSurveyMessage;
    }

    public TranslatedTextContent getIncompatibleChoices() {
        return incompatibleChoices;
    }

    public void setIncompatibleChoices(TranslatedTextContent incompatibleChoices) {
        this.incompatibleChoices = incompatibleChoices;
    }

    public TranslatedTextContent getIncompleteAnswer() {
        return incompleteAnswer;
    }

    public void setIncompleteAnswer(TranslatedTextContent incompleteAnswer) {
        this.incompleteAnswer = incompleteAnswer;
    }

    public TranslatedTextContent getInterruptedTerminationMessage() {
        return interruptedTerminationMessage;
    }

    public void setInterruptedTerminationMessage(TranslatedTextContent interruptedTerminationMessage) {
        this.interruptedTerminationMessage = interruptedTerminationMessage;
    }

    public TranslatedTextContent getInvalidAnswer() {
        return invalidAnswer;
    }

    public void setInvalidAnswer(TranslatedTextContent invalidAnswer) {
        this.invalidAnswer = invalidAnswer;
    }

    public TranslatedTextContent getInvalidCharactersMax() {
        return invalidCharactersMax;
    }

    public void setInvalidCharactersMax(TranslatedTextContent invalidCharactersMax) {
        this.invalidCharactersMax = invalidCharactersMax;
    }

    public TranslatedTextContent getInvalidCharactersMin() {
        return invalidCharactersMin;
    }

    public void setInvalidCharactersMin(TranslatedTextContent invalidCharactersMin) {
        this.invalidCharactersMin = invalidCharactersMin;
    }

    public TranslatedTextContent getInvalidChoicesMax() {
        return invalidChoicesMax;
    }

    public void setInvalidChoicesMax(TranslatedTextContent invalidChoicesMax) {
        this.invalidChoicesMax = invalidChoicesMax;
    }

    public TranslatedTextContent getInvalidChoicesMin() {
        return invalidChoicesMin;
    }

    public void setInvalidChoicesMin(TranslatedTextContent invalidChoicesMin) {
        this.invalidChoicesMin = invalidChoicesMin;
    }

    public TranslatedTextContent getInvalidCreditCard() {
        return invalidCreditCard;
    }

    public void setInvalidCreditCard(TranslatedTextContent invalidCreditCard) {
        this.invalidCreditCard = invalidCreditCard;
    }

    public TranslatedTextContent getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(TranslatedTextContent invalidDate) {
        this.invalidDate = invalidDate;
    }

    public TranslatedTextContent getInvalidEmailAddress() {
        return invalidEmailAddress;
    }

    public void setInvalidEmailAddress(TranslatedTextContent invalidEmailAddress) {
        this.invalidEmailAddress = invalidEmailAddress;
    }

    public TranslatedTextContent getInvalidFileSize() {
        return invalidFileSize;
    }

    public void setInvalidFileSize(TranslatedTextContent invalidFileSize) {
        this.invalidFileSize = invalidFileSize;
    }

    public TranslatedTextContent getInvalidFileType() {
        return invalidFileType;
    }

    public void setInvalidFileType(TranslatedTextContent invalidFileType) {
        this.invalidFileType = invalidFileType;
    }

    public TranslatedTextContent getInvalidNumber() {
        return invalidNumber;
    }

    public void setInvalidNumber(TranslatedTextContent invalidNumber) {
        this.invalidNumber = invalidNumber;
    }

    public TranslatedTextContent getInvalidRankingValue() {
        return invalidRankingValue;
    }

    public void setInvalidRankingValue(TranslatedTextContent invalidRankingValue) {
        this.invalidRankingValue = invalidRankingValue;
    }

    public TranslatedTextContent getInvalidRecordNumber() {
        return invalidRecordNumber;
    }

    public void setInvalidRecordNumber(TranslatedTextContent invalidRecordNumber) {
        this.invalidRecordNumber = invalidRecordNumber;
    }

    public TranslatedTextContent getInvalidTextFormat() {
        return invalidTextFormat;
    }

    public void setInvalidTextFormat(TranslatedTextContent invalidTextFormat) {
        this.invalidTextFormat = invalidTextFormat;
    }

    public TranslatedTextContent getInvalidTimeForDate() {
        return invalidTimeForDate;
    }

    public void setInvalidTimeForDate(TranslatedTextContent invalidTimeForDate) {
        this.invalidTimeForDate = invalidTimeForDate;
    }

    public TranslatedTextContent getLegacyBrowserWarning() {
        return legacyBrowserWarning;
    }

    public void setLegacyBrowserWarning(TranslatedTextContent legacyBrowserWarning) {
        this.legacyBrowserWarning = legacyBrowserWarning;
    }

    public TranslatedTextContent getLockedRecord() {
        return lockedRecord;
    }

    public void setLockedRecord(TranslatedTextContent lockedRecord) {
        this.lockedRecord = lockedRecord;
    }

    public TranslatedTextContent getLowerBoundNotFulfilled() {
        return lowerBoundNotFulfilled;
    }

    public void setLowerBoundNotFulfilled(TranslatedTextContent lowerBoundNotFulfilled) {
        this.lowerBoundNotFulfilled = lowerBoundNotFulfilled;
    }

    public TranslatedTextContent getMobileDeviceMessage() {
        return mobileDeviceMessage;
    }

    public void setMobileDeviceMessage(TranslatedTextContent mobileDeviceMessage) {
        this.mobileDeviceMessage = mobileDeviceMessage;
    }

    public TranslatedTextContent getOutOfBounds() {
        return outOfBounds;
    }

    public void setOutOfBounds(TranslatedTextContent outOfBounds) {
        this.outOfBounds = outOfBounds;
    }

    public TranslatedTextContent getPageHasErrors() {
        return pageHasErrors;
    }

    public void setPageHasErrors(TranslatedTextContent pageHasErrors) {
        this.pageHasErrors = pageHasErrors;
    }

    public TranslatedTextContent getPreviewModeMessage() {
        return previewModeMessage;
    }

    public void setPreviewModeMessage(TranslatedTextContent previewModeMessage) {
        this.previewModeMessage = previewModeMessage;
    }

    public TranslatedTextContent getQuotaMetClosedMessage() {
        return quotaMetClosedMessage;
    }

    public void setQuotaMetClosedMessage(TranslatedTextContent quotaMetClosedMessage) {
        this.quotaMetClosedMessage = quotaMetClosedMessage;
    }

    public TranslatedTextContent getRespondentAccessExpired() {
        return respondentAccessExpired;
    }

    public void setRespondentAccessExpired(TranslatedTextContent respondentAccessExpired) {
        this.respondentAccessExpired = respondentAccessExpired;
    }

    public TranslatedTextContent getScreenedOutTerminationMessage() {
        return screenedOutTerminationMessage;
    }

    public void setScreenedOutTerminationMessage(TranslatedTextContent screenedOutTerminationMessage) {
        this.screenedOutTerminationMessage = screenedOutTerminationMessage;
    }

    public TranslatedTextContent getSurveyLoadingMessage() {
        return surveyLoadingMessage;
    }

    public void setSurveyLoadingMessage(TranslatedTextContent surveyLoadingMessage) {
        this.surveyLoadingMessage = surveyLoadingMessage;
    }

    public TranslatedTextContent getSurveyReaccessMessage() {
        return surveyReaccessMessage;
    }

    public void setSurveyReaccessMessage(TranslatedTextContent surveyReaccessMessage) {
        this.surveyReaccessMessage = surveyReaccessMessage;
    }

    public TranslatedTextContent getTabletDeviceMessage() {
        return tabletDeviceMessage;
    }

    public void setTabletDeviceMessage(TranslatedTextContent tabletDeviceMessage) {
        this.tabletDeviceMessage = tabletDeviceMessage;
    }

    public TranslatedTextContent getTerminationMessage() {
        return terminationMessage;
    }

    public void setTerminationMessage(TranslatedTextContent terminationMessage) {
        this.terminationMessage = terminationMessage;
    }

    public TranslatedTextContent getTimerTerminationMessage() {
        return timerTerminationMessage;
    }

    public void setTimerTerminationMessage(TranslatedTextContent timerTerminationMessage) {
        this.timerTerminationMessage = timerTerminationMessage;
    }

    public TranslatedTextContent getUnavailableDate() {
        return unavailableDate;
    }

    public void setUnavailableDate(TranslatedTextContent unavailableDate) {
        this.unavailableDate = unavailableDate;
    }

    public TranslatedTextContent getUnknownDeviceMessage() {
        return unknownDeviceMessage;
    }

    public void setUnknownDeviceMessage(TranslatedTextContent unknownDeviceMessage) {
        this.unknownDeviceMessage = unknownDeviceMessage;
    }

    public TranslatedTextContent getUnsupportedDeviceMessage() {
        return unsupportedDeviceMessage;
    }

    public void setUnsupportedDeviceMessage(TranslatedTextContent unsupportedDeviceMessage) {
        this.unsupportedDeviceMessage = unsupportedDeviceMessage;
    }

    public TranslatedTextContent getUpperBoundNotFulfilled() {
        return upperBoundNotFulfilled;
    }

    public void setUpperBoundNotFulfilled(TranslatedTextContent upperBoundNotFulfilled) {
        this.upperBoundNotFulfilled = upperBoundNotFulfilled;
    }

    public TranslatedTextContent getUnexpectedError() {
        return unexpectedError;
    }

    public void setUnexpectedError(TranslatedTextContent unexpectedError) {
        this.unexpectedError = unexpectedError;
    }
}
