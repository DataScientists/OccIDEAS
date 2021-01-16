package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Variable {

    private String name;
    private Integer maxMention;
    private Integer choiceListId;
    private TranslatedTexts translatedTexts;

    public Variable() {
    }

    public Variable(String name, Integer choiceListId) {
        this.name = name;
        this.maxMention = 1;
        this.choiceListId = choiceListId;
        this.translatedTexts = new TranslatedTexts();
    }

    public Variable(String name, Integer maxMention, Integer choiceListId) {
        this.name = name;
        this.maxMention = maxMention;
        this.choiceListId = choiceListId;
        this.translatedTexts = new TranslatedTexts();
    }

    public Variable(String name, Integer maxMention, Integer choiceListId, TranslatedTexts translatedTexts) {
        this.name = name;
        this.maxMention = maxMention;
        this.choiceListId = choiceListId;
        this.translatedTexts = translatedTexts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxMention() {
        return maxMention;
    }

    public void setMaxMention(Integer maxMention) {
        this.maxMention = maxMention;
    }

    public Integer getChoiceListId() {
        return choiceListId;
    }

    public void setChoiceListId(Integer choiceListId) {
        this.choiceListId = choiceListId;
    }

    public TranslatedTexts getTranslatedTexts() {
        return translatedTexts;
    }

    public void setTranslatedTexts(TranslatedTexts translatedTexts) {
        this.translatedTexts = translatedTexts;
    }
}
