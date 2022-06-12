package org.occideas.versioning.service;

import com.mysql.cj.jdbc.Blob;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.entity.*;
import org.occideas.versioning.dao.CommitFileDao;
import org.occideas.versioning.dao.CommitVersionDao;
import org.occideas.versioning.dao.FolderDao;
import org.occideas.versioning.dao.UserFolderDao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VersioningServiceTest {

    @Mock
    CommitFileDao commitFileDao;
    @Mock
    CommitVersionDao commitVersionDao;
    @Mock
    FolderDao folderDao;
    @Mock
    UserFolderDao userFolderDao;
    @InjectMocks
    VersioningService versioningService;

    @Test
    void givenUserIdAndObjects_whenCommitObjects_thenReturnCommitId() {
        when(commitFileDao.save(any())).thenReturn(new CommitFile());
        when(commitFileDao.createBlob(any())).thenReturn(new Blob(new byte[]{}, null));
        when(commitVersionDao.save(any(CommitVersion.class))).thenReturn(new CommitVersion());
        String userId = "u12345";
        when(userFolderDao.findById(anyString())).thenReturn(Optional.of(new UserFolder(userId, 1)));
        String master = UUID.randomUUID().toString();
        when(folderDao.findById(1l)).thenReturn(Optional.of(new Folder("master", master, 1, true, LocalDateTime.now())));
        List<Object> listOfObjects = new ArrayList<>();
        listOfObjects.add(new JobModule());
        listOfObjects.add(new Fragment());

        String commitId = versioningService.commitObjects(userId, listOfObjects);

        assertNotNull(commitId);
        verify(commitVersionDao, times(2)).save(any(CommitVersion.class));
        verify(userFolderDao, times(0)).save(any(UserFolder.class));
        verify(userFolderDao, times(1)).findById(anyString());
        verify(commitFileDao, times(listOfObjects.size())).save(any(CommitFile.class));
        verify(commitFileDao, times(listOfObjects.size())).createBlob(any());
        verify(folderDao, times(1)).findById(any());
    }

    @Test
    void givenUserFolderDoesNotExist_whenGetUserFolder_thenCreateDefaultUserFolder() {
        final String userId = "u12345";
        when(userFolderDao.findById(anyString())).thenReturn(Optional.empty());
        UserFolder expected = new UserFolder(userId, 12345);
        when(userFolderDao.save(any(UserFolder.class))).thenReturn(expected);
        when(folderDao.findDefault()).thenReturn(Optional.of(new Folder()));

        UserFolder actual = versioningService.getUserFolder(userId);

        assertNotNull(actual);
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getFolderId(), actual.getFolderId());

    }

}