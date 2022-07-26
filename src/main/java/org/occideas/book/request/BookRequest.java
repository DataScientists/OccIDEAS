package org.occideas.book.request;

public class BookRequest {

    private long bookId;
    private String name;
    private String type;
    private String json;
    private String updatedBy;

    public BookRequest() {
    }

    public BookRequest(long bookId, String name, String type, String json, String updatedBy) {
        this.bookId = bookId;
        this.name = name;
        this.type = type;
        this.json = json;
        this.updatedBy = updatedBy;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
