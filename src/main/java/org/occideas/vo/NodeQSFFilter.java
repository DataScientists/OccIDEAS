package org.occideas.vo;

public class NodeQSFFilter<T> {

    private String filterId;
    private T value;

    public NodeQSFFilter(String filterId, T value) {
        this.filterId = filterId;
        this.value = value;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
