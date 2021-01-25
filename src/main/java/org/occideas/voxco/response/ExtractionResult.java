package org.occideas.voxco.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtractionResult {

    @JsonProperty(value = "ExtractionId")
    private Long extractionId;

    @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "Status")
    private String status;

    @JsonProperty(value = "FileId")
    private Long fileId;

    public Long getExtractionId() {
        return extractionId;
    }

    public void setExtractionId(Long extractionId) {
        this.extractionId = extractionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
