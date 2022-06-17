package org.occideas.versioning.controller;

import org.occideas.entity.Book;
import org.occideas.versioning.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> list() {
        return bookService.getBooks();
    }

    @PostMapping
    public ResponseEntity<Long> createBook(Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

}
