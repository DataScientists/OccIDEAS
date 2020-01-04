package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishMetadata extends BaseQSF {

    @JsonProperty(value = "surveyID")
    private String surveyId;
    @JsonProperty(value = "versionID")
    private String versionId;
    @JsonProperty(value = "versionNumber")
    private Integer versionNumber;
    @JsonProperty(value = "description")
    private String description;
    @JsonProperty(value = "userID")
    private String userId;
    @JsonProperty(value = "creationDate")
    private String creationDate;
    @JsonProperty(value = "published")
    private boolean published;
    @JsonProperty(value = "wasPublished")
    private boolean wasPublished;

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isWasPublished() {
        return wasPublished;
    }

    public void setWasPublished(boolean wasPublished) {
        this.wasPublished = wasPublished;
    }
}
