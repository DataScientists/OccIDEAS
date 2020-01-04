package org.occideas.qsf;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.response.Meta;

public class BaseResponse extends BaseQSF {

    @JsonProperty(value = "meta")
    protected Meta meta;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
