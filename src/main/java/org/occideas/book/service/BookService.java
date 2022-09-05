package org.occideas.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.book.dao.BookDao;
import org.occideas.book.dao.BookModuleDao;
import org.occideas.book.mapper.BookMapper;
import org.occideas.book.request.BookRequest;
import org.occideas.book.response.BookModuleJson;
import org.occideas.book.response.BookVO;
import org.occideas.config.BookConfig;
import org.occideas.entity.Book;
import org.occideas.entity.BookModule;
import org.occideas.exceptions.BookNotExistException;
import org.occideas.exceptions.GenericException;
import org.occideas.node.dao.NodeDao;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.occideas.utilities.FileUtil;
import org.occideas.utilities.NodeUtil;
import org.occideas.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BookService {

    private static Logger log = LogManager.getLogger(BookService.class);

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
    @Autowired
    private BookConfig bookConfig;


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
    public void addModuleToBook(BookRequest bookRequest) throws JsonProcessingException {
        final Optional<Book> bookById = bookDao.findById(bookRequest.getBookId());
        if (bookById.isEmpty()) {
            throw new BookNotExistException("Book does not exist");
        }
        final Book book = bookById.get();
        String directory = bookConfig.getPath() + FileSystems.getDefault().getSeparator() + book.getName();
        boolean success = FileUtil.saveJsonToFile(bookRequest.getJson(), directory, bookRequest.getName());
        if (success) {
            String fullPath = directory + FileSystems.getDefault().getSeparator() + bookRequest.getName() + ".json";
            saveModuleToBook(bookRequest, fullPath);
            book.setLastUpdated(LocalDateTime.now());
            bookDao.save(book);
        } else {
            throw new GenericException("Unable to save to book. Error occured, check the logs.");
        }
    }

    @Async
    public void addModuleToBookByNode(long idNode, long bookId, String updatedBy, List<String> uniqueNames, boolean shouldSave) throws JsonProcessingException {
        NodeVO nodeVO = nodeUtil.convertToNodeVO(nodeDao.getNode(idNode));
        if (shouldSave && !uniqueNames.contains(sanitizeFileName(nodeVO.getName()))) {
            String json = new ObjectMapper().writeValueAsString(nodeVO);
            addModuleToBook(new BookRequest(bookId, sanitizeFileName(nodeVO.getName()), nodeVO.getType(), json, updatedBy));
            uniqueNames.add(sanitizeFileName(nodeVO.getName()));
        }

        for (NodeVO node : nodeVO.getChildNodes()) {
            try {
                if (node.getLink() != 0l) {
                    addModuleToBookByNode(node.getLink(), bookId, updatedBy, uniqueNames, true);
                } else {
                    addModuleToBookByNode(node.getIdNode(), bookId, updatedBy, uniqueNames, false);
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private String sanitizeFileName(String fileName) {
        String newFileName = fileName.trim().replace(" ", "").replace("/", "");
        return newFileName;
    }

    private void saveModuleToBook(BookRequest bookRequest, String jsonPath) throws JsonProcessingException {
        final BookModule bookModule = new BookModule(
                bookRequest.getBookId(),
                bookRequest.getName(),
                jsonPath,
                bookRequest.getType(),
                bookRequest.getUpdatedBy(),
                LocalDateTime.now());

        bookModuleDao.save(bookModule);
    }

    public void deleteBookModuleInBook(long bookId, String name) throws IOException {
        Optional<BookModule> byNameAndBookId = bookModuleDao.findByNameAndBookId(name, bookId);
        if (byNameAndBookId.isEmpty()) {
            throw new GenericException("book module is no longer exist." + name);
        }
        log.info("Deleting module {}", byNameAndBookId.get().getJson());
        Files.delete(Path.of(byNameAndBookId.get().getJson()));
        bookModuleDao.deleteByBookIdAndName(bookId, name);
    }


    public void delete(BookVO bookVO) {
        bookDao.delete(bookMapper.toBook(bookVO));
    }

    public BookModuleJson getModuleInBook(long bookId, String name) {
        Optional<BookModule> byNameAndBookId = bookModuleDao.findByNameAndBookId(name, bookId);
        if (byNameAndBookId.isEmpty()) {
            throw new GenericException("book module is no longer exist." + name);
        }
        return new BookModuleJson(byNameAndBookId.get().getName(),
                FileUtil.readJsonFile(byNameAndBookId.get().getJson()),
                byNameAndBookId.get().getType(),
                byNameAndBookId.get().getLastUpdated());
    }
}
