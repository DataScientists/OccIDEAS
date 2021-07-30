package org.occideas.security.service;

import org.occideas.security.dao.UserProfileDao;
import org.occideas.security.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserProfileServiceImpl implements UserProfileService {

  @Autowired
  private UserProfileDao dao;

  public List<UserProfile> findAll() {
    return dao.findAll();
  }

  public UserProfile findByType(String type) {
    return dao.findByType(type);
  }

  public UserProfile findById(int id) {
    return dao.findById(id);
  }
}
