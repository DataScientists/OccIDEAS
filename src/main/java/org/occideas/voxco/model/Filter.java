package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter {

    @JsonProperty(value = "")
    private Long id;

    @JsonProperty(value = "DispositionResults")
    private List<String> dispositionResults;

    @JsonProperty(value = "EmailStatus")
    private String emailStatus;

    @JsonProperty(value = "SMSStatus")
    private String sMSStatus;

    @JsonProperty(value = "LastActivity")
    private LastActivity lastActivity;

    @JsonProperty(value = "Languages")
    private List<String> languages;

    @JsonProperty(value = "Devices")
    private List<String> devices;

    @JsonProperty(value = "Samples")
    private List<Integer> samples;

    @JsonProperty(value = "UserId")
    private Long userId;

    @JsonProperty(value = "Expression")
    private String expression;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getDispositionResults() {
        return dispositionResults;
    }

    public void setDispositionResults(List<String> dispositionResults) {
        this.dispositionResults = dispositionResults;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getsMSStatus() {
        return sMSStatus;
    }

    public void setsMSStatus(String sMSStatus) {
        this.sMSStatus = sMSStatus;
    }

    public LastActivity getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LastActivity lastActivity) {
        this.lastActivity = lastActivity;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }

    public List<Integer> getSamples() {
        return samples;
    }

    public void setSamples(List<Integer> samples) {
        this.samples = samples;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
