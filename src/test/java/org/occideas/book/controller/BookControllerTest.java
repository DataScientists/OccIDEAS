package org.occideas.book.controller;

import org.junit.jupiter.api.Test;
import org.occideas.book.response.BookResponse;
import org.occideas.config.TestSecurityConfig;
import org.occideas.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestSecurityConfig.class)
class BookControllerTest {

    @LocalServerPort
    private int port;
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

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

}