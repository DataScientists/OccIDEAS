package org.occideas.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "COMMIT_VERSION")
public class CommitVersion implements Serializable {

    @Id
    private String id;
    private String author;
    private LocalDateTime lastUpdated;
    private String parent;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "commitId", referencedColumnName = "id")
    private List<CommitFile> commitFiles;

    public CommitVersion() {
    }

    public CommitVersion(String id, String author, LocalDateTime lastUpdated, String parent) {
        this.id = id;
        this.author = author;
        this.lastUpdated = lastUpdated;
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<CommitFile> getCommitFiles() {
        return commitFiles;
    }

    public void setCommitFiles(List<CommitFile> commitFiles) {
        this.commitFiles = commitFiles;
    }
}
