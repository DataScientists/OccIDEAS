package org.occideas.security.service;

import java.security.GeneralSecurityException;

import org.occideas.security.model.AuthenticationWithToken;

@FunctionalInterface
public interface ExternalServiceAuthenticator {

    AuthenticationWithToken authenticate(String username, String password) throws GeneralSecurityException;
}
