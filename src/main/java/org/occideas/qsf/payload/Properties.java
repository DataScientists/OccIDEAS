package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class Properties extends BaseQSF implements Payload {

    @JsonProperty(value = "Count")
    private int count;

}
