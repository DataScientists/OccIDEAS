package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta extends BaseQSF {

    @JsonProperty(value = "httpStatus")
    private String httpStatus;
    @JsonProperty(value = "error")
    private Error error;

    public Meta() {
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
