package org.occideas.versioning.dao;

import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.UserFolder;
import org.occideas.entity.UserFolder_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserFolderDao extends GenericBaseDao<UserFolder, String> {

    public UserFolderDao() {
        super(UserFolder.class, UserFolder_.USER_ID);
    }
}
