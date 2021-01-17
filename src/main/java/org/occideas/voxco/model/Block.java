package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Block {

    private String name;
    private BlockSettings settings;
    private List<Question> questions;

    public Block() {
    }

    public Block(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BlockSettings getSettings() {
        return settings;
    }

    public void setSettings(BlockSettings settings) {
        this.settings = settings;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
