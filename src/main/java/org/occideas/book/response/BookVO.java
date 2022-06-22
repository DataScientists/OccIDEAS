package org.occideas.book.response;

import org.occideas.entity.Book;
import org.occideas.entity.BookModule;

import java.util.ArrayList;
import java.util.List;

public class BookVO {

    private Book book;
    private List<BookModule> modules = new ArrayList<>();

    public BookVO() {
    }

    public BookVO(Book book, List<BookModule> modules) {
        this.book = book;
        this.modules = modules;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<BookModule> getModules() {
        return modules;
    }

    public void setModules(List<BookModule> modules) {
        this.modules = modules;
    }
}
