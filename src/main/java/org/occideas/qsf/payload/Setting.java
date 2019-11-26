package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Setting {

    @JsonProperty(value = "ForceResponse")
    private String forceResponse;
    @JsonProperty(value = "ForceResponseType")
    private String forceResponseType;
    @JsonProperty(value = "Type")
    private String type;

    public Setting() {
    }

    public Setting(String forceResponse, String forceResponseType, String type) {
        this.forceResponse = forceResponse;
        this.forceResponseType = forceResponseType;
        this.type = type;
    }

    public String getForceResponse() {
        return forceResponse;
    }

    public void setForceResponse(String forceResponse) {
        this.forceResponse = forceResponse;
    }

    public String getForceResponseType() {
        return forceResponseType;
    }

    public void setForceResponseType(String forceResponseType) {
        this.forceResponseType = forceResponseType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
