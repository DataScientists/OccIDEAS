package org.occideas.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "QUALTRICS_SURVEY_RESPONSE")
public class QualtricsSurveyResponse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String surveyId;
    private String responseId;
    private String ipAddress;
    private String progress;
    private String duration;
    private String finished;
    private String recordedDate;
    private String locationLatitude;
    private String locationLongitude;
    @Lob
    private byte[] questionAnswers;
    @Lob
    private byte[] results;

    public QualtricsSurveyResponse() {
    }

    public QualtricsSurveyResponse(String surveyId, String responseId, String ipAddress, String progress, String duration, String finished, String recordedDate, String locationLatitude, String locationLongitude, byte[] questionAnswers, byte[] results) {
        this.surveyId = surveyId;
        this.responseId = responseId;
        this.ipAddress = ipAddress;
        this.progress = progress;
        this.duration = duration;
        this.finished = finished;
        this.recordedDate = recordedDate;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.questionAnswers = questionAnswers;
        this.results = results;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(String recordedDate) {
        this.recordedDate = recordedDate;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public byte[] getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(byte[] questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public byte[] getResults() {
        return results;
    }

    public void setResults(byte[] results) {
        this.results = results;
    }
}
