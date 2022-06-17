package org.occideas.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;

@Entity
@Table(name = "BOOK_MODULE")
public class BookModule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "bookId")
    private long bookId;
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "json", columnDefinition = "BLOB")
    @Lob
    private Blob json;
    @Column(name = "hashCode")
    private Integer hashCode;
    private String type;
    private String author;

    public BookModule() {
    }

    public BookModule(long id, long bookId, String fileName, Blob json, Integer hashCode, String type, String author) {
        this.id = id;
        this.bookId = bookId;
        this.fileName = fileName;
        this.json = json;
        this.hashCode = hashCode;
        this.type = type;
        this.author = author;
    }

    public BookModule(long bookId, String fileName, Blob json, Integer hashCode, String type, String author) {
        this.bookId = bookId;
        this.fileName = fileName;
        this.json = json;
        this.hashCode = hashCode;
        this.type = type;
        this.author = author;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Blob getObject() {
        return json;
    }

    public void setObject(Blob json) {
        this.json = json;
    }

    public Integer getHashCode() {
        return hashCode;
    }

    public void setHashCode(Integer hashCode) {
        this.hashCode = hashCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
