package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSummary {

    private String questionIdNode;
    private String question;
    private String answerIdNode;
    private String answer;
    private String questionType;
    private List<RuleVO> firedRules;
    private List<RuleVO> autoAssessedRules;

    public String getQuestionIdNode() {
        return questionIdNode;
    }

    public void setQuestionIdNode(String questionIdNode) {
        this.questionIdNode = questionIdNode;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerIdNode() {
        return answerIdNode;
    }

    public void setAnswerIdNode(String answerIdNode) {
        this.answerIdNode = answerIdNode;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<RuleVO> getFiredRules() {
        return firedRules;
    }

    public void setFiredRules(List<RuleVO> firedRules) {
        this.firedRules = firedRules;
    }

    public List<RuleVO> getAutoAssessedRules() {
        return autoAssessedRules;
    }

    public void setAutoAssessedRules(List<RuleVO> autoAssessedRules) {
        this.autoAssessedRules = autoAssessedRules;
    }
}
