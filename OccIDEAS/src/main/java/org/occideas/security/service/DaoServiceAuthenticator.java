package org.occideas.security.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.occideas.security.model.AuthenticatedExternalWebService;
import org.occideas.security.model.TokenResponse;
import org.occideas.security.model.User;
import org.occideas.security.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DaoServiceAuthenticator implements ExternalServiceAuthenticator {

	private Logger log = Logger.getLogger(DaoServiceAuthenticator.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @Override
    public AuthenticatedExternalWebService authenticate(String username, String password) {
    	 AuthenticatedExternalWebService authenticatedExternalWebService = null;
    	 User user = userService.findBySso(username);
    	 log.info("User is "+user);
        try {
        	if(user!=null && !StringUtils.isEmpty(password)){
        		if(passwordEncoder.matches(password,user.getPassword())){
        			TokenResponse tokenResponse = new TokenResponse();
            		tokenResponse.getUserInfo().put("roles", getGrantedAuthorities(user));
                	tokenResponse.getUserInfo().put("userId", username);
                	authenticatedExternalWebService = new AuthenticatedExternalWebService(new User(), null,
                			getGrantedAuthorities(user));
                	authenticatedExternalWebService.setToken(tokenResponse);
                	log.info("Login successful token was generated");
        		}else{
        			log.info("Invalid username or password");
        			return authenticatedExternalWebService;
        		}
        		
        	}else{
        		return authenticatedExternalWebService;
        	}
        	
        } catch (Exception e) {
			log.error("Error during login.",e);
		}

        return authenticatedExternalWebService;
    }
    
    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
         
        for(UserProfile userProfile : user.getUserProfiles()){
            System.out.println("UserProfile : "+userProfile);
            authorities.add(new SimpleGrantedAuthority("ROLE_"+userProfile.getType()));
        }
        log.info("authorities :"+authorities);
        return authorities;
    }
}
