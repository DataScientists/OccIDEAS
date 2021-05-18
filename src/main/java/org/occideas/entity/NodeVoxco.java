package org.occideas.entity;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "node_voxco")
public class NodeVoxco implements Serializable {

    @Id
    private Long surveyId;
    private Long idNode;
    private String surveyName;
    private int deleted;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date lastUpdated;
    private Long extractionId;
    private String extractionStatus;
    private Long fileId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date extractionStart;
    @Temporal(TemporalType.TIMESTAMP)
    private Date extractionEnd;
    private String resultPath;
    private Integer importFilterCount;
    private Integer importQuestionCount;
    private Integer voxcoQuestionCount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastValidated;

    public NodeVoxco() {
    }

    public NodeVoxco(Long surveyId, Long idNode, String surveyName) {
        this.surveyId = surveyId;
        this.idNode = idNode;
        this.surveyName = surveyName;
    }

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

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getExtractionId() {
        return extractionId;
    }

    public void setExtractionId(Long extractionId) {
        this.extractionId = extractionId;
    }

    public String getExtractionStatus() {
        return extractionStatus;
    }

    public void setExtractionStatus(String extractionStatus) {
        this.extractionStatus = extractionStatus;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Date getExtractionStart() {
        return extractionStart;
    }

    public void setExtractionStart(Date extractionStart) {
        this.extractionStart = extractionStart;
    }

    public Date getExtractionEnd() {
        return extractionEnd;
    }

    public void setExtractionEnd(Date extractionEnd) {
        this.extractionEnd = extractionEnd;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
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
