package org.occideas.book.controller;

import org.junit.jupiter.api.Test;
import org.occideas.book.response.BookResponse;
import org.occideas.book.response.BookVO;
import org.occideas.book.service.BookService;
import org.occideas.config.TestSecurityConfig;
import org.occideas.entity.Book;
import org.occideas.exceptions.BookNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestSecurityConfig.class)
class BookControllerTest {

    @LocalServerPort
    private int port;
    @MockBean
    private BookService bookService;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void listBooks() {
        ResponseEntity<BookResponse> response = restTemplate.getForEntity("http://localhost:" + port + "/web/rest/book", BookResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void createBook() {
        HttpEntity<Book> request = new HttpEntity<>(new Book("master", LocalDateTime.now(), "jed"));

        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:" + port + "/web/rest/book", request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void deleteBook() {
        when(bookService.findBookById(anyLong())).thenReturn(new BookVO());
        doNothing().when(bookService).delete(any(Book.class));
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/web/rest/book/1", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteBookNotExist() {
        doThrow(BookNotExistException.class).when(bookService).findBookById(anyLong());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/web/rest/book/{id}", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class, 1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}