package org.occideas.security.service;

import java.util.List;

import org.occideas.security.model.UserProfile;

public interface UserProfileService {

	List<UserProfile> findAll();
    
    UserProfile findByType(String type);
     
    UserProfile findById(int id);
	
}
