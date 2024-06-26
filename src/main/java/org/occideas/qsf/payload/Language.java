package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.occideas.qsf.serializer.ChoicesSerializer;

import java.util.Map;

public class Language {

    @JsonIgnore
    private String language;

    @JsonProperty(value = "QuestionText")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String questionText;

    @JsonProperty(value = "Choices")
    @JsonSerialize(using = ChoicesSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Choice> choicesList;

    public Language() {
    }

    public Language(String language, String questionText, Map<String, Choice> choicesList) {
        this.language = language;
        this.questionText = questionText;
        this.choicesList = choicesList;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Map<String, Choice> getChoicesList() {
        return choicesList;
    }

    public void setChoicesList(Map<String, Choice> choicesList) {
        this.choicesList = choicesList;
    }
}
