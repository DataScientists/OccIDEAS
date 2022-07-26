package org.occideas.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOK_MODULE")
@IdClass(BookModuleId.class)
public class BookModule implements Serializable {

    @Id
    @Column(name = "bookId")
    private long bookId;
    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "json")
    @Lob
    private String json;
    private String type;
    private String author;
    private LocalDateTime lastUpdated;

    public BookModule() {
    }

    public BookModule(long bookId, String name, String json, String type, String author, LocalDateTime lastUpdated) {
        this.bookId = bookId;
        this.name = name;
        this.json = json;
        this.type = type;
        this.author = author;
        this.lastUpdated = lastUpdated;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
