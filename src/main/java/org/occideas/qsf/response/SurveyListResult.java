package org.occideas.qsf.response;

import java.util.List;

public class SurveyListResult extends Result {

    private List<Element> elements;
    private String nextPage;

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}
