package org.occideas.qsf;

import org.occideas.common.NodeType;

public enum QuestionSelector {

    MULTIPLE(NodeType.Q_LINKEDMODULE.getDescription(), QSFQuestionSelector.MAVR.name()),
    FREQUENCY(NodeType.Q_FREQUENCY.getDescription(), QSFQuestionSelector.DL.name());

    QuestionSelector(String type, String selector) {
        this.type = type;
        this.selector = selector;
    }

    private String type;
    private String selector;

    public String getType() {
        return type;
    }

    public String getSelector() {
        return selector;
    }

    public static String get(String type){
        for(QuestionSelector questionSelector: QuestionSelector.values()){
            if(questionSelector.type.equals(type)){
                return questionSelector.selector;
            }
        }
        return QSFQuestionSelector.SAVR.name();
    }
}
