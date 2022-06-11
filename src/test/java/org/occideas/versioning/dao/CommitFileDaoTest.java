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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
                commitFileDao.createBlob(serializeObject)));

        final Optional<CommitFile> fileById = commitFileDao.findById(1l);

        assertFalse(fileById.isEmpty());
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileById.get().getHash()
                .getBytes(1, (int) fileById.get().getHash().length()));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        JobModule actual = (JobModule) objectInputStream.readObject();
        assertEquals(serializeObject, getSerialize(actual));
    }

    private byte[] getSerialize(JobModule jobModule) {
        return SerializationUtils.serialize(jobModule);
    }
}