package org.occideas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * One candidate-module option in the multiple-choice question shown when an ANZSCO code
 * returned by the ABS Coder lookup is too short/coarse to resolve to a single OccIDEAS job
 * module. All rows sharing the same anzscoPrefix are shown together as one question.
 */
@Entity
public class AnzscoDisambiguationOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String anzscoPrefix;
    private String questionText;
    private String moduleCode;
    private String optionLabel;
    private int sequence;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnzscoPrefix() {
        return anzscoPrefix;
    }

    public void setAnzscoPrefix(String anzscoPrefix) {
        this.anzscoPrefix = anzscoPrefix;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
