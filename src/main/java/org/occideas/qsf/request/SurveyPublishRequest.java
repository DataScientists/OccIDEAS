package org.occideas.qsf.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class SurveyPublishRequest extends BaseQSF {

    @JsonProperty(value = "Description")
    private String description;
    @JsonProperty(value = "Published")
    private boolean publish;

    public SurveyPublishRequest() {
    }

    public SurveyPublishRequest(String description, boolean publish) {
        this.description = description;
        this.publish = publish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }
}
