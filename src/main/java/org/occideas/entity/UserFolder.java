package org.occideas.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class UserFolder implements Serializable {

    @Id
    private String userId;
    private long folderId;

    public UserFolder() {
    }

    public UserFolder(String userId, long folderId) {
        this.userId = userId;
        this.folderId = folderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }
}
