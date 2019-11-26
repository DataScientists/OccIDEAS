package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

public class StatisticsPayload extends BaseQSF implements Payload {

    @JsonProperty(value = "MobileCompatible")
    private boolean mobileComponents;
    @JsonProperty(value = "ID")
    private String id;

    public StatisticsPayload() {
    }

    public StatisticsPayload(boolean mobileComponents, String id) {
        this.mobileComponents = mobileComponents;
        this.id = id;
    }

    public boolean isMobileComponents() {
        return mobileComponents;
    }

    public void setMobileComponents(boolean mobileComponents) {
        this.mobileComponents = mobileComponents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
