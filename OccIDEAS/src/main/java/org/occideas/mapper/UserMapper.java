package org.occideas.mapper;

import java.util.List;
import java.util.Set;

import org.occideas.security.model.User;
import org.occideas.security.model.UserProfile;
import org.occideas.vo.UserProfileVO;
import org.occideas.vo.UserVO;

public interface UserMapper {

	List<UserVO> convertToUserVOList(List<User> entityList);
	
	UserVO convertToUserVO(User entity);
	
	Set<UserProfileVO> convertToUserProfileVO(Set<UserProfile> entityList);
	
	UserProfileVO convertToUserProfileVO(UserProfile entity);
}
