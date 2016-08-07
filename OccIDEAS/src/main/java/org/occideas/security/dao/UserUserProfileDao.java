package org.occideas.security.dao;

import org.occideas.security.model.UserUserProfile;

public interface UserUserProfileDao {

	public void save(UserUserProfile profile);
	public void delete(int userId);
	
}
