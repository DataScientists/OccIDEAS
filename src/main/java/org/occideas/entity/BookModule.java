package org.occideas.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "BOOK_MODULE")
@IdClass(BookModuleId.class)
public class BookModule implements Serializable {

    @Id
    @Column(name = "bookId")
    private long bookId;
    @Id
    @Column(name = "idNode")
    private long idNode;
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "json", columnDefinition = "BLOB")
    @Lob
    private byte[] json;
    @Column(name = "hashCode")
    private Integer hashCode;
    private String type;
    private String author;


    public BookModule() {
    }

    public BookModule(long bookId, long idNode, String fileName, byte[] json, Integer hashCode, String type, String author) {
        this.bookId = bookId;
        this.idNode = idNode;
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
