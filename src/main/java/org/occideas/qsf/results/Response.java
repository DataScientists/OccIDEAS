
package org.occideas.qsf.results;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    @JsonProperty("responseId")
    private String responseId;
    @JsonProperty("values")
    private Map<String, Object> values;
    @JsonProperty("labels")
    private Map<String, Object> labels;
    @JsonProperty("displayedFields")
    private List<String> displayedFields = null;
    @JsonProperty("displayedValues")
    private Map<String, Object> displayedValues;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, Object> labels) {
        this.labels = labels;
    }

    public List<String> getDisplayedFields() {
        return displayedFields;
    }

    public void setDisplayedFields(List<String> displayedFields) {
        this.displayedFields = displayedFields;
    }

    public Map<String, Object> getDisplayedValues() {
        return displayedValues;
    }

    public void setDisplayedValues(Map<String, Object> displayedValues) {
        this.displayedValues = displayedValues;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    @Override
    public String toString() {
        return "Response{" +
                "responseId='" + responseId + '\'' +
                ", values=" + values +
                ", labels=" + labels +
                ", displayedFields=" + displayedFields +
                ", displayedValues=" + displayedValues +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
