package org.occideas.security.provider;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.AuthenticatedExternalWebService;
import org.occideas.security.model.DomainUser;
import org.occideas.security.model.TokenResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.google.common.base.Optional;

public class TokenAuthenticationProvider implements AuthenticationProvider {

	private TokenManager tokenManager;

	public TokenAuthenticationProvider(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		@SuppressWarnings("unchecked")
		Optional<String> token = (Optional<String>) authentication.getPrincipal();
		if (!token.isPresent() || token.get().isEmpty()) {
			throw new BadCredentialsException("Invalid token");
		}
		validateToken(token.get());
		String user = tokenManager.parseUsernameFromToken(token.get());
		AuthenticatedExternalWebService authenticatedExternalWebService = new 
				AuthenticatedExternalWebService(new DomainUser(user), null,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setToken(tokenManager.createTokenForUser(user,
				authenticatedExternalWebService.getAuthorities()));
		authenticatedExternalWebService.setToken(tokenResponse);
		authenticatedExternalWebService.setAuthenticated(true);
		return authenticatedExternalWebService;
	}
	
	private void validateToken(String token){
		String parseExpiryFromToken = tokenManager.parseExpiryFromToken(token);
		if(tokenManager.parseUsernameFromToken(token) == null || 
				tokenManager.parseAuthFromToken(token) == null ||
				parseExpiryFromToken == null){
			throw new BadCredentialsException("Invalid token");
		}
		if(ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime().isAfter(LocalDateTime.parse(parseExpiryFromToken))){
			throw new BadCredentialsException("Token expired");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		 return authentication.equals(PreAuthenticatedAuthenticationToken.class);
	}
}
