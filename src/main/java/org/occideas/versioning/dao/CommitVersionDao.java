package org.occideas.versioning.dao;

import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.CommitVersion;
import org.occideas.entity.CommitVersion_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CommitVersionDao extends GenericBaseDao<CommitVersion, String> {

    public CommitVersionDao() {
        super(CommitVersion.class, CommitVersion_.ID);
    }
}
