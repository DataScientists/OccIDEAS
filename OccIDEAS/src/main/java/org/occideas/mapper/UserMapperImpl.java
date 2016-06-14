package org.occideas.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.occideas.security.model.User;
import org.occideas.security.model.UserProfile;
import org.occideas.vo.UserProfileVO;
import org.occideas.vo.UserVO;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper{

	@Override
	public List<UserVO> convertToUserVOList(List<User> entityList) {
		if(entityList ==null){
			return null;
		}
		List<UserVO> list = new ArrayList<>();
		for(User entity:entityList){
			list.add(convertToUserVO(entity));
		}
		return list;
	}

	@Override
	public UserVO convertToUserVO(User entity) {
		if(entity == null){
			return null;
		}
		
		UserVO vo = new UserVO();
		vo.setEmail(entity.getEmail());
		vo.setFirstName(entity.getFirstName());
		vo.setId(entity.getId());
		vo.setLastName(entity.getLastName());
		vo.setPassword(entity.getPassword());
		vo.setSsoId(entity.getSsoId());
		vo.setState(entity.getState());
		vo.setUserProfiles(convertToUserProfileVO(entity.getUserProfiles()));
		return vo;
	}

	@Override
	public Set<UserProfileVO> convertToUserProfileVO(Set<UserProfile> entityList) {
		if(entityList == null){
			return null;
		}
		Set<UserProfileVO> set = new HashSet<>();
		for(UserProfile entity:entityList){
			set.add(convertToUserProfileVO(entity));
		}
		return set;
	}

	@Override
	public UserProfileVO convertToUserProfileVO(UserProfile entity) {
		if(entity == null){
			return null;
		}
		UserProfileVO vo = new UserProfileVO();
		vo.setId(entity.getId());
		vo.setType(entity.getType());
		return vo;
	}


}
