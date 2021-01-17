package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.occideas.entity.Constant;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question {

    private String name;
    private Type type;
    private QuestionSettings settings;
    private TranslatedTexts translatedTexts;
    private List<Variable> variables;

    public Question() {
    }

    public Question(String name, Type type, TranslatedTexts translatedTexts, List<Variable> variables) {
        this.name = name;
        this.type = type;
        this.translatedTexts = translatedTexts;
        this.variables = variables;
        this.settings = new QuestionSettings();
    }

    public enum Type {
        RadioButton("RADIO"),
        CheckBox("CHECK"),
        TextAnswer("TEXT");

        private String variable;

        Type(String variable) {
            this.variable = variable;
        }

        public String getVariable() {
            return variable;
        }
    }

    public static Type getType(String questionType) {
        switch (questionType) {
            case Constant.Q_SIMPLE:
            case Constant.Q_SINGLE:
                return Type.RadioButton;
            case Constant.Q_MULTIPLE:
                return Type.CheckBox;
            default:
                return Type.TextAnswer;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public QuestionSettings getSettings() {
        return settings;
    }

    public void setSettings(QuestionSettings settings) {
        this.settings = settings;
    }

    public TranslatedTexts getTranslatedTexts() {
        return translatedTexts;
    }

    public void setTranslatedTexts(TranslatedTexts translatedTexts) {
        this.translatedTexts = translatedTexts;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }
}
