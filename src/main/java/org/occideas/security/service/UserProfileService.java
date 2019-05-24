package org.occideas.security.service;

import org.occideas.security.model.UserProfile;

import java.util.List;

public interface UserProfileService {

  List<UserProfile> findAll();

  UserProfile findByType(String type);

  UserProfile findById(int id);

}
