package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThemeTranslatedTexts {

    @JsonProperty(value = "BackButton")
    private TranslatedTextContent backButton;

    @JsonProperty(value = "NextButton")
    private TranslatedTextContent nextButton;

    @JsonProperty(value = "Progress")
    private TranslatedTextContent progress;

    @JsonProperty(value = "QuitButton")
    private TranslatedTextContent quitButton;

    @JsonProperty(value = "BackTooltip")
    private TranslatedTextContent backTooltip;

    @JsonProperty(value = "NextTooltip")
    private TranslatedTextContent nextTooltip;

    @JsonProperty(value = "QuitTooltip")
    private TranslatedTextContent quitTooltip;

    @JsonProperty(value = "PageHeader")
    private TranslatedTextContent pageHeader;

    @JsonProperty(value = "PageFooter")
    private TranslatedTextContent pageFooter;

    @JsonProperty(value = "DropdownEmptyAnswer")
    private TranslatedTextContent dropdownEmptyAnswer;

    @JsonProperty(value = "SubmitButton")
    private TranslatedTextContent submitButton;

    @JsonProperty(value = "SubmitTooltip")
    private TranslatedTextContent submitTooltip;

    @JsonProperty(value = "ErrorMessageImage")
    private TranslatedTextContent errorMessageImage;

    @JsonProperty(value = "BackButtonImage")
    private TranslatedTextContent backButtonImage;

    @JsonProperty(value = "NextButtonImage")
    private TranslatedTextContent nextButtonImage;

    @JsonProperty(value = "QuitButtonImage")
    private TranslatedTextContent quitButtonImage;

    @JsonProperty(value = "SubmitButtonImage")
    private TranslatedTextContent submitButtonImage;

    @JsonProperty(value = "StarRatingImage")
    private TranslatedTextContent sarRatingImage;

    @JsonProperty(value = "StarRatingSelectedImage")
    private TranslatedTextContent starRatingSelectedImage;

    @JsonProperty(value = "StarRatingHighlightedImage")
    private TranslatedTextContent starRatingHighlightedImage;

    @JsonProperty(value = "StarRatingClearRating")
    private TranslatedTextContent starRatingClearRating;

    @JsonProperty(value = "StarRatingClearRatingHoverImage")
    private TranslatedTextContent starRatingClearRatingHoverImage;

    @JsonProperty(value = "BackButtonHoverImage")
    private TranslatedTextContent backButtonHoverImage;

    @JsonProperty(value = "NextButtonHoverImage")
    private TranslatedTextContent nextButtonHoverImage;

    @JsonProperty(value = "QuitButtonHoverImage")
    private TranslatedTextContent quitButtonHoverImage;

    @JsonProperty(value = "SubmitButtonHoverImage")
    private TranslatedTextContent submitButtonHoverImage;

    @JsonProperty(value = "SliderNoAnswer")
    private TranslatedTextContent sliderNoAnswer;

    @JsonProperty(value = "LanguageSelector")
    private TranslatedTextContent languageSelector;

    @JsonProperty(value = "AutoCompleteMaxResults")
    private TranslatedTextContent autoCompleteMaxResults;

    @JsonProperty(value = "FileUploadClear")
    private TranslatedTextContent fileUploadClear;

    @JsonProperty(value = "PrintResponsesButton")
    private TranslatedTextContent printResponsesButton;

    public ThemeTranslatedTexts() {
        this.backButton = new TranslatedTextContent("Back");
        this.nextButton = new TranslatedTextContent("Next");
        this.progress = new TranslatedTextContent("Progress");
        this.quitButton = new TranslatedTextContent("Quit");
        this.backTooltip = new TranslatedTextContent("Back");
        this.nextTooltip = new TranslatedTextContent("Next");
        this.quitTooltip = new TranslatedTextContent("Quit");
        this.pageHeader = new TranslatedTextContent("<div style='width: 800px !important; margin: 0px auto !important; padding: 20px 0px;'><table valign='center' align='center' border='0' width='100%'><tbody><tr><td align='left'><img alt='logo' src='/media/default/logoHere.png'></td></tr></tbody></table></div>");
        this.pageFooter = new TranslatedTextContent("<style type=text/css>.pageBackground:not(.ui-page) .pageFooter { position: fixed; bottom: 0; right: 0; }</style><span id='PageFooterControl_PageFooterLabel_default'><div class='demoLogo' style='border-radius: 10px 0 0 0; padding: 10px 40px; background-color: #0f6cb4; color: #EBEBEB;  font-size: 14px;'>Powered by Voxco</div></span>");
        this.dropdownEmptyAnswer = new TranslatedTextContent("Select an answer...");
        this.submitButton = new TranslatedTextContent("Submit");
        this.submitTooltip = new TranslatedTextContent("Submit");
        this.errorMessageImage = new TranslatedTextContent("/media/default/X.png");
        this.backButtonImage = new TranslatedTextContent("/media/default/back_OFF.png");
        this.nextButtonImage = new TranslatedTextContent("/media/default/next_OFF.png");
        this.quitButtonImage = new TranslatedTextContent("/media/default/exit_OFF.png");
        this.submitButtonImage = new TranslatedTextContent("/media/default/submit_OFF.png");
        this.sarRatingImage = new TranslatedTextContent("/media/default/star_OFF.png");
        this.starRatingSelectedImage = new TranslatedTextContent("/media/default/star_ON.png");
        this.starRatingHighlightedImage = new TranslatedTextContent("/media/default/star_MOUSE.png");
        this.starRatingClearRating = new TranslatedTextContent("/media/default/reset_OFF.png");
        this.starRatingClearRatingHoverImage = new TranslatedTextContent("/media/default/reset_ON.png");
        this.backButtonHoverImage = new TranslatedTextContent("/media/default/back_ON.png");
        this.nextButtonHoverImage = new TranslatedTextContent("/media/default/next_ON.png");
        this.quitButtonHoverImage = new TranslatedTextContent("/media/default/exit_ON.png");
        this.submitButtonHoverImage = new TranslatedTextContent("/media/default/submit_ON.png");
        this.sliderNoAnswer = new TranslatedTextContent("No Answer");
        this.languageSelector = new TranslatedTextContent("Language");
        this.autoCompleteMaxResults = new TranslatedTextContent("Limited to {max} results");
        this.fileUploadClear = new TranslatedTextContent("Clear");
        this.printResponsesButton = new TranslatedTextContent("View Responses");

    }

    public TranslatedTextContent getBackButton() {
        return backButton;
    }

    public void setBackButton(TranslatedTextContent backButton) {
        this.backButton = backButton;
    }

    public TranslatedTextContent getNextButton() {
        return nextButton;
    }

    public void setNextButton(TranslatedTextContent nextButton) {
        this.nextButton = nextButton;
    }

    public TranslatedTextContent getProgress() {
        return progress;
    }

    public void setProgress(TranslatedTextContent progress) {
        this.progress = progress;
    }

    public TranslatedTextContent getQuitButton() {
        return quitButton;
    }

    public void setQuitButton(TranslatedTextContent quitButton) {
        this.quitButton = quitButton;
    }

    public TranslatedTextContent getBackTooltip() {
        return backTooltip;
    }

    public void setBackTooltip(TranslatedTextContent backTooltip) {
        this.backTooltip = backTooltip;
    }

    public TranslatedTextContent getNextTooltip() {
        return nextTooltip;
    }

    public void setNextTooltip(TranslatedTextContent nextTooltip) {
        this.nextTooltip = nextTooltip;
    }

    public TranslatedTextContent getQuitTooltip() {
        return quitTooltip;
    }

    public void setQuitTooltip(TranslatedTextContent quitTooltip) {
        this.quitTooltip = quitTooltip;
    }

    public TranslatedTextContent getPageHeader() {
        return pageHeader;
    }

    public void setPageHeader(TranslatedTextContent pageHeader) {
        this.pageHeader = pageHeader;
    }

    public TranslatedTextContent getPageFooter() {
        return pageFooter;
    }

    public void setPageFooter(TranslatedTextContent pageFooter) {
        this.pageFooter = pageFooter;
    }

    public TranslatedTextContent getDropdownEmptyAnswer() {
        return dropdownEmptyAnswer;
    }

    public void setDropdownEmptyAnswer(TranslatedTextContent dropdownEmptyAnswer) {
        this.dropdownEmptyAnswer = dropdownEmptyAnswer;
    }

    public TranslatedTextContent getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(TranslatedTextContent submitButton) {
        this.submitButton = submitButton;
    }

    public TranslatedTextContent getSubmitTooltip() {
        return submitTooltip;
    }

    public void setSubmitTooltip(TranslatedTextContent submitTooltip) {
        this.submitTooltip = submitTooltip;
    }

    public TranslatedTextContent getErrorMessageImage() {
        return errorMessageImage;
    }

    public void setErrorMessageImage(TranslatedTextContent errorMessageImage) {
        this.errorMessageImage = errorMessageImage;
    }

    public TranslatedTextContent getBackButtonImage() {
        return backButtonImage;
    }

    public void setBackButtonImage(TranslatedTextContent backButtonImage) {
        this.backButtonImage = backButtonImage;
    }

    public TranslatedTextContent getNextButtonImage() {
        return nextButtonImage;
    }

    public void setNextButtonImage(TranslatedTextContent nextButtonImage) {
        this.nextButtonImage = nextButtonImage;
    }

    public TranslatedTextContent getQuitButtonImage() {
        return quitButtonImage;
    }

    public void setQuitButtonImage(TranslatedTextContent quitButtonImage) {
        this.quitButtonImage = quitButtonImage;
    }

    public TranslatedTextContent getSubmitButtonImage() {
        return submitButtonImage;
    }

    public void setSubmitButtonImage(TranslatedTextContent submitButtonImage) {
        this.submitButtonImage = submitButtonImage;
    }

    public TranslatedTextContent getSarRatingImage() {
        return sarRatingImage;
    }

    public void setSarRatingImage(TranslatedTextContent sarRatingImage) {
        this.sarRatingImage = sarRatingImage;
    }

    public TranslatedTextContent getStarRatingSelectedImage() {
        return starRatingSelectedImage;
    }

    public void setStarRatingSelectedImage(TranslatedTextContent starRatingSelectedImage) {
        this.starRatingSelectedImage = starRatingSelectedImage;
    }

    public TranslatedTextContent getStarRatingHighlightedImage() {
        return starRatingHighlightedImage;
    }

    public void setStarRatingHighlightedImage(TranslatedTextContent starRatingHighlightedImage) {
        this.starRatingHighlightedImage = starRatingHighlightedImage;
    }

    public TranslatedTextContent getStarRatingClearRating() {
        return starRatingClearRating;
    }

    public void setStarRatingClearRating(TranslatedTextContent starRatingClearRating) {
        this.starRatingClearRating = starRatingClearRating;
    }

    public TranslatedTextContent getStarRatingClearRatingHoverImage() {
        return starRatingClearRatingHoverImage;
    }

    public void setStarRatingClearRatingHoverImage(TranslatedTextContent starRatingClearRatingHoverImage) {
        this.starRatingClearRatingHoverImage = starRatingClearRatingHoverImage;
    }

    public TranslatedTextContent getBackButtonHoverImage() {
        return backButtonHoverImage;
    }

    public void setBackButtonHoverImage(TranslatedTextContent backButtonHoverImage) {
        this.backButtonHoverImage = backButtonHoverImage;
    }

    public TranslatedTextContent getNextButtonHoverImage() {
        return nextButtonHoverImage;
    }

    public void setNextButtonHoverImage(TranslatedTextContent nextButtonHoverImage) {
        this.nextButtonHoverImage = nextButtonHoverImage;
    }

    public TranslatedTextContent getQuitButtonHoverImage() {
        return quitButtonHoverImage;
    }

    public void setQuitButtonHoverImage(TranslatedTextContent quitButtonHoverImage) {
        this.quitButtonHoverImage = quitButtonHoverImage;
    }

    public TranslatedTextContent getSubmitButtonHoverImage() {
        return submitButtonHoverImage;
    }

    public void setSubmitButtonHoverImage(TranslatedTextContent submitButtonHoverImage) {
        this.submitButtonHoverImage = submitButtonHoverImage;
    }

    public TranslatedTextContent getSliderNoAnswer() {
        return sliderNoAnswer;
    }

    public void setSliderNoAnswer(TranslatedTextContent sliderNoAnswer) {
        this.sliderNoAnswer = sliderNoAnswer;
    }

    public TranslatedTextContent getLanguageSelector() {
        return languageSelector;
    }

    public void setLanguageSelector(TranslatedTextContent languageSelector) {
        this.languageSelector = languageSelector;
    }

    public TranslatedTextContent getAutoCompleteMaxResults() {
        return autoCompleteMaxResults;
    }

    public void setAutoCompleteMaxResults(TranslatedTextContent autoCompleteMaxResults) {
        this.autoCompleteMaxResults = autoCompleteMaxResults;
    }

    public TranslatedTextContent getFileUploadClear() {
        return fileUploadClear;
    }

    public void setFileUploadClear(TranslatedTextContent fileUploadClear) {
        this.fileUploadClear = fileUploadClear;
    }

    public TranslatedTextContent getPrintResponsesButton() {
        return printResponsesButton;
    }

    public void setPrintResponsesButton(TranslatedTextContent printResponsesButton) {
        this.printResponsesButton = printResponsesButton;
    }
}

