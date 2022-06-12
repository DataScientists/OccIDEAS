package org.occideas.versioning.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class FolderDaoTest {

    @Autowired
    FolderDao folderDao;

    @AfterEach
    public void cleanup() {
        folderDao.deleteAll();
    }

    @Test
    void givenFolder_whenFindFolderByHead_thenReturnFolder() {
        folderDao.save(new Folder("master", "sample", 1, false, LocalDateTime.now()));

        List<Folder> folders = folderDao.findByHead("sample");

        assertFalse(folders.isEmpty());
        assertEquals("master", folders.get(0).getName());
    }

    @Test
    void givenFolder_whenFindFolderDefault_thenReturnFolder() {
        folderDao.save(new Folder("master", "sample", 1, true, LocalDateTime.now()));

        Optional<Folder> defaultFolder = folderDao.findDefault();

        assertTrue(defaultFolder.isPresent());
        assertEquals("master", defaultFolder.get().getName());
    }
}