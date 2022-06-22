package org.occideas.book.response;

import java.util.List;

public class BookResponse {

    private List<BookVO> books;

    public BookResponse() {
    }

    public BookResponse(List<BookVO> books) {
        this.books = books;
    }

    public List<BookVO> getBooks() {
        return books;
    }

    public void setBooks(List<BookVO> books) {
        this.books = books;
    }
}
