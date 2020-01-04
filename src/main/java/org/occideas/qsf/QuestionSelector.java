package org.occideas.qsf;

public enum QuestionSelector {

    MULTIPLE("Q_multiple","MAVR");

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
        return "SAVR";
    }
}
