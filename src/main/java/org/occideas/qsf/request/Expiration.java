package org.occideas.qsf.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class Expiration extends BaseQSF {

    @JsonProperty(value = "startDate")
    private String startDate;
    @JsonProperty(value = "endDate")
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
