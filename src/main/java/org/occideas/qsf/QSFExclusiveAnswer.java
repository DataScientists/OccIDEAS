package org.occideas.qsf;

public enum QSFExclusiveAnswer {

    NO("No"),
    DONT_KNOW("Don't Know"),
    NONE_OF_THE_ABOVE("None of the Above");


    QSFExclusiveAnswer(String text) {
        this.text = text;
    }

    private String text;

    public String getText() {
        return text;
    }

    public static boolean isAnswerExclusive(String answer) {
        for (QSFExclusiveAnswer exclusiveAnswer : QSFExclusiveAnswer.values()) {
            if (exclusiveAnswer.getText().toUpperCase().equalsIgnoreCase(answer)) {
                return true;
            }
        }
        return false;
    }
}
