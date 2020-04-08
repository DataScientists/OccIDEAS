package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CopySurveyResult extends Result {

    @JsonProperty(value = "id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
