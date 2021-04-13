package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostAnswerActionProperty {

    @JsonProperty(value = "Variable")
    private String variable;

    @JsonProperty(value = "ValueType")
    private String valueType = "Variable";

    @JsonProperty(value = "Value")
    private String value;

    @JsonProperty(value = "MatrixIndex")
    private String matrixIndex = "[$ROW]";

    public PostAnswerActionProperty() {
    }

    public PostAnswerActionProperty(String variable, String value) {
        this.variable = variable;
        this.value = value;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMatrixIndex() {
        return matrixIndex;
    }

    public void setMatrixIndex(String matrixIndex) {
        this.matrixIndex = matrixIndex;
    }
}
