package org.occideas.security.provider;

import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.AuthenticationWithToken;
import org.occideas.security.service.ExternalServiceAuthenticator;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.base.Optional;

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
        AuthenticationWithToken resultOfAuthentication = externalServiceAuthenticator.authenticate(username.get(), password.get());
        if(resultOfAuthentication == null){
        	throw new BadCredentialsException("Invalid Domain User Credentials");
        }
        resultOfAuthentication.getToken().setToken(tokenManager.createTokenForUser(username.get(),resultOfAuthentication.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
        return resultOfAuthentication;
    }
    

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
