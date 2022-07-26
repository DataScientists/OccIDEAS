package org.occideas.book.request;

public class AddNodeRequest {

    private long idNode;
    private long bookId;

    public long getIdNode() {
        return idNode;
    }

    public void setIdNode(long idNode) {
        this.idNode = idNode;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
}
