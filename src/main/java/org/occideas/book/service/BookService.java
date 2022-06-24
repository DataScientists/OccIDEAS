package org.occideas.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.book.dao.BookDao;
import org.occideas.book.dao.BookModuleDao;
import org.occideas.book.mapper.BookMapper;
import org.occideas.book.request.BookRequest;
import org.occideas.book.response.BookVO;
import org.occideas.entity.Book;
import org.occideas.entity.BookModule;
import org.occideas.entity.JobModule;
import org.occideas.exceptions.BookNotExistException;
import org.occideas.mapper.ModuleMapper;
import org.occideas.module.dao.ModuleDao;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.occideas.vo.ModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class BookService {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private BookModuleDao bookModuleDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private ModuleDao moduleDao;
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private BookMapper bookMapper;

    public List<BookVO> getBooks() {
        final List<Book> books = bookDao.list();
        return bookMapper.toBookVOList(books);
    }

    public BookVO findBookById(Long id) {
        final Optional<Book> byId = bookDao.findById(id);
        if (byId.isEmpty()) {
            throw new BookNotExistException("Book does not exist");
        }
        final Book book = byId.get();
        return bookMapper.toBookVO(book);
    }


    public long createBook(Book book) {
        TokenManager tokenManager = new TokenManager();
        String token = ((TokenResponse) SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
        book.setCreatedBy(tokenManager.parseUsernameFromToken(token));
        book.setLastUpdated(LocalDateTime.now());
        Book newBook = bookDao.save(book);
        return newBook.getId();
    }

    public void addToBook(long bookId, Object object, String userId) {
        byte[] serializedObject = null;
        try {
            serializedObject = new ObjectMapper().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        bookModuleDao.save(new BookModule(bookId, object.getClass().getName(),
                serializedObject, object.hashCode(), object.getClass().getName(),
                userId));
    }

    public long addAllToBook(long bookId, List<Object> objects, String userId) {
        objects.forEach(obj -> addToBook(bookId, obj, userId));
        return bookId;
    }

    public List<Object> addAllToBookExcludeExisting(long bookId, List<Object> objects, String userId) {
        final List<Object> existingBookModuleInBook = getExistingBookModuleInBook(bookId, objects);
        final List<Object> filteredBookModule = objects.stream().filter(obj -> !existingBookModuleInBook.contains(obj)).collect(Collectors.toList());
        addAllToBook(bookId, filteredBookModule, userId);
        return existingBookModuleInBook;
    }

    public List<Object> getExistingBookModuleInBook(long bookId, List<Object> objects) {
        List<Object> results = new ArrayList<>();
        objects.forEach(obj -> {
            final Optional<BookModule> byFileNameAndBookId = bookModuleDao.findByFileNameAndBookId(obj.getClass().getName(), bookId);
            if (byFileNameAndBookId.isPresent()) {
                results.add(obj);
            }
        });
        return results;
    }

    public void addModuleToBook(BookRequest bookRequest) throws JsonProcessingException {
        final Optional<Book> bookOptional = bookDao.findById(bookRequest.getBookId());
        final JobModule module = moduleDao.get(bookRequest.getIdNode());
        ModuleVO modVO = moduleMapper.convertToModuleVO(module, false);
        final byte[] valueAsBytes = new ObjectMapper().writeValueAsBytes(modVO);
        TokenManager tokenManager = new TokenManager();
        String token = ((TokenResponse) SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
        final BookModule bookModule = bookModuleDao.save(
                new BookModule(bookRequest.getBookId(),
                        modVO.getName(),
                        valueAsBytes, modVO.hashCode(),
                        modVO.getClass().getName(),
                        tokenManager.parseUsernameFromToken(token)));
        bookModuleDao.save(bookModule);
    }


    public void delete(BookVO bookVO) {
        bookDao.delete(bookMapper.toBook(bookVO));
    }
}
