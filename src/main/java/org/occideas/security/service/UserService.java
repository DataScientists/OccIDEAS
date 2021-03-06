package org.occideas.security.service;

import org.occideas.exceptions.InvalidCurrentPasswordException;
import org.occideas.security.model.User;
import org.occideas.vo.PasswordVO;
import org.occideas.vo.UserProfileVO;
import org.occideas.vo.UserUserProfileVO;
import org.occideas.vo.UserVO;

import java.util.List;

public interface UserService {

  UserVO save(UserVO userVO);

  UserVO update(UserVO userVO);

  void changePassword(PasswordVO vo) throws InvalidCurrentPasswordException;

  User findById(int id);

  User findBySso(String sso);

  List<UserVO> getUserRoles();

  List<UserProfileVO> getRoles();

  void saveUserUserProfile(UserUserProfileVO vo);

  void saveUserUserProfileList(List<UserUserProfileVO> vo);

  void deleteUserUserProfile(int id);

}
