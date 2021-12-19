package org.occideas.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class QSFQuestionMapperId implements Serializable {

    private String surveyId;
    private String questionId;

    public QSFQuestionMapperId() {
    }

    public QSFQuestionMapperId(String surveyId, String questionId) {
        this.surveyId = surveyId;
        this.questionId = questionId;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QSFQuestionMapperId that = (QSFQuestionMapperId) o;
        return surveyId.equals(that.surveyId) && questionId.equals(that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surveyId, questionId);
    }
}
