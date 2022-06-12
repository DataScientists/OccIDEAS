package org.occideas.versioning.dao;

import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.CommitFile;
import org.occideas.entity.JobModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommitFileDaoTest {

    @Autowired
    CommitFileDao commitFileDao;
    @Autowired
    private SessionFactory sessionFactory;

    @AfterEach
    public void cleanup() {
        commitFileDao.deleteAll();
    }

    @Test
    void givenObject_whenConvertToHashSerializable_shouldSave() throws SQLException, IOException, ClassNotFoundException {
        JobModule jobModule = new JobModule();
        jobModule.setName("test name");
        jobModule.setIdNode(1l);
        jobModule.setDescription("test description");
        final byte[] serializeObject = getSerialize(jobModule);
        commitFileDao.save(new CommitFile(1l, "12asd", "",
                commitFileDao.createBlob(serializeObject), jobModule.hashCode(), jobModule.getClass().getName()));

        final Optional<CommitFile> fileById = commitFileDao.findById(1l);

        assertFalse(fileById.isEmpty());
        assertEquals(jobModule.hashCode(), fileById.get().getHashCode());
    }

    private byte[] getSerialize(JobModule jobModule) {
        return SerializationUtils.serialize(jobModule);
    }
}