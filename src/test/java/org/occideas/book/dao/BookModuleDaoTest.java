package org.occideas.book.dao;

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

import java.util.Optional;

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
    void givenObject_whenFindByFileName_shouldReturnBookModule() throws JsonProcessingException {
        JobModule sampleObject = new JobModule();
        bookModuleDao.save(new BookModule(1l,
                1l,
                sampleObject.getClass().getName(),
                getJson(sampleObject),
                sampleObject.hashCode(),
                sampleObject.getClass().getName(),
                "u12345"));

        final Optional<BookModule> byFileNameAndBookId = bookModuleDao.findByFileNameAndBookId(sampleObject.getClass().getName(), 1l);

        assertTrue(byFileNameAndBookId.isPresent());

    }

    @Test
    void givenExistModule_whenDelete_shouldDeleteBookModule() throws JsonProcessingException {
        JobModule sampleObject = new JobModule();
        bookModuleDao.save(new BookModule(1l,
                1l,
                sampleObject.getClass().getName(),
                getJson(sampleObject),
                sampleObject.hashCode(),
                sampleObject.getClass().getName(),
                "u12345"));
        final Optional<BookModule> saved = bookModuleDao.findByFileNameAndBookId(sampleObject.getClass().getName(), 1l);
        assertTrue(saved.isPresent());

        bookModuleDao.deleteByBookIdAndIdNode(1l, 1l);

        final Optional<BookModule> deleted = bookModuleDao.findByFileNameAndBookId(sampleObject.getClass().getName(), 1l);
        assertTrue(deleted.isEmpty());
    }

    private byte[] getJson(JobModule jobModule) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(jobModule);
    }
}