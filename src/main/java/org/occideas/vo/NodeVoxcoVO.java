package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeVoxcoVO {

    private Long surveyId;
    private Long idNode;
    private String surveyName;
    private Integer importFilterCount;
    private Integer importQuestionCount;
    private Integer voxcoQuestionCount;
    private Date lastValidated;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Long getIdNode() {
        return idNode;
    }

    public void setIdNode(Long idNode) {
        this.idNode = idNode;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public Integer getImportFilterCount() {
        return importFilterCount;
    }

    public void setImportFilterCount(Integer importFilterCount) {
        this.importFilterCount = importFilterCount;
    }

    public Integer getImportQuestionCount() {
        return importQuestionCount;
    }

    public void setImportQuestionCount(Integer importQuestionCount) {
        this.importQuestionCount = importQuestionCount;
    }

    public Integer getVoxcoQuestionCount() {
        return voxcoQuestionCount;
    }

    public void setVoxcoQuestionCount(Integer voxcoQuestionCount) {
        this.voxcoQuestionCount = voxcoQuestionCount;
    }

    public Date getLastValidated() {
        return lastValidated;
    }

    public void setLastValidated(Date lastValidated) {
        this.lastValidated = lastValidated;
    }
}
