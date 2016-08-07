package org.occideas.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.occideas.security.model.User;
import org.occideas.security.model.UserProfile;
import org.occideas.security.model.UserUserProfile;
import org.occideas.vo.UserProfileVO;
import org.occideas.vo.UserUserProfileVO;
import org.occideas.vo.UserVO;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

	@Override
	public List<UserVO> convertToUserVOList(List<User> entityList) {
		if (entityList == null) {
			return null;
		}
		List<UserVO> list = new ArrayList<>();
		for (User entity : entityList) {
			list.add(convertToUserVO(entity));
		}
		return list;
	}

	@Override
	public UserVO convertToUserVO(User entity) {
		if (entity == null) {
			return null;
		}

		UserVO vo = new UserVO();
		vo.setEmail(entity.getEmail());
		vo.setId(entity.getId());
		vo.setPassword(entity.getPassword());
		vo.setSsoId(entity.getSsoId());
		vo.setState(entity.getState());
		vo.setUserProfiles(convertToUserProfileVO(entity.getUserProfiles()));
		return vo;
	}

	@Override
	public Set<UserProfileVO> convertToUserProfileVO(Set<UserProfile> entityList) {
		if (entityList == null) {
			return null;
		}
		Set<UserProfileVO> set = new HashSet<>();
		for (UserProfile entity : entityList) {
			set.add(convertToUserProfileVO(entity));
		}
		return set;
	}

	@Override
	public UserProfileVO convertToUserProfileVO(UserProfile entity) {
		if (entity == null) {
			return null;
		}
		UserProfileVO vo = new UserProfileVO();
		vo.setId(entity.getId());
		vo.setType(entity.getType());
		return vo;
	}

	@Override
	public User convertToUser(UserVO vo) {
		if (vo == null) {
			return null;
		}
		User entity = new User();
		entity.setEmail(vo.getEmail());
		entity.setId(vo.getId());
		entity.setPassword(vo.getPassword());
		entity.setSsoId(vo.getSsoId());
		entity.setState(vo.getState());
//		entity.setUserProfiles(convertToUserProfileSet(vo.getUserProfiles()));
		return entity;
	}

	@Override
	public List<User> convertToUserList(List<UserVO> entityList) {
		return null;
	}

	@Override
	public UserProfile convertToUserProfile(UserProfileVO vo) {
		if (vo == null) {
			return null;
		}
		UserProfile entity = new UserProfile();
		entity.setId(vo.getId());
		entity.setType(vo.getType());
		return entity;
	}

	@Override
	public Set<UserProfile> convertToUserProfileSet(Set<UserProfileVO> voList) {
		if(voList == null){
			return null;
		}
		Set<UserProfile> set = new HashSet<>();
		for(UserProfileVO vo:voList){
			set.add(convertToUserProfile(vo));
		}
		return set;
	}

	@Override
	public List<UserProfileVO> convertToUserProfileVOList(List<UserProfile> entityList) {
		if (entityList == null) {
			return null;
		}
		List<UserProfileVO> list = new ArrayList<>();
		for (UserProfile entity : entityList) {
			list.add(convertToUserProfileVO(entity));
		}
		return list;
	}

	@Override
	public UserUserProfile convertToUserUserProfile(UserUserProfileVO vo) {
		
		if(vo == null){
			return null;
		}
		UserUserProfile entity = new UserUserProfile();
		entity.setUserId(vo.getUserId());
		entity.setUserProfileId(vo.getUserProfileId());
		
		return entity;
	}

	@Override
	public UserUserProfileVO convertToUserUserProfileVO(UserUserProfile entity) {
		if(entity == null){
			return null;
		}
		UserUserProfileVO vo = new UserUserProfileVO();
		entity.setUserId(entity.getUserId());
		entity.setUserProfileId(entity.getUserProfileId());
		
		return vo;
	}

	@Override
	public User convertToUserVOWithoutPassword(UserVO vo) {
		if (vo == null) {
			return null;
		}
		User entity = new User();
		entity.setEmail(vo.getEmail());
		entity.setId(vo.getId());
		entity.setSsoId(vo.getSsoId());
		entity.setState(vo.getState());
		return entity;
	}

}
