package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.payload.SimpleQuestionPayload;

import java.util.List;

public class SurveyQuestionsResults {

    @JsonProperty("elements")
    private List<SimpleQuestionPayload> questions;

    public List<SimpleQuestionPayload> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SimpleQuestionPayload> questions) {
        this.questions = questions;
    }
}
