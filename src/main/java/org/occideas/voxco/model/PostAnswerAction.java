package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostAnswerAction {

    private PostAnswerActionProperty properties;

    private String type;

    private String condition;

    public PostAnswerAction() {
    }

    public PostAnswerAction(PostAnswerActionProperty properties, String condition) {
        this.properties = properties;
        this.condition = condition;
        this.type = "SetVariableValue";
    }

    public PostAnswerAction(PostAnswerActionProperty properties, String type, String condition) {
        this.properties = properties;
        this.type = type;
        this.condition = condition;
    }

    public PostAnswerActionProperty getProperties() {
        return properties;
    }

    public void setProperties(PostAnswerActionProperty properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
