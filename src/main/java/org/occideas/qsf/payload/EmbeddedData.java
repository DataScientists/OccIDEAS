package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

import java.util.ArrayList;
import java.util.List;

public class EmbeddedData extends BaseQSF {

    @JsonProperty(value = "Description")
    private String description;
    @JsonProperty(value = "Type")
    private String type;
    @JsonProperty(value = "Field")
    private String field;
    @JsonProperty(value = "VariableType")
    private String variableType;
    @JsonProperty(value = "DataVisibility")
    private List<Object> dataVisibility;
    @JsonProperty(value = "AnalyzeText")
    private boolean analyzeText;

    public EmbeddedData() {
    }

    public EmbeddedData(String type, String field, String variableType) {
        this.type = type;
        this.field = field;
        this.variableType = variableType;

        this.description = field;
        this.dataVisibility = new ArrayList<>();
        this.analyzeText = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getVariableType() {
        return variableType;
    }

    public void setVariableType(String variableType) {
        this.variableType = variableType;
    }

    public List<Object> getDataVisibility() {
        return dataVisibility;
    }

    public void setDataVisibility(List<Object> dataVisibility) {
        this.dataVisibility = dataVisibility;
    }

    public boolean isAnalyzeText() {
        return analyzeText;
    }

    public void setAnalyzeText(boolean analyzeText) {
        this.analyzeText = analyzeText;
    }
}
