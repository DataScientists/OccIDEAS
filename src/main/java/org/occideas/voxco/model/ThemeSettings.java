package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThemeSettings {

    @JsonProperty(value = "BackButtonDisplayPositionType")
    private String backButtonDisplayPositionType = "Bottom";

    @JsonProperty(value = "BackButtonType")
    private String backButtonType = "Image";

    @JsonProperty(value = "BackButtonVisible")
    private boolean backButtonVisible = true;

    @JsonProperty(value = "CheckBoxButtonType")
    private String checkBoxButtonType = "Button";

    @JsonProperty(value = "LanguageSelectorShowLabel")
    private boolean languageSelectorShowLabel;

    @JsonProperty(value = "LanguageSelectorVisible")
    private boolean languageSelectorVisible;

    @JsonProperty(value = "NextButtonDisplayPositionType")
    private boolean nextButtonDisplayPositionType;

    @JsonProperty(value = "NextButtonType")
    private boolean nextButtonType;

    @JsonProperty(value = "NextButtonVisible")
    private boolean nextButtonVisible = true;

    @JsonProperty(value = "PageFooterVisible")
    private boolean pageFooterVisible = true;

    @JsonProperty(value = "PageHeaderVisible")
    private boolean pageHeaderVisible = true;

    @JsonProperty(value = "PageTitleVisible")
    private boolean pageTitleVisible;

    @JsonProperty(value = "ProgressBarLocationType")
    private String progressBarLocationType = "Top";

    @JsonProperty(value = "ProgressBarShowLabel")
    private boolean progressBarShowLabel = true;

    @JsonProperty(value = "ProgressBarShowPercentage")
    private boolean progressBarShowPercentage = true;

    @JsonProperty(value = "ProgressBarVisible")
    private boolean progressBarVisible = true;

    @JsonProperty(value = "QuestionBgSameAsBlock")
    private boolean questionBgSameAsBlock;

    @JsonProperty(value = "QuestionBlockBgSameAsPage")
    private boolean questionBlockBgSameAsPage;

    @JsonProperty(value = "QuestionLabelVisible")
    private boolean questionLabelVisible = true;

    @JsonProperty(value = "QuestionNameVisible")
    private boolean questionNameVisible;

    @JsonProperty(value = "QuitButtonDisplayPositionType")
    private String quitButtonDisplayPositionType = "None";

    @JsonProperty(value = "QuitButtonType")
    private String quitButtonType = "Image";

    @JsonProperty(value = "QuitButtonVisible")
    private boolean quitButtonVisible = true;

    @JsonProperty(value = "RadioButtonType")
    private String radioButtonType = "Button";

    @JsonProperty(value = "SurveyShortcutVisible")
    private boolean surveyShortcutVisible;

    @JsonProperty(value = "MobileUseStandardButtons")
    private boolean mobileUseStandardButtons;

    @JsonProperty(value = "CenterQuestions")
    private boolean centerQuestions;

    @JsonProperty(value = "UsePageHeaderMobile")
    private boolean usePageHeaderMobile;

    @JsonProperty(value = "UsePageFooterMobile")
    private boolean usePageFooterMobile;

    @JsonProperty(value = "PrintResponsesButtonVisible")
    private boolean printResponsesButtonVisible;

    public String getBackButtonDisplayPositionType() {
        return backButtonDisplayPositionType;
    }

    public void setBackButtonDisplayPositionType(String backButtonDisplayPositionType) {
        this.backButtonDisplayPositionType = backButtonDisplayPositionType;
    }

    public String getBackButtonType() {
        return backButtonType;
    }

    public void setBackButtonType(String backButtonType) {
        this.backButtonType = backButtonType;
    }

    public boolean isBackButtonVisible() {
        return backButtonVisible;
    }

    public void setBackButtonVisible(boolean backButtonVisible) {
        this.backButtonVisible = backButtonVisible;
    }

    public String getCheckBoxButtonType() {
        return checkBoxButtonType;
    }

    public void setCheckBoxButtonType(String checkBoxButtonType) {
        this.checkBoxButtonType = checkBoxButtonType;
    }

    public boolean isLanguageSelectorShowLabel() {
        return languageSelectorShowLabel;
    }

    public void setLanguageSelectorShowLabel(boolean languageSelectorShowLabel) {
        this.languageSelectorShowLabel = languageSelectorShowLabel;
    }

    public boolean isLanguageSelectorVisible() {
        return languageSelectorVisible;
    }

    public void setLanguageSelectorVisible(boolean languageSelectorVisible) {
        this.languageSelectorVisible = languageSelectorVisible;
    }

    public boolean isNextButtonDisplayPositionType() {
        return nextButtonDisplayPositionType;
    }

    public void setNextButtonDisplayPositionType(boolean nextButtonDisplayPositionType) {
        this.nextButtonDisplayPositionType = nextButtonDisplayPositionType;
    }

    public boolean isNextButtonType() {
        return nextButtonType;
    }

    public void setNextButtonType(boolean nextButtonType) {
        this.nextButtonType = nextButtonType;
    }

    public boolean isNextButtonVisible() {
        return nextButtonVisible;
    }

    public void setNextButtonVisible(boolean nextButtonVisible) {
        this.nextButtonVisible = nextButtonVisible;
    }

    public boolean isPageFooterVisible() {
        return pageFooterVisible;
    }

    public void setPageFooterVisible(boolean pageFooterVisible) {
        this.pageFooterVisible = pageFooterVisible;
    }

    public boolean isPageHeaderVisible() {
        return pageHeaderVisible;
    }

    public void setPageHeaderVisible(boolean pageHeaderVisible) {
        this.pageHeaderVisible = pageHeaderVisible;
    }

    public boolean isPageTitleVisible() {
        return pageTitleVisible;
    }

    public void setPageTitleVisible(boolean pageTitleVisible) {
        this.pageTitleVisible = pageTitleVisible;
    }

    public String getProgressBarLocationType() {
        return progressBarLocationType;
    }

    public void setProgressBarLocationType(String progressBarLocationType) {
        this.progressBarLocationType = progressBarLocationType;
    }

    public boolean isProgressBarShowLabel() {
        return progressBarShowLabel;
    }

    public void setProgressBarShowLabel(boolean progressBarShowLabel) {
        this.progressBarShowLabel = progressBarShowLabel;
    }

    public boolean isProgressBarShowPercentage() {
        return progressBarShowPercentage;
    }

    public void setProgressBarShowPercentage(boolean progressBarShowPercentage) {
        this.progressBarShowPercentage = progressBarShowPercentage;
    }

    public boolean isProgressBarVisible() {
        return progressBarVisible;
    }

    public void setProgressBarVisible(boolean progressBarVisible) {
        this.progressBarVisible = progressBarVisible;
    }

    public boolean isQuestionBgSameAsBlock() {
        return questionBgSameAsBlock;
    }

    public void setQuestionBgSameAsBlock(boolean questionBgSameAsBlock) {
        this.questionBgSameAsBlock = questionBgSameAsBlock;
    }

    public boolean isQuestionBlockBgSameAsPage() {
        return questionBlockBgSameAsPage;
    }

    public void setQuestionBlockBgSameAsPage(boolean questionBlockBgSameAsPage) {
        this.questionBlockBgSameAsPage = questionBlockBgSameAsPage;
    }

    public boolean isQuestionLabelVisible() {
        return questionLabelVisible;
    }

    public void setQuestionLabelVisible(boolean questionLabelVisible) {
        this.questionLabelVisible = questionLabelVisible;
    }

    public boolean isQuestionNameVisible() {
        return questionNameVisible;
    }

    public void setQuestionNameVisible(boolean questionNameVisible) {
        this.questionNameVisible = questionNameVisible;
    }

    public String getQuitButtonDisplayPositionType() {
        return quitButtonDisplayPositionType;
    }

    public void setQuitButtonDisplayPositionType(String quitButtonDisplayPositionType) {
        this.quitButtonDisplayPositionType = quitButtonDisplayPositionType;
    }

    public String getQuitButtonType() {
        return quitButtonType;
    }

    public void setQuitButtonType(String quitButtonType) {
        this.quitButtonType = quitButtonType;
    }

    public boolean isQuitButtonVisible() {
        return quitButtonVisible;
    }

    public void setQuitButtonVisible(boolean quitButtonVisible) {
        this.quitButtonVisible = quitButtonVisible;
    }

    public String getRadioButtonType() {
        return radioButtonType;
    }

    public void setRadioButtonType(String radioButtonType) {
        this.radioButtonType = radioButtonType;
    }

    public boolean isSurveyShortcutVisible() {
        return surveyShortcutVisible;
    }

    public void setSurveyShortcutVisible(boolean surveyShortcutVisible) {
        this.surveyShortcutVisible = surveyShortcutVisible;
    }

    public boolean isMobileUseStandardButtons() {
        return mobileUseStandardButtons;
    }

    public void setMobileUseStandardButtons(boolean mobileUseStandardButtons) {
        this.mobileUseStandardButtons = mobileUseStandardButtons;
    }

    public boolean isCenterQuestions() {
        return centerQuestions;
    }

    public void setCenterQuestions(boolean centerQuestions) {
        this.centerQuestions = centerQuestions;
    }

    public boolean isUsePageHeaderMobile() {
        return usePageHeaderMobile;
    }

    public void setUsePageHeaderMobile(boolean usePageHeaderMobile) {
        this.usePageHeaderMobile = usePageHeaderMobile;
    }

    public boolean isUsePageFooterMobile() {
        return usePageFooterMobile;
    }

    public void setUsePageFooterMobile(boolean usePageFooterMobile) {
        this.usePageFooterMobile = usePageFooterMobile;
    }

    public boolean isPrintResponsesButtonVisible() {
        return printResponsesButtonVisible;
    }

    public void setPrintResponsesButtonVisible(boolean printResponsesButtonVisible) {
        this.printResponsesButtonVisible = printResponsesButtonVisible;
    }
}
