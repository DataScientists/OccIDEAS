package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Theme {

    private String name;
    private Settings settings;
    private TranslatedTexts translatedTexts;

    public Theme() {
    }

    public Theme(String name) {
        this.name = name;
        this.settings = new Settings();
        this.translatedTexts = new TranslatedTexts();
    }

    public Theme(String name, Settings settings, TranslatedTexts translatedTexts) {
        this.name = name;
        this.settings = settings;
        this.translatedTexts = translatedTexts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public TranslatedTexts getTranslatedTexts() {
        return translatedTexts;
    }

    public void setTranslatedTexts(TranslatedTexts translatedTexts) {
        this.translatedTexts = translatedTexts;
    }
}
