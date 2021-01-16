package org.occideas.voxco.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SurveyImportResult {

    @JsonProperty(value = "ImportedBlocks")
    private Integer importedBlocks;
    @JsonProperty(value = "ImportedQuestions")
    private Integer importedQuestions;
    @JsonProperty(value = "ImportedLogics")
    private Integer importedLogics;
    @JsonProperty(value = "ImportedActions")
    private Integer importedActions;
    @JsonProperty(value = "AppendedQuestions")
    private List<String> appendedQuestions;

    public Integer getImportedBlocks() {
        return importedBlocks;
    }

    public void setImportedBlocks(Integer importedBlocks) {
        this.importedBlocks = importedBlocks;
    }

    public Integer getImportedQuestions() {
        return importedQuestions;
    }

    public void setImportedQuestions(Integer importedQuestions) {
        this.importedQuestions = importedQuestions;
    }

    public Integer getImportedLogics() {
        return importedLogics;
    }

    public void setImportedLogics(Integer importedLogics) {
        this.importedLogics = importedLogics;
    }

    public Integer getImportedActions() {
        return importedActions;
    }

    public void setImportedActions(Integer importedActions) {
        this.importedActions = importedActions;
    }

    public List<String> getAppendedQuestions() {
        return appendedQuestions;
    }

    public void setAppendedQuestions(List<String> appendedQuestions) {
        this.appendedQuestions = appendedQuestions;
    }
}
