package org.occideas.qsf.response;

import java.util.List;

public class DistributionListResult {

    private List<DistributionListElement> elements;
    private String nextPage;

    public List<DistributionListElement> getElements() {
        return elements;
    }

    public void setElements(List<DistributionListElement> elements) {
        this.elements = elements;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    @Override
    public String toString() {
        return "DistributionListResult{" +
                "elements=" + elements +
                ", nextPage='" + nextPage + '\'' +
                '}';
    }
}
