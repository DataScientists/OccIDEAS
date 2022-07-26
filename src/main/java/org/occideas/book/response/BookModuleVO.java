package org.occideas.book.response;

import java.time.LocalDateTime;
import java.util.Map;

public class BookModuleVO {

    private long bookId;
    private String name;
    private Map<?, ?> json;
    private String type;
    private String author;
    private LocalDateTime lastUpdateDate;

    public BookModuleVO() {
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<?, ?> getJson() {
        return json;
    }

    public void setJson(Map<?, ?> json) {
        this.json = json;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
