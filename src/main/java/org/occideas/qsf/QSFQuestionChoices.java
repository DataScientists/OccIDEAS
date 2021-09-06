package org.occideas.qsf;

import org.occideas.qsf.payload.Choice;

import java.util.List;
import java.util.Map;

public class QSFQuestionChoices {

    private Map<String, Choice> choices;
    private List<String> choiceOrder;

    public QSFQuestionChoices(Map<String, Choice> choices, List<String> choiceOrder) {
        this.choices = choices;
        this.choiceOrder = choiceOrder;
    }

    public Map<String, Choice> getChoices() {
        return choices;
    }

    public void setChoices(Map<String, Choice> choices) {
        this.choices = choices;
    }

    public List<String> getChoiceOrder() {
        return choiceOrder;
    }

    public void setChoiceOrder(List<String> choiceOrder) {
        this.choiceOrder = choiceOrder;
    }
}
