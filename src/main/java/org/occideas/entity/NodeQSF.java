package org.occideas.entity;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "node_qsf")
public class NodeQSF implements Serializable {

    @Id
    private String surveyId;
    private long idNode;
    private String results;
    @UpdateTimestamp
    private Date lastUpdated;

    public NodeQSF() {
    }

    public NodeQSF(String surveyId, long idNode, String results) {
        this.surveyId = surveyId;
        this.idNode = idNode;
        this.results = results;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public long getIdNode() {
        return idNode;
    }

    public void setIdNode(long idNode) {
        this.idNode = idNode;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
