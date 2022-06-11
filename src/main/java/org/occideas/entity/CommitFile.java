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
    @Column(name = "hash", columnDefinition = "BLOB")
    @Lob
    private Blob hash;

    public CommitFile() {
    }

    public CommitFile(long id, String commitId, String fileName, Blob hash) {
        this.id = id;
        this.commitId = commitId;
        this.fileName = fileName;
        this.hash = hash;
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

    public Blob getHash() {
        return hash;
    }

    public void setHash(Blob hash) {
        this.hash = hash;
    }


}
