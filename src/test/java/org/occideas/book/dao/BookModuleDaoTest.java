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

import java.time.LocalDateTime;
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
        bookModuleDao.save(new BookModule(1l,
                "Sample Name",
                "/sampleBook/sample.json",
                "Task Module",
                "u12345",
                LocalDateTime.now()));

        final Optional<BookModule> byFileNameAndBookId = bookModuleDao.findByNameAndBookId("Sample Name", 1l);

        assertTrue(byFileNameAndBookId.isPresent());

    }

    @Test
    void givenExistModule_whenDelete_shouldDeleteBookModule() throws JsonProcessingException {
        bookModuleDao.save(new BookModule(1l,
                "Sample Name",
                "/sampleBook/sample.json",
                "Task Module",
                "u12345",
                LocalDateTime.now()));
        final Optional<BookModule> saved = bookModuleDao.findByNameAndBookId("Sample Name", 1l);
        assertTrue(saved.isPresent());

        bookModuleDao.deleteByBookIdAndName(1l, "Sample Name");

        final Optional<BookModule> deleted = bookModuleDao.findByNameAndBookId("Sample Name", 1l);
        assertTrue(deleted.isEmpty());
    }

    private byte[] getJson(JobModule jobModule) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(jobModule);
    }
}