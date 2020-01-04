package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SurveyPublishResult {

    @JsonProperty(value = "metadata")
    private PublishMetadata publishMetadata;

    public PublishMetadata getPublishMetadata() {
        return publishMetadata;
    }

    public void setPublishMetadata(PublishMetadata publishMetadata) {
        this.publishMetadata = publishMetadata;
    }
}
