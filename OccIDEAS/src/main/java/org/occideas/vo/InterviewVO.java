package org.occideas.vo;

import java.util.List;

/**
 * Created by quangnn on 2/17/2016.
 */

public class InterviewVO {
    private String freeText;
    private String type;
    private long singleAnswerId;
    private List<Long> multipleAnswerId;
    private long interviewId;
    private long moduleId;
    private long questionId;

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }


    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSingleAnswerId() {
        return singleAnswerId;
    }

    public void setSingleAnswerId(long singleAnswerId) {
        this.singleAnswerId = singleAnswerId;
    }

    public List<Long> getMultipleAnswerId() {
        return multipleAnswerId;
    }

    public void setMultipleAnswerId(List<Long> multipleAnswerId) {
        this.multipleAnswerId = multipleAnswerId;
    }

    public long getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(long interviewId) {
        this.interviewId = interviewId;
    }
}
