package org.occideas.versioning.dao;

import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.CommitFile;
import org.occideas.entity.CommitVersion;
import org.occideas.entity.JobModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommitVersionDaoTest {

    @Autowired
    CommitFileDao commitFileDao;
    @Autowired
    CommitVersionDao commitVersionDao;
    @Autowired
    private SessionFactory sessionFactory;

    @AfterEach
    public void cleanup() {
        commitFileDao.deleteAll();
        commitVersionDao.deleteAll();
    }

    @Test
    public void givenCommitId_whenGetCommitFilesByCommitId_thenReturnFiles() {
        commitVersionDao.save(new CommitVersion("12asd", "sample", LocalDateTime.now(), null));
        commitFileDao.save(new CommitFile(1l, "12asd", "testFile.txt", Hibernate.getLobCreator(sessionFactory.getCurrentSession()).createBlob(
                SerializationUtils.serialize(new JobModule())
        )));

        final Optional<CommitVersion> commitVersionById = commitVersionDao.findById("12asd");

        assertFalse(commitVersionById.isEmpty());
        assertEquals("12asd", commitVersionById.get().getId());
        assertEquals("sample", commitVersionById.get().getAuthor());
        assertEquals(1, commitVersionById.get().getCommitFiles().size());

    }

}