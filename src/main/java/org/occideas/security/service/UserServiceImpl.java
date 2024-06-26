package org.occideas.security.service;

import org.occideas.exceptions.InvalidCurrentPasswordException;
import org.occideas.mapper.UserMapper;
import org.occideas.security.dao.UserDao;
import org.occideas.security.dao.UserProfileDao;
import org.occideas.security.dao.UserUserProfileDao;
import org.occideas.security.model.User;
import org.occideas.security.model.UserProfile;
import org.occideas.vo.PasswordVO;
import org.occideas.vo.UserProfileVO;
import org.occideas.vo.UserUserProfileVO;
import org.occideas.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDao dao;
  @Autowired
  private UserProfileDao profileDao;
  @Autowired
  private UserUserProfileDao userUserProfileDao;
  @Autowired
  private UserMapper mapper;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public UserVO save(UserVO vo) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    vo.setPassword(passwordEncoder.encode(vo.getPassword()));
    return mapper.convertToUserVO(dao.save(mapper.convertToUser(vo)));
  }

  public UserVO update(UserVO vo) {
    return mapper.convertToUserVO(dao.save(mapper.convertToUser(vo)));
  }

  public User findById(int id) {
    return dao.findById(id);
  }

  public User findBySso(String sso) {
    return dao.findBySSO(sso);
  }

  @Override
  public List<UserVO> getUserRoles() {
    return mapper.convertToUserVOList(dao.findAll());
  }

  @Override
  public List<UserProfileVO> getRoles() {
    List<UserProfile> userProfiles = profileDao.findAll();
    return mapper.convertToUserProfileVOList(userProfiles);
  }

  @Override
  public void saveUserUserProfile(UserUserProfileVO vo) {
    userUserProfileDao.save(mapper.convertToUserUserProfile(vo));
  }

  @Override
  public void deleteUserUserProfile(int id) {
    userUserProfileDao.delete(id);
  }

  @Override
  public void saveUserUserProfileList(List<UserUserProfileVO> list) {
    for (UserUserProfileVO vo : list) {
      userUserProfileDao.save(mapper.convertToUserUserProfile(vo));
    }
  }

  @Override
  public void changePassword(PasswordVO vo) throws InvalidCurrentPasswordException {
    //check if current password is valid
    User user = checkCurrentPasswordIsValid(vo.getUserId(), vo.getCurrentPassword());
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    user.setPassword(passwordEncoder.encode(vo.getNewPassword()));
    dao.save(user);
  }

  private User checkCurrentPasswordIsValid(String userId, String currentPassword) throws InvalidCurrentPasswordException {
    User user = dao.findBySSO(userId);
    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      throw new InvalidCurrentPasswordException("Current Password is Incorrect for user id " + userId);
    }
    return user;
  }

}
