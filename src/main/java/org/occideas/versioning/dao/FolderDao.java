package org.occideas.versioning.dao;

import org.hibernate.Session;
import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.Folder;
import org.occideas.entity.Folder_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class FolderDao extends GenericBaseDao<Folder, Long> {

    public FolderDao() {
        super(Folder.class, Folder_.ID);
    }

    public List<Folder> findByHead(String head) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Folder> criteria = builder.createQuery(Folder.class);
        Root<Folder> root = criteria.from(Folder.class);
        criteria.select(root);
        criteria.where(builder.and(
                        builder.equal(root.get(Folder_.HEAD), head)
                )
        );
        return sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
    }

    public Optional<Folder> findDefault() {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Folder> criteria = builder.createQuery(Folder.class);
        Root<Folder> root = criteria.from(Folder.class);
        criteria.select(root);
        criteria.where(builder.and(
                        builder.isTrue(root.get(Folder_.IS_DEFAULT))
                )
        );
        return sessionFactory.getCurrentSession().createQuery(criteria).uniqueResultOptional();
    }
}
