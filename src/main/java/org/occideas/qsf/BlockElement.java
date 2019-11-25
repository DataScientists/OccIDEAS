package org.occideas.qsf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockElement extends BaseQSF{

    @JsonProperty(value = "Type")
    private String type;
    @JsonProperty(value = "QuestionID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String questionId;

    public BlockElement() {
    }

    public BlockElement(String type, String questionId) {
        this.type = type;
        this.questionId = questionId;
    }

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
