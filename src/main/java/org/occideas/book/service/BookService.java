package org.occideas.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.book.dao.BookDao;
import org.occideas.book.dao.BookModuleDao;
import org.occideas.book.mapper.BookMapper;
import org.occideas.book.request.BookRequest;
import org.occideas.book.response.BookVO;
import org.occideas.entity.Book;
import org.occideas.entity.BookModule;
import org.occideas.exceptions.BookNotExistException;
import org.occideas.node.dao.NodeDao;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.occideas.utilities.NodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BookService {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private BookModuleDao bookModuleDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private NodeUtil nodeUtil;
    @Autowired
    private NodeDao nodeDao;


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

    @Async
    public void addModuleToBook(BookRequest bookRequest, String updatedBy) throws JsonProcessingException {

        saveModuleToBook(bookRequest, updatedBy);
        final Optional<Book> bookById = bookDao.findById(bookRequest.getBookId());
        if (bookById.isEmpty()) {
            throw new BookNotExistException("Book does not exist");
        }
        final Book book = bookById.get();
        book.setLastUpdated(LocalDateTime.now());
        bookDao.save(book);
    }

//    @Async
//    public void addModuleToBookByNode(long idNode, String updatedBy) throws JsonProcessingException {
//        NodeVO nodeVO = nodeUtil.convertToNodeVO(nodeDao.getNode(idNode));
//        String json = new ObjectMapper().writeValueAsString(nodeVO);
//
//        final BookModule bookModule = new BookModule(
//                bookRequest.getBookId(),
//                nodeVO.getName(),
//                valueAsBytes, nodeVO.hashCode(),
//                nodeVO.getClass().getName(),
//                updatedBy);
//
//        if (shouldSave) {
//            log.info("Adding Module {}", nodeVO.getName());
//            bookModuleDao.save(bookModule);
//        }
//
//        nodeVO.getChildNodes().stream()
//                .forEach(node -> {
//                    try {
//                        if (node.getLink() != 0l) {
//                            saveModuleToBook(new BookRequest(node.getLink(), bookRequest.getBookId()), updatedBy, true);
//                        } else {
//                            saveModuleToBook(new BookRequest(node.getIdNode(), bookRequest.getBookId()), updatedBy, false);
//                        }
//                    } catch (JsonProcessingException e) {
//                        log.error(e.getMessage(), e);
//                    }
//                });
//    }

    private void saveModuleToBook(BookRequest bookRequest, String updatedBy) throws JsonProcessingException {
        final BookModule bookModule = new BookModule(
                bookRequest.getBookId(),
                bookRequest.getName(),
                bookRequest.getJson(),
                bookRequest.getType(),
                updatedBy,
                LocalDateTime.now());

        bookModuleDao.save(bookModule);
    }

    public void deleteBookModuleInBook(long bookId, String name) {
        bookModuleDao.deleteByBookIdAndName(bookId, name);
    }


    public void delete(BookVO bookVO) {
        bookDao.delete(bookMapper.toBook(bookVO));
    }
}
