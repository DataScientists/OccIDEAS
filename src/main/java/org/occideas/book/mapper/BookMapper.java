package org.occideas.book.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.occideas.book.response.BookModuleVO;
import org.occideas.book.response.BookVO;
import org.occideas.entity.Book;
import org.occideas.entity.BookModule;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        vo.setName(bookModule.getName());
        vo.setAuthor(bookModule.getAuthor());
        try {
            Map<?, ?> map = new ObjectMapper().readValue(Paths.get(bookModule.getJson()).toFile(), Map.class);
            vo.setJson(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        vo.setLastUpdateDate(bookModule.getLastUpdated());
        vo.setType(bookModule.getType());
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
