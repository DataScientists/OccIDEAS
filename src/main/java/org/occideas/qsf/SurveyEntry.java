package org.occideas.qsf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SurveyEntry {

    @JsonProperty(value = "SurveyID")
    private String surveyId;
    @JsonProperty(value = "SurveyName")
    private String surveyName;
    @JsonProperty(value = "SurveyDescription")
    private String surveyDescription;
    @JsonProperty(value = "SurveyOwnerID")
    private String surveyOwnerID;
    @JsonProperty(value = "SurveyBrandID")
    private String surveyBrandID;
    @JsonProperty(value = "DivisionID")
    private String divisionID;
    @JsonProperty(value = "SurveyLanguage")
    private String surveyLanguage;
    @JsonProperty(value = "SurveyActiveResponseSet")
    private String surveyActiveResponseSet;
    @JsonProperty(value = "SurveyStatus")
    private String surveyStatus;
    @JsonProperty(value = "SurveyStartDate")
    private String surveyStartDate;
    @JsonProperty(value = "SurveyExpirationDate")
    private String surveyExpirationDate;
    @JsonProperty(value = "SurveyCreationDate")
    private String surveyCreationDate;
    @JsonProperty(value = "CreatorID")
    private String creatorID;
    @JsonProperty(value = "LastModified")
    private String lastModified;
    @JsonProperty(value = "LastAccessed")
    private String lastAccessed;
    @JsonProperty(value = "LastActivated")
    private String lastActivated;
    @JsonProperty(value = "Deleted")
    private String deleted;

    public SurveyEntry() {
    }

    public SurveyEntry(String surveyId, String surveyName, String surveyDescription, String surveyOwnerID, String surveyBrandID, String divisionID, String surveyLanguage, String surveyActiveResponseSet, String surveyStatus, String surveyStartDate, String surveyExpirationDate, String surveyCreationDate, String creatorID, String lastModified, String lastAccessed, String lastActivated, String deleted) {
        this.surveyId = surveyId;
        this.surveyName = surveyName;
        this.surveyDescription = surveyDescription;
        this.surveyOwnerID = surveyOwnerID;
        this.surveyBrandID = surveyBrandID;
        this.divisionID = divisionID;
        this.surveyLanguage = surveyLanguage;
        this.surveyActiveResponseSet = surveyActiveResponseSet;
        this.surveyStatus = surveyStatus;
        this.surveyStartDate = surveyStartDate;
        this.surveyExpirationDate = surveyExpirationDate;
        this.surveyCreationDate = surveyCreationDate;
        this.creatorID = creatorID;
        this.lastModified = lastModified;
        this.lastAccessed = lastAccessed;
        this.lastActivated = lastActivated;
        this.deleted = deleted;
    }

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

    public String getSurveyOwnerID() {
        return surveyOwnerID;
    }

    public void setSurveyOwnerID(String surveyOwnerID) {
        this.surveyOwnerID = surveyOwnerID;
    }

    public String getSurveyBrandID() {
        return surveyBrandID;
    }

    public void setSurveyBrandID(String surveyBrandID) {
        this.surveyBrandID = surveyBrandID;
    }

    public String getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(String divisionID) {
        this.divisionID = divisionID;
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

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
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

    private String encloseQuote(String value){
        if(value != null) {
            return "\""+value+"\"";
        }
        return null;
    }

    @Override
    public String toString() {
        return "\"SurveyEntry\":{" +
                "\"SurveyID\":"+ encloseQuote(surveyId) +
                ",\"SurveyName\":" + encloseQuote(surveyName) +
                ",\"SurveyDescription\":" + encloseQuote(surveyDescription) +
                ",\"SurveyOwnerID\":" + encloseQuote(surveyOwnerID) +
                ",\"SurveyBrandID\":" + encloseQuote(surveyBrandID) +
                ",\"DivisionID\":" + encloseQuote(divisionID) +
                ",\"SurveyLanguage\":" + encloseQuote(surveyLanguage) +
                ",\"SurveyActiveResponseSet\":" + encloseQuote(surveyActiveResponseSet) +
                ",\"SurveyStatus\":" + encloseQuote(surveyStatus) +
                ",\"SurveyStartDate\":" + encloseQuote(surveyStartDate) +
                ",\"SurveyExpirationDate\":" + encloseQuote(surveyExpirationDate) +
                ",\"SurveyCreationDate\":" + encloseQuote(surveyCreationDate) +
                ",\"CreatorID\":" + encloseQuote(creatorID) +
                ",\"LastModified\":" + encloseQuote(lastModified) +
                ",\"LastAccessed\":" + encloseQuote(lastAccessed) +
                ",\"LastActivated\":" + encloseQuote(lastActivated) +
                ",\"Deleted\":" + encloseQuote(deleted)  +
                '}';
    }
}
