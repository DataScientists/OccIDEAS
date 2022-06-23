package org.occideas.book.response;

import org.occideas.entity.Book;

public class BookVO {

    private Book book;

    public BookVO() {
    }

    public BookVO(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
