package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssessmentResult<T> {

    private String shiftHours;
    private String totalPartialExposure;
    private String autoExposureLevel;
    private String peak;
    private String totalFrequency;
    private List<T> results = new ArrayList<>();

    public AssessmentResult(String shiftHours, String totalPartialExposure, String autoExposureLevel, String peak, String totalFrequency) {
        this.shiftHours = shiftHours;
        this.totalPartialExposure = totalPartialExposure;
        this.autoExposureLevel = autoExposureLevel;
        this.peak = peak;
        this.totalFrequency = totalFrequency;
    }

    public String getShiftHours() {
        return shiftHours;
    }

    public void setShiftHours(String shiftHours) {
        this.shiftHours = shiftHours;
    }

    public String getTotalPartialExposure() {
        return totalPartialExposure;
    }

    public void setTotalPartialExposure(String totalPartialExposure) {
        this.totalPartialExposure = totalPartialExposure;
    }

    public String getAutoExposureLevel() {
        return autoExposureLevel;
    }

    public void setAutoExposureLevel(String autoExposureLevel) {
        this.autoExposureLevel = autoExposureLevel;
    }

    public String getPeak() {
        return peak;
    }

    public void setPeak(String peak) {
        this.peak = peak;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public String getTotalFrequency() {
        return totalFrequency;
    }

    public void setTotalFrequency(String totalFrequency) {
        this.totalFrequency = totalFrequency;
    }
}
