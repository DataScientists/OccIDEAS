package org.occideas.book.response;

import java.time.LocalDateTime;
import java.util.List;

public class BookVO {

    private long id;
    private String name;
    private String description;
    private LocalDateTime lastUpdated;
    private String createdBy;
    private List<BookModuleVO> modules;

    public BookVO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<BookModuleVO> getModules() {
        return modules;
    }

    public void setModules(List<BookModuleVO> modules) {
        this.modules = modules;
    }
}
