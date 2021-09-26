package org.occideas.qsf.service;

import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;

import java.util.HashMap;
import java.util.Map;

public class QuestionAnswerWrapper {

    private String qualtricsQID;
    private Question question;
    private PossibleAnswer dependsOn;
    private Map<PossibleAnswer, String> choiceSelectors = new HashMap<>();
    private QuestionAnswerWrapper parent;

    public QuestionAnswerWrapper(Question question, PossibleAnswer dependsOn) {
        this.question = question;
        this.dependsOn = dependsOn;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public PossibleAnswer getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(PossibleAnswer dependsOn) {
        this.dependsOn = dependsOn;
    }

    public Map<PossibleAnswer, String> getChoiceSelectors() {
        return choiceSelectors;
    }

    public void setChoiceSelectors(Map<PossibleAnswer, String> choiceSelectors) {
        this.choiceSelectors = choiceSelectors;
    }

    public QuestionAnswerWrapper getParent() {
        return parent;
    }

    public void setParent(QuestionAnswerWrapper parent) {
        this.parent = parent;
    }

    public String getQualtricsQID() {
        return qualtricsQID;
    }

    public void setQualtricsQID(String qualtricsQID) {
        this.qualtricsQID = qualtricsQID;
    }
}
