package org.occideas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class QualtricsSurvey implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String distributionId;
    private String surveyId;
    private String participantEmail;
    private LocalDateTime sentAssessmentDate;
    private LocalDateTime createdDate;
    private LocalDateTime surveyExpiryDate;
    private String ownerId;
    private String orgId;
    private LocalDateTime distributionSentDate;
    private String mailingListId;
    private String libraryId;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(String distributionId) {
        this.distributionId = distributionId;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public LocalDateTime getSentAssessmentDate() {
        return sentAssessmentDate;
    }

    public void setSentAssessmentDate(LocalDateTime sentAssessmentDate) {
        this.sentAssessmentDate = sentAssessmentDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getSurveyExpiryDate() {
        return surveyExpiryDate;
    }

    public void setSurveyExpiryDate(LocalDateTime surveyExpiryDate) {
        this.surveyExpiryDate = surveyExpiryDate;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public LocalDateTime getDistributionSentDate() {
        return distributionSentDate;
    }

    public void setDistributionSentDate(LocalDateTime distributionSentDate) {
        this.distributionSentDate = distributionSentDate;
    }

    public String getMailingListId() {
        return mailingListId;
    }

    public void setMailingListId(String mailingListId) {
        this.mailingListId = mailingListId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
