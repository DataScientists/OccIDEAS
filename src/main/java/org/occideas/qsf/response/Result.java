package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class Result extends BaseQSF {

    @JsonProperty(value = "id")
    private String id;

    public Result() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
