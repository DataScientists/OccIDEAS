package org.occideas.qsf.response;

public class SurveyLink {

    private String surveyId;
    private String expirationDate;
    private String linkType;

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    @Override
    public String toString() {
        return "SurveyLink{" +
                "surveyId='" + surveyId + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", linkType='" + linkType + '\'' +
                '}';
    }
}
