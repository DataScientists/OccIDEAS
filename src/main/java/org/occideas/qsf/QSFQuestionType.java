package org.occideas.qsf;

public enum QSFQuestionType {

    SINGLE_CHOICE(QSFQuestionSelector.SAVR.name(), QSFQuestionSubSelector.TX.name(), "MC"),
    MULTIPLE_CHOICE(QSFQuestionSelector.MAVR.name(), QSFQuestionSubSelector.TX.name(), "MC"),
    Matrix(QSFQuestionSelector.Likert.name(), QSFQuestionSubSelector.SingleAnswer.name(), "Matrix"),
    Captcha(null, null, null),
    CONSTANT_SUM(QSFQuestionSelector.VRTL.name(), QSFQuestionSubSelector.TX.name(), "CS"),
    DESCRIPTIVE_TEXT(QSFQuestionSelector.TB.name(), null, "DB"),
    TEXT_GRAPHIC(QSFQuestionSelector.GRB.name(), QSFQuestionSubSelector.WOTXB.name(), "DB"),
    DRILL_DOWN(null, null, "DD"),
    Draw(null, null, null),
    DynamicMatrix(null, null, null),
    FileUpload(null, null, null),
    GAP(null, null, null),
    HeatMap(null, null, null),
    HIGHLIGHT(QSFQuestionSelector.Text.name(), null, "HL"),
    HotSpot(null, null, null),
    Meta(null, null, null),
    PGR(null, null, null),
    RO(null, null, null),
    SBS(null, null, null),
    Slider(QSFQuestionSelector.HSLIDER.name(), null, "Slider"),
    SS(null, null, null),
    TEXT_ENTRY(QSFQuestionSelector.SL.name(), null, "TE"),
    TEXT_ENTRY_FORM(QSFQuestionSelector.FORM.name(), null, "TE"),
    Timing(QSFQuestionSelector.PageTimer.name(), null, "Timing"),
    TreeSelect(null, null, null);

    private String selector;
    private String subSelector;
    private String type;

    QSFQuestionType(String selector, String subSelector, String type) {
        this.selector = selector;
        this.subSelector = subSelector;
        this.type = type;
    }

    public String getSelector() {
        return selector;
    }

    public String getSubSelector() {
        return subSelector;
    }

    public String getType() {
        return type;
    }
}
