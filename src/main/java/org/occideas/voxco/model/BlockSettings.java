package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockSettings {

    @JsonProperty(value = "DisplayShortcuts")
    private Boolean displayShortcuts;

    public Boolean getDisplayShortcuts() {
        return displayShortcuts;
    }

    public void setDisplayShortcuts(Boolean displayShortcuts) {
        this.displayShortcuts = displayShortcuts;
    }
}
