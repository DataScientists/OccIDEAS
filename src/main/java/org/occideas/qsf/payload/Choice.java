package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {

    @JsonProperty(value = "Display")
    private String display;
    @JsonProperty(value = "TextEntry")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String textEntry;
    @JsonProperty(value = "TextEntryValidation")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String textEntryValidation;
    @JsonProperty(value = "ExclusiveAnswer")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean exclusiveAnswer;


    public Choice() {
    }

    public Choice(String display) {
        this.display = display;
    }

    public Choice(String display, String textEntry) {
        this.display = display;
        this.textEntry = textEntry;
    }

    public Choice(String display, Boolean exclusiveAnswer) {
        this.display = display;
        this.exclusiveAnswer = exclusiveAnswer;
    }

    public Choice(String display, String textEntry, String textEntryValidation) {
        this.display = display;
        this.textEntry = textEntry;
        this.textEntryValidation = textEntryValidation;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getTextEntry() {
        return textEntry;
    }

    public void setTextEntry(String textEntry) {
        this.textEntry = textEntry;
    }

    public String getTextEntryValidation() {
        return textEntryValidation;
    }

    public void setTextEntryValidation(String textEntryValidation) {
        this.textEntryValidation = textEntryValidation;
    }

    public Boolean getExclusiveAnswer() {
        return exclusiveAnswer;
    }

    public void setExclusiveAnswer(Boolean exclusiveAnswer) {
        this.exclusiveAnswer = exclusiveAnswer;
    }

    @Override
    public String toString() {
        return "Choice{" +
                "display='" + display + '\'' +
                ", textEntry='" + textEntry + '\'' +
                ", textEntryValidation='" + textEntryValidation + '\'' +
                ", exclusiveAnswer=" + exclusiveAnswer +
                '}';
    }
}
