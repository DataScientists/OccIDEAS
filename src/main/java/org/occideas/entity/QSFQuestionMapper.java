package org.occideas.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "QUALTRICS_QUESTION_MAPPER")
public class QSFQuestionMapper implements Serializable {

    @EmbeddedId
    private QSFQuestionMapperId id;
    private long idNode;
    private String type;
    private String questionText;
    private Long frequencyIdNode;

    public QSFQuestionMapper() {
    }

    public QSFQuestionMapper(QSFQuestionMapperId id, long idNode, String type, String questionText, Long frequencyIdNode) {
        this.id = id;
        this.idNode = idNode;
        this.type = type;
        this.questionText = questionText;
        this.frequencyIdNode = frequencyIdNode;
    }

    public QSFQuestionMapperId getId() {
        return id;
    }

    public void setId(QSFQuestionMapperId id) {
        this.id = id;
    }

    public long getIdNode() {
        return idNode;
    }

    public void setIdNode(long idNode) {
        this.idNode = idNode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Long getFrequencyIdNode() {
        return frequencyIdNode;
    }

    public void setFrequencyIdNode(Long frequencyIdNode) {
        this.frequencyIdNode = frequencyIdNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QSFQuestionMapper that = (QSFQuestionMapper) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
