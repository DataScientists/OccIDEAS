package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DistributionListElement {

    private String id;
    private String parentDistributionId;
    private String ownerId;
    private String organizationId;
    private String requestStatus;
    private String requestType;
    private String sendDate;
    private String createdDate;
    private String modifiedDate;
    private EmailHeader headers;
    private EmailRecipients recipients;
    private Message message;
    private SurveyLink surveyLink;
    private String embeddedData;
    private Stats stats;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentDistributionId() {
        return parentDistributionId;
    }

    public void setParentDistributionId(String parentDistributionId) {
        this.parentDistributionId = parentDistributionId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public EmailHeader getHeaders() {
        return headers;
    }

    public void setHeaders(EmailHeader headers) {
        this.headers = headers;
    }

    public EmailRecipients getRecipients() {
        return recipients;
    }

    public void setRecipients(EmailRecipients recipients) {
        this.recipients = recipients;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public SurveyLink getSurveyLink() {
        return surveyLink;
    }

    public void setSurveyLink(SurveyLink surveyLink) {
        this.surveyLink = surveyLink;
    }

    public String getEmbeddedData() {
        return embeddedData;
    }

    public void setEmbeddedData(String embeddedData) {
        this.embeddedData = embeddedData;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "DistributionListResult{" +
                "id='" + id + '\'' +
                ", parentDistributionId='" + parentDistributionId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", requestStatus='" + requestStatus + '\'' +
                ", requestType='" + requestType + '\'' +
                ", sendDate='" + sendDate + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", headers=" + headers +
                ", recipients=" + recipients +
                ", message=" + message +
                ", surveyLink=" + surveyLink +
                ", embeddedData='" + embeddedData + '\'' +
                ", stats=" + stats +
                '}';
    }

}
