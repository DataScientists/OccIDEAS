package org.occideas.qsf.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionAnswerResponse {

    private String qsfQuestionId;
    private String occideasAnswerIdNode;
    private String freeTextAnswer;

    public QuestionAnswerResponse(String qsfQuestionId, String occideasAnswerIdNode) {
        this.qsfQuestionId = qsfQuestionId;
        this.occideasAnswerIdNode = occideasAnswerIdNode;
    }

    public QuestionAnswerResponse(String qsfQuestionId, String occideasAnswerIdNode, String freeTextAnswer) {
        this.qsfQuestionId = qsfQuestionId;
        this.occideasAnswerIdNode = occideasAnswerIdNode;
        this.freeTextAnswer = freeTextAnswer;
    }

    public String getQsfQuestionId() {
        return qsfQuestionId;
    }

    public void setQsfQuestionId(String qsfQuestionId) {
        this.qsfQuestionId = qsfQuestionId;
    }

    public String getOccideasAnswerIdNode() {
        return occideasAnswerIdNode;
    }

    public void setOccideasAnswerIdNode(String occideasAnswerIdNode) {
        this.occideasAnswerIdNode = occideasAnswerIdNode;
    }

    public String getFreeTextAnswer() {
        return freeTextAnswer;
    }

    public void setFreeTextAnswer(String freeTextAnswer) {
        this.freeTextAnswer = freeTextAnswer;
    }

    @Override
    public String toString() {
        return "QuestionAnswerResponse{" +
                "qsfQuestionId='" + qsfQuestionId + '\'' +
                ", occideasAnswerIdNode='" + occideasAnswerIdNode + '\'' +
                ", freeTextAnswer='" + freeTextAnswer + '\'' +
                '}';
    }
}
