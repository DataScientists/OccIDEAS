package org.occideas.entity;

import java.io.Serializable;
import java.util.Objects;

public class BookModuleId implements Serializable {

    protected long bookId;
    protected String name;

    public BookModuleId() {
    }

    public BookModuleId(long bookId, String name) {
        this.bookId = bookId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookModuleId that = (BookModuleId) o;
        return bookId == that.bookId && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, name);
    }
}
