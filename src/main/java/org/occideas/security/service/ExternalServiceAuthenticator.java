package org.occideas.security.service;

import org.occideas.security.model.AuthenticationWithToken;

import java.security.GeneralSecurityException;

@FunctionalInterface
public interface ExternalServiceAuthenticator {

  AuthenticationWithToken authenticate(String username, String password) throws GeneralSecurityException;
}
