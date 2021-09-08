package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {

    @JsonProperty("SurveyID")
    private String surveyId;
    @JsonProperty("SurveyName")
    private String surveyName;
    @JsonProperty("SurveyDescription")
    private String surveyDescription;
    @JsonProperty("SurveyOwnerID")
    private String surveyOwnerId;
    @JsonProperty("SurveyBrandID")
    private String surveyBrandId;
    @JsonProperty("DivisionID")
    private String divisionId;
    @JsonProperty("SurveyLanguage")
    private String surveyLanguage;
    @JsonProperty("SurveyActiveResponseSet")
    private String surveyActiveResponseSet;
    @JsonProperty("SurveyStatus")
    private String surveyStatus;
    @JsonProperty("SurveyStartDate")
    private String surveyStartDate;
    @JsonProperty("SurveyExpirationDate")
    private String surveyExpirationDate;
    @JsonProperty("SurveyCreationDate")
    private String surveyCreationDate;
    @JsonProperty("CreatorID")
    private String creatorId;
    @JsonProperty("LastModified")
    private String lastModified;
    @JsonProperty("LastAccessed")
    private String lastAccessed;
    @JsonProperty("LastActivated")
    private String lastActivated;
    @JsonProperty("Deleted")
    private String deleted;

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyDescription() {
        return surveyDescription;
    }

    public void setSurveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
    }

    public String getSurveyOwnerId() {
        return surveyOwnerId;
    }

    public void setSurveyOwnerId(String surveyOwnerId) {
        this.surveyOwnerId = surveyOwnerId;
    }

    public String getSurveyBrandId() {
        return surveyBrandId;
    }

    public void setSurveyBrandId(String surveyBrandId) {
        this.surveyBrandId = surveyBrandId;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getSurveyLanguage() {
        return surveyLanguage;
    }

    public void setSurveyLanguage(String surveyLanguage) {
        this.surveyLanguage = surveyLanguage;
    }

    public String getSurveyActiveResponseSet() {
        return surveyActiveResponseSet;
    }

    public void setSurveyActiveResponseSet(String surveyActiveResponseSet) {
        this.surveyActiveResponseSet = surveyActiveResponseSet;
    }

    public String getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(String surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

    public String getSurveyStartDate() {
        return surveyStartDate;
    }

    public void setSurveyStartDate(String surveyStartDate) {
        this.surveyStartDate = surveyStartDate;
    }

    public String getSurveyExpirationDate() {
        return surveyExpirationDate;
    }

    public void setSurveyExpirationDate(String surveyExpirationDate) {
        this.surveyExpirationDate = surveyExpirationDate;
    }

    public String getSurveyCreationDate() {
        return surveyCreationDate;
    }

    public void setSurveyCreationDate(String surveyCreationDate) {
        this.surveyCreationDate = surveyCreationDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(String lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public String getLastActivated() {
        return lastActivated;
    }

    public void setLastActivated(String lastActivated) {
        this.lastActivated = lastActivated;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
