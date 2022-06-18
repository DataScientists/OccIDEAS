package org.occideas.book.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.BookModule;
import org.occideas.entity.BookModule_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Blob;
import java.util.Optional;

@Repository
@Transactional
public class BookModuleDao extends GenericBaseDao<BookModule, Long> {

    public BookModuleDao() {
        super(BookModule.class, BookModule_.ID);
    }

    public Optional<BookModule> findByFileNameAndBookId(String filename, long bookId) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BookModule> criteria = builder.createQuery(BookModule.class);
        Root<BookModule> root = criteria.from(BookModule.class);
        criteria.select(root);
        criteria.where(builder.and(
                        builder.equal(root.get(BookModule_.FILE_NAME), filename),
                        builder.equal(root.get(BookModule_.BOOK_ID), bookId)
                )
        );
        return sessionFactory.getCurrentSession().createQuery(criteria).uniqueResultOptional();
    }

    public Blob createBlob(byte[] serializeObject) {
        return Hibernate.getLobCreator(sessionFactory.getCurrentSession()).createBlob(
                serializeObject
        );
    }
}
