package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class ProjectPayload extends BaseQSF implements Payload {

    @JsonProperty(value = "ProjectCategory")
    private String projectCategory;
    @JsonProperty(value = "SchemaVersion")
    private String schemaVersion;

    public ProjectPayload() {
    }

    public ProjectPayload(String projectCategory, String schemaVersion) {
        this.projectCategory = projectCategory;
        this.schemaVersion = schemaVersion;
    }

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
}
