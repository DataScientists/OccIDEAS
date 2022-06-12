package org.occideas.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;

@Entity
@Table(name = "COMMIT_FILE")
public class CommitFile implements Serializable {

    @Id
    private long id;
    @Column(name = "commitId")
    private String commitId;
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "object", columnDefinition = "BLOB")
    @Lob
    private Blob object;
    @Column(name = "hashCode")
    private Integer hashCode;
    private String type;

    public CommitFile() {
    }

    public CommitFile(long id, String commitId, String fileName, Blob object, Integer hashCode, String type) {
        this.id = id;
        this.commitId = commitId;
        this.fileName = fileName;
        this.object = object;
        this.hashCode = hashCode;
        this.type = type;
    }

    public CommitFile(String commitId, String fileName, Blob object, Integer hashCode, String type) {
        this.commitId = commitId;
        this.fileName = fileName;
        this.object = object;
        this.hashCode = hashCode;
        this.type = type;
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

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public Blob getObject() {
        return object;
    }

    public void setObject(Blob object) {
        this.object = object;
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
