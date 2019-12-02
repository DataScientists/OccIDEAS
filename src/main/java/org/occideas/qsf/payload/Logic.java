package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Logic {

    @JsonProperty(value = "LogicType")
    private String logicType;
    @JsonProperty(value = "QuestionID")
    private String questionId;
    @JsonProperty(value = "QuestionIsInLoop")
    private String questionIsInLoop;
    @JsonProperty(value = "ChoiceLocator")
    private String choiceLocator;
    @JsonProperty(value = "Operator")
    private String operator;
    @JsonProperty(value = "QuestionIDFromLocator")
    private String questionIdFromLocator;
    @JsonProperty(value = "LeftOperand")
    private String leftOperand;
    @JsonProperty(value = "Type")
    private String type;
    @JsonProperty(value = "Description")
    private String description;

    public Logic() {
    }

    public Logic(String logicType, String questionId, String questionIsInLoop, String choiceLocator, String operator, String questionIdFromLocator, String leftOperand, String type, String description) {
        this.logicType = logicType;
        this.questionId = questionId;
        this.questionIsInLoop = questionIsInLoop;
        this.choiceLocator = choiceLocator;
        this.operator = operator;
        this.questionIdFromLocator = questionIdFromLocator;
        this.leftOperand = leftOperand;
        this.type = type;
        this.description = description;
    }

    public String getLogicType() {
        return logicType;
    }

    public void setLogicType(String logicType) {
        this.logicType = logicType;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionIsInLoop() {
        return questionIsInLoop;
    }

    public void setQuestionIsInLoop(String questionIsInLoop) {
        this.questionIsInLoop = questionIsInLoop;
    }

    public String getChoiceLocator() {
        return choiceLocator;
    }

    public void setChoiceLocator(String choiceLocator) {
        this.choiceLocator = choiceLocator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getQuestionIdFromLocator() {
        return questionIdFromLocator;
    }

    public void setQuestionIdFromLocator(String questionIdFromLocator) {
        this.questionIdFromLocator = questionIdFromLocator;
    }

    public String getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(String leftOperand) {
        this.leftOperand = leftOperand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
