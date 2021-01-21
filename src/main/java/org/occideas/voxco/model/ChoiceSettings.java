package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChoiceSettings {

    @JsonProperty(value = "OpenEndType")
    private String openEndType = "Right";

    @JsonProperty(value = "OpenEndHeight")
    private String openEndHeight = "50px";

    @JsonProperty(value = "OpenEndWidth")
    private String openEndWidth = "250px";

    @JsonProperty(value = "DisplayOpenEndOnlyWhenSelected")
    private boolean displayOpenEndOnlyWhenSelected = true;

    @JsonProperty(value = "EnforceOpenEnd")
    private boolean enforceOpenEnd = true;
    @JsonProperty(value = "OpenEndSize")
    private String openEndSize = "";

    @JsonProperty(value = "OpenEndLines")
    private int openEndLines = 2;

    public String getOpenEndType() {
        return openEndType;
    }

    public void setOpenEndType(String openEndType) {
        this.openEndType = openEndType;
    }

    public String getOpenEndHeight() {
        return openEndHeight;
    }

    public void setOpenEndHeight(String openEndHeight) {
        this.openEndHeight = openEndHeight;
    }

    public String getOpenEndWidth() {
        return openEndWidth;
    }

    public void setOpenEndWidth(String openEndWidth) {
        this.openEndWidth = openEndWidth;
    }

    public boolean isDisplayOpenEndOnlyWhenSelected() {
        return displayOpenEndOnlyWhenSelected;
    }

    public void setDisplayOpenEndOnlyWhenSelected(boolean displayOpenEndOnlyWhenSelected) {
        this.displayOpenEndOnlyWhenSelected = displayOpenEndOnlyWhenSelected;
    }

    public boolean isEnforceOpenEnd() {
        return enforceOpenEnd;
    }

    public void setEnforceOpenEnd(boolean enforceOpenEnd) {
        this.enforceOpenEnd = enforceOpenEnd;
    }

    public String getOpenEndSize() {
        return openEndSize;
    }

    public void setOpenEndSize(String openEndSize) {
        this.openEndSize = openEndSize;
    }

    public int getOpenEndLines() {
        return openEndLines;
    }

    public void setOpenEndLines(int openEndLines) {
        this.openEndLines = openEndLines;
    }
}
