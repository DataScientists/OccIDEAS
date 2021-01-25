package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LastActivity {

    @JsonProperty(value = "UseCurrentDate")
    private Boolean useCurrentDate;

    @JsonProperty(value = "Begin")
    private Date begin;

    @JsonProperty(value = "End")
    private Date end;

    public Boolean getUseCurrentDate() {
        return useCurrentDate;
    }

    public void setUseCurrentDate(Boolean useCurrentDate) {
        this.useCurrentDate = useCurrentDate;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
