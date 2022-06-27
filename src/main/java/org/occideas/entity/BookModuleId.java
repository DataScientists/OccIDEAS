package org.occideas.entity;

import java.io.Serializable;
import java.util.Objects;

public class BookModuleId implements Serializable {

    protected long bookId;
    protected long idNode;

    public BookModuleId() {
    }

    public BookModuleId(long bookId, long idNode) {
        this.bookId = bookId;
        this.idNode = idNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookModuleId that = (BookModuleId) o;
        return bookId == that.bookId && idNode == that.idNode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, idNode);
    }
}
