package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Theme {

    private String name;
    private String css;
    private ThemeSettings settings;
    private ThemeTranslatedTexts translatedTexts;

    public Theme() {
        setDefaultCss();
    }

    public Theme(String name) {
        this.name = name;

        setDefaultCss();
        this.settings = new ThemeSettings();
        this.translatedTexts = new ThemeTranslatedTexts();
    }

    public Theme(String name, ThemeSettings settings, ThemeTranslatedTexts translatedTexts) {
        this.name = name;
        this.settings = settings;
        this.translatedTexts = translatedTexts;
        setDefaultCss();
    }

    public void setDefaultCss() {
        this.css = ".backButton.ui-btn-up-c { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.backButton.ui-btn-hover-c { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.backButton.ui-btn-down-c { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.backButton.ui-btn-active { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.nextButton.ui-btn-up-c { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.nextButton.ui-btn-hover-c { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.nextButton.ui-btn-down-c { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.nextButton.ui-btn-active { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.quitButton.ui-btn-up-c { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.quitButton.ui-btn-hover-c { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.quitButton.ui-btn-down-c { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.quitButton.ui-btn-active { color: #FFFFFF; font-size: 12pt; font-weight: bold; } \\n.choiceImage .choiceImageOverlay { border-style: solid; border-width: 0px; border-color: #FFFFFF; } \\n.choiceImage:before { border-style: solid; border-width: 0px; border-color: #FFFFFF; } \\n.standard .choiceText { font-family:  'Open Sans', sans-serif; font-size: 10pt; } \\n.ui-radio label, .ui-checkbox label { font-family:  'Open Sans', sans-serif; font-size: 10pt; } \\n.mobile .choiceHeader { margin-top: 10px; } \\n.mobile .choiceContainer { margin-left: 10px; } \\n.dropDown, .ui-select .ui-btn-up-c { font-family:  'Open Sans', sans-serif; font-size: 10pt; } \\n.ui-select .ui-btn-hover-c { font-family:  'Open Sans', sans-serif; font-size: 10pt; } \\n.ui-select .ui-btn-down-c { font-family:  'Open Sans', sans-serif; font-size: 10pt; } \\n.errorMessage { color: #d9351a; font-family:  'Open Sans', sans-serif; font-size: 10pt; font-weight: bold; } \\n.standard .grid { border-spacing: 5px; border-collapse: collapse; } \\n.grid { border-spacing: 5px; border-collapse: collapse; } \\n.standard .grid .gridColumnHeader { background-color: #0E76C1; font-weight: normal; font-family:  'Open Sans', sans-serif; font-size: 10pt; color: #FFFFFF; } \\n.gridColumnHeader { background-color: #0E76C1; font-weight: normal; font-family:  'Open Sans', sans-serif; font-size: 10pt; color: #FFFFFF; } \\n.standard .grid .gridRowHeader { font-family:  'Open Sans', sans-serif; font-size: 10pt; font-weight: normal; color: #000000; } \\n.gridRowHeader { font-family:  'Open Sans', sans-serif; font-size: 10pt; font-weight: normal; color: #000000; } \\n.standard .grid .gridAlternateRow { background-color: #FFFFFF; } \\n.gridAlternateRow { background-color: #FFFFFF; } \\n.navigationBar { min-height: 20px; } \\n.questionBackground { background-color: #f1f1f1; } \\n.questionContent { padding: 10px 10px 10px 10px; } \\n.questionName { font-family:  'Open Sans', sans-serif; font-size: 10pt; color: #000000; } \\n.questionText { font-family:  'Open Sans', sans-serif; font-size: 10pt; color: #000000; font-weight: bold; } \\n.questionTextContainer { text-align: left; } \\n.pageBackground { min-height: 200px; background-position: left bottom; background-repeat: repeat-x; } \\n.progressBarBackground { padding: 1px 1px 1px 1px; background-color: #f1f1f1; border-width: 0px; border-style: none; } \\n.progressBarBar { background-color: #5EB662; background-image: -webkit-gradient(linear, left top, right top, from(#6EC372), to(#49A14D)); background-image: -webkit-linear-gradient(left, #6EC372, #49A14D); background-image: -moz-linear-gradient(left, #6EC372, #49A14D); background-image: -ms-linear-gradient(left, #6EC372, #49A14D); background-image: -o-linear-gradient(left, #6EC372, #49A14D); background-image: linear-gradient(left, #6EC372, #49A14D); filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1, StartColorStr='#6EC372', EndColorStr='#49A14D'); background-position: right center; background-repeat: repeat-y } \\n.progressBar { font-family:  'Open Sans', sans-serif; font-size: 10pt; font-weight: bold; } \\n.languageSelector { text-align: right; } \\n.choiceImage.selectedImageChoice .choiceImageOverlay { border-width: 5px; border-style: solid; border-color: #0E76C1; } \\n.selectedImageChoice:before { border-width: 5px; border-style: solid; border-color: #0E76C1; } \\n.choiceImage.highlightedImageChoice .choiceImageOverlay { border-width: 3px; border-style: solid; border-color: #62A1C7; } \\n.highlightedImageChoice:before { border-width: 3px; border-style: solid; border-color: #62A1C7; } \\n.standard .selectedImageArea { background-color: #0E76C1; } \\n.selectedImageArea { background-color: #0E76C1; } \\n.standard .highlightedImageArea { background-color: #FFFFFF; } \\n.highlightedImageArea { background-color: #FFFFFF; } \\n.cardSort.standard .bucketList .bucket { color: #FFF; background-color: #FFFFFF; border-width: 1px; border-style: solid; border-color: #D8D8D8; font-family:  'Open Sans', sans-serif; font-size: 10pt; font-weight: normal; font-style: italic; } \\n.bucket { color: #FFF; background-color: #FFFFFF; border-width: 1px; border-style: solid; border-color: #D8D8D8; font-family:  'Open Sans', sans-serif; font-size: 10pt; font-weight: normal; font-style: italic; } \\n.cardSort.standard .card { color: #FFF; background-color: #168CB3; border-width: 1px; border-style: solid; border-color: #D8D8D8; font-family:  'Open Sans', sans-serif; font-size: 10pt; font-weight: normal; font-style: normal; } \\n.card { color: #FFF; background-color: #168CB3; border-width: 1px; border-style: solid; border-color: #D8D8D8; font-family:  'Open Sans', sans-serif; font-size: 10pt; font-weight: normal; font-style: normal; } \\n.dragDropRanking.standard .dragDropAnswerList { border-width: 1px; border-style: solid; border-color: #D8D8D8; } \\n.dragDropAnswerList { border-width: 1px; border-style: solid; border-color: #D8D8D8; } \\n.dragDropRanking.standard .dragDropChoiceList { border-width: 1px; border-style: solid; border-color: #D8D8D8; } \\n.dragDropChoiceList { border-width: 1px; border-style: solid; border-color: #D8D8D8; } \\n.dragDropRanking.standard .dragDropList .dragDropItem { background-color: #168CB3; color: #FFF; } \\n.dragDropItem { background-color: #168CB3; color: #FFF; } \\n .mobile .starRating > .star.off > a {background-image:url(\\\"/media/default/star_OFF.png\\\"); width: 0px; height: 0px; background-position: 0px 0px; }\\n .mobile .starRating > .star.on > a {background-image:url(\\\"/media/default/star_ON.png\\\"); width: 0px; height: 0px; background-position: 0px 0px; }\\n .mobile .starRating > .star.hover > a {background-image:url(\\\"/media/default/star_MOUSE.png\\\"); width: 0px; height: 0px; background-position: 0px 0px; }\\n .mobile .starRating > .clearRating.off {background-image:url(\\\"/media/default/reset_OFF.png\\\"); width: 0px; height: 0px; background-position: 0px 0px; }\\n .mobile .starRating > .clearRating.hover { background-image:url(\\\"/media/default/reset_ON.png\\\"); width: 0px; height: 0px; background-position: 0px 0px; }\\n \\n/******* Custom CSS *******/\\n ";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public ThemeSettings getSettings() {
        return settings;
    }

    public void setSettings(ThemeSettings settings) {
        this.settings = settings;
    }

    public ThemeTranslatedTexts getTranslatedTexts() {
        return translatedTexts;
    }

    public void setTranslatedTexts(ThemeTranslatedTexts translatedTexts) {
        this.translatedTexts = translatedTexts;
    }
}
