package org.occideas.security.service;

import java.util.List;

import org.occideas.mapper.UserMapper;
import org.occideas.security.dao.UserDao;
import org.occideas.security.dao.UserProfileDao;
import org.occideas.security.dao.UserUserProfileDao;
import org.occideas.security.model.User;
import org.occideas.security.model.UserProfile;
import org.occideas.vo.UserProfileVO;
import org.occideas.vo.UserUserProfileVO;
import org.occideas.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
    private UserDao dao;
	@Autowired
	private UserProfileDao profileDao;
	@Autowired
	private UserUserProfileDao userUserProfileDao;
	@Autowired
	private UserMapper mapper;
     
    public UserVO save(UserVO vo){
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        vo.setPassword(passwordEncoder.encode(vo.getPassword()));
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
	
}
