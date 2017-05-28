package org.occideas.security.service;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.security.model.AuthenticatedExternalWebService;
import org.occideas.security.model.State;
import org.occideas.security.model.TokenResponse;
import org.occideas.security.model.User;
import org.occideas.security.model.UserProfile;
import org.occideas.security.model.UserProfileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DaoServiceAuthenticator implements ExternalServiceAuthenticator {

	private Logger log = LogManager.getLogger(DaoServiceAuthenticator.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public AuthenticatedExternalWebService authenticate(String username, String password)
			throws GeneralSecurityException {
		AuthenticatedExternalWebService authenticatedExternalWebService = null;
		User user = userService.findBySso(username);
		log.debug("User is " + user);
		if (user != null && !StringUtils.isEmpty(password)) {
			if (!State.ACTIVE.getState().equals(user.getState())) {
				log.debug("User is no longer Active.");
				throw new GeneralSecurityException("User is no longer Active.");
			}

			if (passwordEncoder.matches(password, user.getPassword())) {
				TokenResponse tokenResponse = new TokenResponse();
				tokenResponse.getUserInfo().put("roles", getGrantedAuthorities(user));
				tokenResponse.getUserInfo().put("userId", username);
				authenticatedExternalWebService = new AuthenticatedExternalWebService(new User(), null,
						getGrantedAuthorities(user));
				authenticatedExternalWebService.setToken(tokenResponse);
				log.debug("Login successful token was generated");
			} else {
				log.debug("Invalid username or password");
				throw new GeneralSecurityException("Invalid Domain User Credentials");
			}

		} else {
			return authenticatedExternalWebService;
		}

		return authenticatedExternalWebService;
	}

	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (UserProfile userProfile : user.getUserProfiles()) {
			System.out.println("UserProfile : " + userProfile);
			authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getType()));
		}
		if (authorities.isEmpty()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + UserProfileType.READONLY.name()));
		}
		log.debug("authorities :" + authorities);
		return authorities;
	}
}
