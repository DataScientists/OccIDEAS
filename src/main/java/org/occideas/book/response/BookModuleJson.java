package org.occideas.book.response;

import java.time.LocalDateTime;

public class BookModuleJson {

    private String name;
    private String json;
    private String type;
    private LocalDateTime lastUpdatedDate;

    public BookModuleJson(String name, String json, String type, LocalDateTime lastUpdatedDate) {
        this.name = name;
        this.json = json;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
