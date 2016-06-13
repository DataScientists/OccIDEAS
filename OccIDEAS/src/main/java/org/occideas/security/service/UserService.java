package org.occideas.security.service;

import java.util.List;

import org.occideas.security.model.User;
import org.occideas.vo.UserVO;

public interface UserService {

	void save(User user);
    
    User findById(int id);
     
    User findBySso(String sso);
    
    List<UserVO> getUserRoles();
	
}
