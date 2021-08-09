package org.occideas.qsf.response;

public class SurveyListenResult {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SurveyListenResult{" +
                "id='" + id + '\'' +
                '}';
    }
}
