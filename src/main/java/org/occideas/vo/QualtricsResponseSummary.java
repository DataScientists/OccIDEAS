package org.occideas.vo;

import java.util.Map;

public class QualtricsResponseSummary {

    private Map<String, ResponseSummary> summary;

    public Map<String, ResponseSummary> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, ResponseSummary> summary) {
        this.summary = summary;
    }
}
