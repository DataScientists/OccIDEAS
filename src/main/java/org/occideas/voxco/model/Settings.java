package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Settings {

    @JsonProperty(value = "DisplayShortcuts")
    private Boolean displayShortcuts;

    @JsonProperty(value = "CaseMode")
    private String caseMode;

    @JsonProperty(value = "DisplayOrder")
    private String displayOrder;

    public Boolean getDisplayShortcuts() {
        return displayShortcuts;
    }

    public void setDisplayShortcuts(Boolean displayShortcuts) {
        this.displayShortcuts = displayShortcuts;
    }

    public String getCaseMode() {
        return caseMode;
    }

    public void setCaseMode(String caseMode) {
        this.caseMode = caseMode;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }
}
