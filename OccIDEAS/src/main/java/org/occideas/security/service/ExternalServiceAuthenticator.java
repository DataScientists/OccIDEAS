package org.occideas.security.service;

import org.occideas.security.model.AuthenticationWithToken;

@FunctionalInterface
public interface ExternalServiceAuthenticator {

    AuthenticationWithToken authenticate(String username, String password);
}
