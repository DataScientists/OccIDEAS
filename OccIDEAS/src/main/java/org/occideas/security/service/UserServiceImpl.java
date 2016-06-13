package org.occideas.security.service;

import java.util.List;

import org.occideas.mapper.UserMapper;
import org.occideas.security.dao.UserDao;
import org.occideas.security.model.User;
import org.occideas.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
    private UserDao dao;
	@Autowired
	private UserMapper mapper;
     
    @Autowired
    private PasswordEncoder passwordEncoder;
 
     
    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
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
	
}
