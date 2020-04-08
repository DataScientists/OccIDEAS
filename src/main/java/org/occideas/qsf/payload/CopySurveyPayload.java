package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class CopySurveyPayload extends BaseQSF implements Payload {

    @JsonProperty("projectName")
    private String projectName;

    public CopySurveyPayload(String projectName) {
        this.projectName = projectName;
    }

    public CopySurveyPayload() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
