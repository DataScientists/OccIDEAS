package org.occideas.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Table(name = "QUALTRICS_SURVEY")
public class QualtricsSurvey implements Serializable {

    @Id
    private String responseId;
    private String brandId;
    private String topic;
    private String surveyId;
    private LocalDateTime completedDate;
    private String qualtricsStatus;
    @Lob
    private byte[] response;
    private boolean isProcessed;

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public String getQualtricsStatus() {
        return qualtricsStatus;
    }

    public void setQualtricsStatus(String qualtricsStatus) {
        this.qualtricsStatus = qualtricsStatus;
    }

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    @Override
    public String toString() {
        return "QualtricsSurvey{" +
                "responseId='" + responseId + '\'' +
                ", brandId='" + brandId + '\'' +
                ", topic='" + topic + '\'' +
                ", surveyId='" + surveyId + '\'' +
                ", completedDate=" + completedDate +
                ", qualtricsStatus='" + qualtricsStatus + '\'' +
                ", response=" + Arrays.toString(response) +
                ", isProcessed=" + isProcessed +
                '}';
    }
}
