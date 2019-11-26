package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {

    @JsonProperty(value = "QuestionDescriptionOption")
    private String questionDescriptionOption;

    public Configuration() {
    }

    public Configuration(String questionDescriptionOption) {
        this.questionDescriptionOption = questionDescriptionOption;
    }

    public String getQuestionDescriptionOption() {
        return questionDescriptionOption;
    }

    public void setQuestionDescriptionOption(String questionDescriptionOption) {
        this.questionDescriptionOption = questionDescriptionOption;
    }
}
