package org.occideas.security.service;

import org.occideas.security.model.User;

public interface UserService {

	void save(User user);
    
    User findById(int id);
     
    User findBySso(String sso);
	
}
