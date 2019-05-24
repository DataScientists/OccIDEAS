package org.occideas.security.dao;

import org.occideas.security.model.UserUserProfile;

public interface UserUserProfileDao {

  void save(UserUserProfile profile);

  void delete(int userId);

}
