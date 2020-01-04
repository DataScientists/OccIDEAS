package org.occideas.qsf.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SurveyUpdateRequest extends BaseQSF {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "name")
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "isActive")
    private boolean active;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "expiration")
    private Expiration expiration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "ownerId")
    private String ownerId;

    public SurveyUpdateRequest() {
    }

    public SurveyUpdateRequest(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Expiration getExpiration() {
        return expiration;
    }

    public void setExpiration(Expiration expiration) {
        this.expiration = expiration;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
