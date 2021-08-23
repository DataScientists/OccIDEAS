package org.occideas.qsf.request;

public class SurveyListenRequest {

    private String topics;
    private String publicationUrl;
    private boolean encrypt;

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getPublicationUrl() {
        return publicationUrl;
    }

    public void setPublicationUrl(String publicationUrl) {
        this.publicationUrl = publicationUrl;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    @Override
    public String toString() {
        return "SurveyListenRequest{" +
                "topics='" + topics + '\'' +
                ", publicationUrl='" + publicationUrl + '\'' +
                ", encrypt=" + encrypt +
                '}';
    }
}
