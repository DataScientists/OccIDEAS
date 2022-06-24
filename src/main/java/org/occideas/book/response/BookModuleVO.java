package org.occideas.book.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.occideas.entity.BookModule;
import org.occideas.vo.ModuleVO;

import java.io.IOException;

public class BookModuleVO {

    private long id;
    private long bookId;
    private String fileName;
    private ModuleVO jobModule;
    private Integer hashCode;
    private String type;
    private String author;

    public BookModuleVO() {
    }

    public BookModuleVO(BookModule bookModule) {
        this.id = bookModule.getId();
        this.bookId = bookModule.getBookId();
        this.fileName = bookModule.getFileName();
        try {
            this.jobModule = new ObjectMapper().readValue(bookModule.getObject(), ModuleVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.hashCode = bookModule.getHashCode();
        this.type = bookModule.getType();
        this.author = bookModule.getAuthor();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getHashCode() {
        return hashCode;
    }

    public void setHashCode(Integer hashCode) {
        this.hashCode = hashCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ModuleVO getJobModule() {
        return jobModule;
    }

    public void setJobModule(ModuleVO jobModule) {
        this.jobModule = jobModule;
    }
}
