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
import org.occideas.entity.Fragment;
import org.occideas.entity.JobModule;
import org.occideas.exceptions.BookNotExistException;
import org.occideas.fragment.dao.FragmentDao;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.module.dao.ModuleDao;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    private ModuleDao moduleDao;
    @Autowired
    private FragmentDao fragmentDao;
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private FragmentMapper fragmentMapper;
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

    @Async
    public void addModuleToBook(BookRequest bookRequest, String updatedBy) throws JsonProcessingException {
        saveModuleToBook(bookRequest, updatedBy);
    }

    private void saveModuleToBook(BookRequest bookRequest, String updatedBy) throws JsonProcessingException {
        NodeVO nodeVO = getNodeVO(bookRequest);


        final byte[] valueAsBytes = new ObjectMapper().writeValueAsBytes(nodeVO);

        final BookModule bookModule = new BookModule(
                bookRequest.getBookId(),
                bookRequest.getIdNode(),
                nodeVO.getName(),
                valueAsBytes, nodeVO.hashCode(),
                nodeVO.getClass().getName(),
                updatedBy);

        bookModuleDao.save(bookModule);

        nodeVO.getChildNodes().stream()
                .filter(node -> node.getLink() != 0l)
                .forEach(node -> {
                    try {
                        saveModuleToBook(new BookRequest(node.getLink(), bookRequest.getBookId()), updatedBy);
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage(), e);
                    }
                });
    }

    private NodeVO getNodeVO(BookRequest bookRequest) {
        final JobModule module = moduleDao.get(bookRequest.getIdNode());
        if (Objects.nonNull(module)) {
            ModuleVO modVO = moduleMapper.convertToModuleVO(module, true);
            return modVO;
        } else {
            final Fragment fragment = fragmentDao.get(bookRequest.getIdNode());
            FragmentVO fragmentVO = fragmentMapper.convertToFragmentVO(fragment, true);
            return fragmentVO;
        }
    }

    public void deleteBookModuleInBook(long bookId, long idNode) {
        bookModuleDao.deleteByBookIdAndIdNode(bookId, idNode);
    }


    public void delete(BookVO bookVO) {
        bookDao.delete(bookMapper.toBook(bookVO));
    }
}
