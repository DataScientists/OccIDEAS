package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {

    @JsonProperty(value = "Display")
    private String display;

    public Choice() {
    }

    public Choice(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
