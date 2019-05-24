package org.occideas.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;
import java.util.Date;

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

  public TokenResponse getToken() {
    return (TokenResponse) getDetails();
  }

  public void setToken(TokenResponse token) {
    setDetails(token);
  }

  public Date getExpiry() {
    return expiry;
  }

  public void setExpiry(Date expiry) {
    this.expiry = expiry;
  }

}
