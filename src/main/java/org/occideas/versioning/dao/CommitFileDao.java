package org.occideas.versioning.dao;

import org.hibernate.Hibernate;
import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.CommitFile;
import org.occideas.entity.CommitFile_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;

@Repository
@Transactional
public class CommitFileDao extends GenericBaseDao<CommitFile, Long> {

    public CommitFileDao() {
        super(CommitFile.class, CommitFile_.ID);
    }

    public Blob createBlob(byte[] serializeObject) {
        return Hibernate.getLobCreator(sessionFactory.getCurrentSession()).createBlob(
                serializeObject
        );
    }
}
