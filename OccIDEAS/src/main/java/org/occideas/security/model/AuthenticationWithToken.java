package org.occideas.security.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date expiry;

	public AuthenticationWithToken(Object aPrincipal, Object aCredentials) {
		super(aPrincipal, aCredentials);
	}

	public AuthenticationWithToken(Object aPrincipal, Object aCredentials,
			Collection<? extends GrantedAuthority> anAuthorities) {
		super(aPrincipal, aCredentials, anAuthorities);
	}

	public void setToken(TokenResponse token) {
		setDetails(token);
	}

	public TokenResponse getToken() {
		return (TokenResponse) getDetails();
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

}
