package org.occideas.versioning.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.BookModule;
import org.occideas.entity.JobModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BookModuleDaoTest {

    @Autowired
    BookModuleDao bookModuleDao;
    @Autowired
    private SessionFactory sessionFactory;

    @AfterEach
    public void cleanup() {
        bookModuleDao.deleteAll();
    }

    @Test
    void givenObject_whenConvertToJson_shouldSave() throws IOException {
        JobModule jobModule = new JobModule();
        jobModule.setName("test name");
        jobModule.setIdNode(1l);
        jobModule.setDescription("test description");
        final byte[] json = getJson(jobModule);
        final BookModule bookModule = bookModuleDao.save(new BookModule(1l, jobModule.getClass().getName(),
                bookModuleDao.createBlob(json), jobModule.hashCode(),
                jobModule.getClass().getName(),
                "u12345"));

        final Optional<BookModule> fileById = bookModuleDao.findById(bookModule.getId());

        assertTrue(fileById.isPresent());
        assertEquals(jobModule.hashCode(), fileById.get().getHashCode());
    }

    @Test
    void givenObject_whenFindByFileName_shouldReturnBookModule() throws JsonProcessingException {
        JobModule sampleObject = new JobModule();
        bookModuleDao.save(new BookModule(1l, sampleObject.getClass().getName(),
                bookModuleDao.createBlob(getJson(sampleObject)),
                sampleObject.hashCode(),
                sampleObject.getClass().getName(),
                "u12345"));

        final Optional<BookModule> byFileNameAndBookId = bookModuleDao.findByFileNameAndBookId(sampleObject.getClass().getName(), 1l);

        assertTrue(byFileNameAndBookId.isPresent());
    }

    private byte[] getJson(JobModule jobModule) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(jobModule);
    }
}