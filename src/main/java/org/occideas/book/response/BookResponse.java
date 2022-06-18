package org.occideas.book.response;

import org.occideas.entity.Book;

import java.util.List;

public class BookResponse {

    private List<Book> books;

    public BookResponse() {
    }

    public BookResponse(List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
