package org.occideas.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class AuthenticatedExternalWebService extends AuthenticationWithToken {

    /**
	 * 
	 */
	private static final long serialVersionUID = 839634049584179187L;

	public AuthenticatedExternalWebService(Object aPrincipal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
    }
}
