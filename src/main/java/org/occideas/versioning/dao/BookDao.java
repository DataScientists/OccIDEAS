package org.occideas.versioning.dao;

import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.Book;
import org.occideas.entity.Book_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BookDao extends GenericBaseDao<Book, Long> {

    public BookDao() {
        super(Book.class, Book_.ID);
    }

}
