package org.occideas.versioning.dao;

import org.hibernate.Session;
import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.CommitVersion;
import org.occideas.entity.CommitVersion_;
import org.occideas.exceptions.GenericException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
@Transactional
public class CommitVersionDao extends GenericBaseDao<CommitVersion, String> {

    public CommitVersionDao() {
        super(CommitVersion.class, CommitVersion_.ID);
    }

    public Optional<CommitVersion> findMaster() {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<CommitVersion> criteria = builder.createQuery(CommitVersion.class);
        Root<CommitVersion> root = criteria.from(CommitVersion.class);
        criteria.select(root);
        criteria.where(builder.and(
                        builder.isNull(root.get(CommitVersion_.PARENT))
                )
        );
        Optional<CommitVersion> masterVersion = sessionFactory.getCurrentSession().createQuery(criteria).uniqueResultOptional();
        if (masterVersion.isEmpty()) {
            throw new GenericException("Master folder is empty.");
        }
        return masterVersion;
    }
}
