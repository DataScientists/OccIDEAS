package org.occideas.qsf;

public enum QSFElementTypes {

    BLOCK("BL","Survey Blocks"),FLOW("FL","Survey Flow"),OPTIONS("SO","Survey Options"),
    SCORING("SCO","Scoring"),PROJECT("PROJ","CORE"),STATISTICS("STAT","Survey Statistics"),
    QUESTIONCOUNT("QC","Survey Question Count"),RESPONSESET("RS","Response Set"),
    QUESTION("SQ","Survey Question");

    QSFElementTypes(String abbr,String desc) {
        this.abbr = abbr;
        this.desc = desc;
    }

    private String abbr;
    private String desc;

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
