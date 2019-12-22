package org.occideas.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "node_qsf")
public class NodeQSF implements Serializable {

    @Id
    private String surveyId;
    private long idNode;

    public NodeQSF() {
    }

    public NodeQSF(String surveyId, long idNode) {
        this.surveyId = surveyId;
        this.idNode = idNode;
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
}
