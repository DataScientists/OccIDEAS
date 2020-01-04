package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {

    @JsonProperty(value = "Display")
    private String display;
    @JsonProperty(value = "TextEntry")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String textEntry;

    public Choice() {
    }

    public Choice(String display) {
        this.display = display;
    }

    public Choice(String display, String textEntry) {
        this.display = display;
        this.textEntry = textEntry;
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
}
