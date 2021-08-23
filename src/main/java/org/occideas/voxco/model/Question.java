package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.occideas.common.NodeType;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question {

    private String name;
    private Type type;
    private QuestionSettings settings;
    private TranslatedTexts translatedTexts;
    private List<PostAnswerAction> postAnswerActions;
    private List<Variable> variables;
    private String skipLogic;
    private String displayLogic;


    public Question() {
    }

    public Question(String name, Type type, TranslatedTexts translatedTexts, List<PostAnswerAction> postAnswerActions, List<Variable> variables, String displayLogic) {
        this.name = name;
        this.type = type;
        this.translatedTexts = translatedTexts;
        this.variables = variables;
        this.settings = new QuestionSettings();

        if (displayLogic != null && !"".equals(displayLogic.trim())) {
            this.displayLogic = displayLogic;
        }

        if (postAnswerActions != null && !postAnswerActions.isEmpty()) {
            this.postAnswerActions = postAnswerActions;
        }
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
        if (NodeType.Q_SIMPLE.getDescription().equalsIgnoreCase(questionType) ||
                NodeType.Q_SINGLE.getDescription().equalsIgnoreCase(questionType)) {
            return Type.RadioButton;
        }
        if (NodeType.Q_MULTIPLE.getDescription().equalsIgnoreCase(questionType)) {
            return Type.CheckBox;
        }
        return null;
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

    public List<PostAnswerAction> getPostAnswerActions() {
        return postAnswerActions;
    }

    public void setPostAnswerActions(List<PostAnswerAction> postAnswerActions) {
        this.postAnswerActions = postAnswerActions;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    public String getSkipLogic() {
        return skipLogic;
    }

    public void setSkipLogic(String skipLogic) {
        this.skipLogic = skipLogic;
    }

    public String getDisplayLogic() {
        return displayLogic;
    }

    public void setDisplayLogic(String displayLogic) {
        this.displayLogic = displayLogic;
    }
}
