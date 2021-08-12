package org.occideas.entity;

import org.occideas.qsf.subscriber.constant.QualtricsSubscriptionStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "QUALTRICS_SURVEY_SUBSCRIPTION")
public class QualtricsSurveySubscription implements Serializable {

    @Id
    private String subscriptionId;
    private String surveyId;
    @Enumerated(EnumType.STRING)
    private QualtricsSubscriptionStatus status;
    private LocalDateTime subscriptionDate;

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public QualtricsSubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(QualtricsSubscriptionStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(LocalDateTime subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    @Override
    public String toString() {
        return "QualtricsSurveySubscription{" +
                "subscriptionId='" + subscriptionId + '\'' +
                ", surveyId='" + surveyId + '\'' +
                ", status=" + status +
                ", subscriptionDate=" + subscriptionDate +
                '}';
    }
}
