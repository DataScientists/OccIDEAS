package org.occideas.mapper;

import org.occideas.security.model.User;
import org.occideas.security.model.UserProfile;
import org.occideas.security.model.UserUserProfile;
import org.occideas.vo.UserProfileVO;
import org.occideas.vo.UserUserProfileVO;
import org.occideas.vo.UserVO;

import java.util.List;
import java.util.Set;

public interface UserMapper {

  List<UserVO> convertToUserVOList(List<User> entityList);

  UserVO convertToUserVO(User entity);

  Set<UserProfileVO> convertToUserProfileVO(Set<UserProfile> entityList);

  List<UserProfileVO> convertToUserProfileVOList(List<UserProfile> entityList);

  UserProfileVO convertToUserProfileVO(UserProfile entity);

  UserProfile convertToUserProfile(UserProfileVO vo);

  User convertToUser(UserVO vo);

  List<User> convertToUserList(List<UserVO> entityList);

  Set<UserProfile> convertToUserProfileSet(Set<UserProfileVO> voList);

  UserUserProfile convertToUserUserProfile(UserUserProfileVO vo);

  UserUserProfileVO convertToUserUserProfileVO(UserUserProfile entity);

  User convertToUserVOWithoutPassword(UserVO vo);
}
