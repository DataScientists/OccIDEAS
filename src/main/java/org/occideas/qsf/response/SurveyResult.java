package org.occideas.qsf.response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SurveyResult {

    private String responseId;
    private Map<String, String> labels = new HashMap<>();
    private String[] displayedFields;
    private Map<String, Integer[]> displayedValues;
    private SurveyValue values;

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public String[] getDisplayedFields() {
        return displayedFields;
    }

    public void setDisplayedFields(String[] displayedFields) {
        this.displayedFields = displayedFields;
    }

    public Map<String, Integer[]> getDisplayedValues() {
        return displayedValues;
    }

    public void setDisplayedValues(Map<String, Integer[]> displayedValues) {
        this.displayedValues = displayedValues;
    }

    public SurveyValue getValues() {
        return values;
    }

    public void setValues(SurveyValue values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "SurveyResult{" +
                "responseId='" + responseId + '\'' +
                ", labels=" + labels +
                ", displayedFields=" + Arrays.toString(displayedFields) +
                ", displayedValues=" + displayedValues +
                ", values=" + values +
                '}';
    }
}
