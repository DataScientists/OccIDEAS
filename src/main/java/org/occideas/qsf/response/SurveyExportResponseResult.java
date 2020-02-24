package org.occideas.qsf.response;


import com.fasterxml.jackson.annotation.JsonInclude;

public class SurveyExportResponseResult extends Result {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String progressId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double percentComplete;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fileId;

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
    }

    public double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
