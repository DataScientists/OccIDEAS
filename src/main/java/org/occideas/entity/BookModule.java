package org.occideas.entity;

import javax.persistence.*;
import java.io.Serializable;

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
    private byte[] json;
    @Column(name = "hashCode")
    private Integer hashCode;
    private String type;
    private String author;
    private long idNode;

    public BookModule() {
    }

    public BookModule(long id, long bookId, String fileName, byte[] json, Integer hashCode, String type, String author, long idNode) {
        this.id = id;
        this.bookId = bookId;
        this.fileName = fileName;
        this.json = json;
        this.hashCode = hashCode;
        this.type = type;
        this.author = author;
        this.idNode = idNode;
    }

    public BookModule(long bookId, String fileName, byte[] json, Integer hashCode, String type, String author, long idNode) {
        this.bookId = bookId;
        this.fileName = fileName;
        this.json = json;
        this.hashCode = hashCode;
        this.type = type;
        this.author = author;
        this.idNode = idNode;
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

    public byte[] getObject() {
        return json;
    }

    public void setObject(byte[] json) {
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

    public byte[] getJson() {
        return json;
    }

    public void setJson(byte[] json) {
        this.json = json;
    }

    public long getIdNode() {
        return idNode;
    }

    public void setIdNode(long idNode) {
        this.idNode = idNode;
    }
}
