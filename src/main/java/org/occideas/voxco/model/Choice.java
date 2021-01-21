package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Choice {

    private ChoiceSettings settings;
    private String value;
    private TranslatedTexts translatedTexts;

    public Choice() {
    }

    public Choice(String value, TranslatedTexts translatedTexts) {
        this.value = value;
        this.translatedTexts = translatedTexts;
    }

    public Choice(ChoiceSettings settings, String value, TranslatedTexts translatedTexts) {
        this.settings = settings;
        this.value = value;
        this.translatedTexts = translatedTexts;
    }

    public ChoiceSettings getSettings() {
        return settings;
    }

    public void setSettings(ChoiceSettings settings) {
        this.settings = settings;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TranslatedTexts getTranslatedTexts() {
        return translatedTexts;
    }

    public void setTranslatedTexts(TranslatedTexts translatedTexts) {
        this.translatedTexts = translatedTexts;
    }
}
