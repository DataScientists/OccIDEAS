package org.occideas.voxco.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.occideas.voxco.model.Block;
import org.occideas.voxco.model.Choice;
import org.occideas.voxco.model.SurveySettings;
import org.occideas.voxco.model.SurveyTranslatedTexts;
import org.occideas.voxco.model.Theme;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyImportRequest {

    private Long id;
    private String name;
    private SurveySettings settings;
    private String defaultLanguage;
    private Set<String> languages;
    private List<Block> blocks;
    private List<List<Choice>> choiceLists;
    private List<Object> shortcuts;
    private List<Object> randomizations;
    private SurveyTranslatedTexts translatedTexts;
    private Theme theme;

    public SurveyImportRequest() {
    }

    public SurveyImportRequest(String name, String completedActionRedirectUrl, List<Block> blocks, List<List<Choice>> choiceLists) {
        this.name = name;
        this.blocks = blocks;
        this.choiceLists = choiceLists;

        this.settings = new SurveySettings(completedActionRedirectUrl);
        addDefaultLanguage();
        this.shortcuts = new ArrayList<>();
        this.randomizations = new ArrayList<>();
        this.translatedTexts = new SurveyTranslatedTexts();
        this.theme = new Theme(name);

    }

    public void addDefaultLanguage() {
        this.defaultLanguage = "en";
        if (this.languages == null) {
            this.languages = new HashSet<>();
        }
        this.languages.add(this.defaultLanguage);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SurveySettings getSettings() {
        return settings;
    }

    public void setSettings(SurveySettings settings) {
        this.settings = settings;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Set<String> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<String> languages) {
        this.languages = languages;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<List<Choice>> getChoiceLists() {
        return choiceLists;
    }

    public void setChoiceLists(List<List<Choice>> choiceLists) {
        this.choiceLists = choiceLists;
    }

    public List<Object> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(List<Object> shortcuts) {
        this.shortcuts = shortcuts;
    }

    public List<Object> getRandomizations() {
        return randomizations;
    }

    public void setRandomizations(List<Object> randomizations) {
        this.randomizations = randomizations;
    }

    public SurveyTranslatedTexts getTranslatedTexts() {
        return translatedTexts;
    }

    public void setTranslatedTexts(SurveyTranslatedTexts translatedTexts) {
        this.translatedTexts = translatedTexts;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
