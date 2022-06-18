package org.occideas.book.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class BookDaoTest {

    @Autowired
    BookDao bookDao;

    @AfterEach
    public void cleanup() {
        bookDao.deleteAll();
    }

    @Test
    void givenFolder_whenFindBookById_thenReturnBook() {
        final Book book = bookDao.save(new Book("master", LocalDateTime.now(), "u12345"));

        final Optional<Book> optionalBook = bookDao.findById(book.getId());

        assertTrue(optionalBook.isPresent());
        assertEquals("master", optionalBook.get().getName());
    }

}