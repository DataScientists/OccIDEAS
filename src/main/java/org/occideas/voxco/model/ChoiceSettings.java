package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChoiceSettings {

    @JsonProperty(value = "OpenEndType")
    private String openEndType;

    @JsonProperty(value = "OpenEndHeight")
    private String openEndHeight;

    @JsonProperty(value = "OpenEndWidth")
    private String openEndWidth;

    @JsonProperty(value = "DisplayOpenEndOnlyWhenSelected")
    private Boolean displayOpenEndOnlyWhenSelected;

    @JsonProperty(value = "EnforceOpenEnd")
    private Boolean enforceOpenEnd;

    @JsonProperty(value = "OpenEndSize")
    private String openEndSize;

    @JsonProperty(value = "OpenEndLines")
    private Integer openEndLines;

    @JsonProperty(value = "Exclusive")
    private Boolean exclusive;

    @JsonProperty(value = "Visible")
    private Boolean visible;

    public ChoiceSettings() {
        this.openEndType = "Right";
        this.openEndHeight = "50px";
        this.openEndWidth = "250px";
        this.displayOpenEndOnlyWhenSelected = true;
        this.enforceOpenEnd = true;
        this.openEndSize = "";
        this.openEndLines = 2;
    }

    public ChoiceSettings(Boolean exclusive) {
        this.exclusive = exclusive;
    }

    public ChoiceSettings(Boolean exclusive, Boolean visible) {
        this.exclusive = exclusive;
        this.visible = visible;
    }

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

    public Boolean getDisplayOpenEndOnlyWhenSelected() {
        return displayOpenEndOnlyWhenSelected;
    }

    public void setDisplayOpenEndOnlyWhenSelected(Boolean displayOpenEndOnlyWhenSelected) {
        this.displayOpenEndOnlyWhenSelected = displayOpenEndOnlyWhenSelected;
    }

    public Boolean getEnforceOpenEnd() {
        return enforceOpenEnd;
    }

    public void setEnforceOpenEnd(Boolean enforceOpenEnd) {
        this.enforceOpenEnd = enforceOpenEnd;
    }

    public String getOpenEndSize() {
        return openEndSize;
    }

    public void setOpenEndSize(String openEndSize) {
        this.openEndSize = openEndSize;
    }

    public Integer getOpenEndLines() {
        return openEndLines;
    }

    public void setOpenEndLines(Integer openEndLines) {
        this.openEndLines = openEndLines;
    }

    public Boolean getExclusive() {
        return exclusive;
    }

    public void setExclusive(Boolean exclusive) {
        this.exclusive = exclusive;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
