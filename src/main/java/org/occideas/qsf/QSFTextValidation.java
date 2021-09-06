package org.occideas.qsf;

public enum QSFTextValidation {

    VALID_NUMBER("ValidNumber");


    QSFTextValidation(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }
}
