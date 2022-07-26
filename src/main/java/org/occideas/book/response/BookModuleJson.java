package org.occideas.book.response;

import java.time.LocalDateTime;

public class BookModuleJson {

    private String name;
    private String json;
    private LocalDateTime lastUpdatedDate;

    public BookModuleJson(String name, String json, LocalDateTime lastUpdatedDate) {
        this.name = name;
        this.json = json;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
