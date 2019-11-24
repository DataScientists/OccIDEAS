package org.occideas.qsf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockElement {

    @JsonProperty(value = "Type")
    private String type;
    @JsonProperty(value = "QuestionID")
    private String questionId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
