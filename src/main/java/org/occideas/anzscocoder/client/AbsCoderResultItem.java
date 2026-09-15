package org.occideas.anzscocoder.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsCoderResultItem {

    private String codeCategory;
    private String codeLabel;
    private double codeConfidence;

    public String getCodeCategory() {
        return codeCategory;
    }

    public void setCodeCategory(String codeCategory) {
        this.codeCategory = codeCategory;
    }

    public String getCodeLabel() {
        return codeLabel;
    }

    public void setCodeLabel(String codeLabel) {
        this.codeLabel = codeLabel;
    }

    public double getCodeConfidence() {
        return codeConfidence;
    }

    public void setCodeConfidence(double codeConfidence) {
        this.codeConfidence = codeConfidence;
    }
}
