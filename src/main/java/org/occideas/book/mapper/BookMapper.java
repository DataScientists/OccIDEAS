package org.occideas.book.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.occideas.book.response.BookModuleVO;
import org.occideas.book.response.BookVO;
import org.occideas.entity.Book;
import org.occideas.entity.BookModule;
import org.occideas.vo.ModuleVO;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookMapper {

    public Book toBook(BookVO vo) {
        Book book = new Book();
        book.setId(vo.getId());
        book.setCreatedBy(vo.getCreatedBy());
        book.setLastUpdated(vo.getLastUpdated());
        book.setDescription(vo.getDescription());
        book.setName(vo.getName());
        return book;
    }

    public BookModuleVO toBookModuleVO(BookModule bookModule) {
        BookModuleVO vo = new BookModuleVO();
        vo.setBookId(bookModule.getBookId());
        vo.setId(bookModule.getId());
        vo.setAuthor(bookModule.getAuthor());
        try {
            ModuleVO jobModule = new ObjectMapper().readValue(bookModule.getObject(), ModuleVO.class);
            vo.setJobModule(jobModule);
        } catch (IOException e) {
            e.printStackTrace();
        }
        vo.setFileName(bookModule.getFileName());
        vo.setHashCode(bookModule.getHashCode());
        return vo;
    }

    public List<BookModuleVO> toBookModuleVOList(List<BookModule> bookModules) {
        List<BookModuleVO> bookModuleVOs = new ArrayList<>();
        bookModules.stream().forEach(entity -> bookModuleVOs.add(toBookModuleVO(entity)));
        return bookModuleVOs;
    }

    public BookVO toBookVO(Book entity) {
        BookVO vo = new BookVO();
        vo.setCreatedBy(entity.getCreatedBy());
        vo.setDescription(entity.getDescription());
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setLastUpdated(entity.getLastUpdated());
        vo.setModules(toBookModuleVOList(entity.getModules()));
        return vo;
    }

    public List<Book> toBookList(List<BookVO> voList) {
        List<Book> books = new ArrayList<>();
        voList.stream().forEach(vo -> books.add(toBook(vo)));
        return books;
    }

    public List<BookVO> toBookVOList(List<Book> voList) {
        List<BookVO> books = new ArrayList<>();
        voList.stream().forEach(entity -> books.add(toBookVO(entity)));
        return books;
    }

}
