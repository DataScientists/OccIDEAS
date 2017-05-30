package org.occideas.security.provider;

import java.security.GeneralSecurityException;
import java.util.List;

import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.AuthenticationWithToken;
import org.occideas.security.service.ExternalServiceAuthenticator;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

@Transactional
public class DomainUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	private ExternalServiceAuthenticator externalServiceAuthenticator;
	private TokenManager tokenManager;

	public DomainUsernamePasswordAuthenticationProvider(ExternalServiceAuthenticator externalServiceAuthenticator,
			TokenManager tokenManager) {
		this.externalServiceAuthenticator = externalServiceAuthenticator;
		this.tokenManager = tokenManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Optional<String> username = (Optional<String>) authentication.getPrincipal();
		Optional<String> password = (Optional<String>) authentication.getCredentials();

		if (!username.isPresent() || !password.isPresent()) {
			throw new BadCredentialsException("Invalid Domain User Credentials");
		}
		AuthenticationWithToken resultOfAuthentication = null;
		try {
			resultOfAuthentication = externalServiceAuthenticator.authenticate(username.get(),
					password.get());
			if(resultOfAuthentication != null){
			resultOfAuthentication.getToken().setToken(tokenManager.createTokenForUser(username.get(),
					(List<GrantedAuthority>) resultOfAuthentication.getToken().getUserInfo().get("roles")));
			SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
			}
		} catch (GeneralSecurityException ex) {
			throw new BadCredentialsException(ex.getMessage());
		} catch (Exception e) {
			throw new BadCredentialsException(e.getMessage());
		}
		return resultOfAuthentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
