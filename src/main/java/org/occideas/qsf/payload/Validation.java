package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Validation {

    @JsonProperty(value = "Settings")
    private Setting setting;

    public Validation() {
    }

    public Validation(Setting setting) {
        this.setting = setting;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }
}
