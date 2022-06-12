package org.occideas.versioning.service;

import org.apache.commons.lang3.SerializationUtils;
import org.occideas.entity.CommitFile;
import org.occideas.entity.CommitVersion;
import org.occideas.entity.Folder;
import org.occideas.entity.UserFolder;
import org.occideas.versioning.dao.CommitFileDao;
import org.occideas.versioning.dao.CommitVersionDao;
import org.occideas.versioning.dao.FolderDao;
import org.occideas.versioning.dao.UserFolderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VersioningService {

    @Autowired
    private CommitFileDao commitFileDao;
    @Autowired
    private CommitVersionDao commitVersionDao;
    @Autowired
    private FolderDao folderDao;
    @Autowired
    private UserFolderDao userFolderDao;

    public void commitObject(String commitId, Object object) {
        Blob serializedObject = commitFileDao.createBlob(SerializationUtils.serialize((Serializable) object));
        commitFileDao.save(new CommitFile(commitId, object.getClass().getName(),
                serializedObject, object.hashCode(), object.getClass().getName()));
    }

    public String commitObjects(String userId, List<Object> objects) {
        final String commitId = UUID.randomUUID().toString();
        prepareNewVersion(userId, commitId);
        objects.forEach(obj -> commitObject(commitId, obj));
        return commitId;
    }

    public UserFolder getUserFolder(String userId) {
        Optional<UserFolder> userFolderOptional = userFolderDao.findById(userId);
        if (userFolderOptional.isEmpty()) {
            Optional<Folder> defaultFolder = folderDao.findDefault();
            return userFolderDao.save(new UserFolder(userId, defaultFolder.get().getId()));
        }
        return userFolderOptional.get();
    }

    public void prepareNewVersion(String userId, String commitId) {
        commitVersionDao.save(new CommitVersion(commitId, userId, LocalDateTime.now(), null));
        UserFolder userFolder = getUserFolder(userId);
        Optional<Folder> folderOptional = folderDao.findById(userFolder.getFolderId());
        commitVersionDao.save(new CommitVersion(folderOptional.get().getHead(), userId, LocalDateTime.now(), commitId));
    }


}
