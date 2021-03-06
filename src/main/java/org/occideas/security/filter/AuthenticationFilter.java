package org.occideas.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class AuthenticationFilter extends GenericFilterBean {

  public static final String TOKEN_SESSION_KEY = "token";
  public static final String USER_SESSION_KEY = "user";
  private static final Logger log = LogManager.getLogger(AuthenticationFilter.class);
  public static final String ERROR_MSG = "ErrorMsg";
  private AuthenticationManager authenticationManager;
  private UrlPathHelper urlPathHelper = new UrlPathHelper();

  public AuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    HttpServletRequest httpRequest = asHttp(request);
    HttpServletResponse httpResponse = asHttp(response);

    Optional<String> username = Optional.fromNullable(httpRequest.getHeader(WSConstants.AUTH_USERNAME_PROP));
    Optional<String> password = Optional.fromNullable(httpRequest.getHeader(WSConstants.AUTH_PWD_PROP));
    Optional<String> token = Optional.fromNullable(httpRequest.getHeader(WSConstants.AUTH_TOKEN));

    String resourcePath = urlPathHelper.getPathWithinApplication(httpRequest);

    try {
      boolean shouldAuthenticateLoginUserAndPassword = postToAuthenticate(httpRequest, resourcePath);
      if (shouldAuthenticateLoginUserAndPassword) {
        log.debug("Trying to authenticate user {} by X-Auth-Username method -" + username);
        processUsernamePasswordAuthentication(httpResponse, username, password);
        return;
      }

      if (token.isPresent()) {
        log.debug("Trying to authenticate user by X-Auth-Token method. Token: {}-" + token);
        processTokenAuthentication(httpResponse, token);
      }

      if(!token.isPresent()){
        String unauthorized_access = "Unauthorized Access";
        httpResponse.addHeader(ERROR_MSG, unauthorized_access);
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, unauthorized_access);
        return;
      }

      log.debug("AuthenticationFilter is passing request down the filter chain");
      addSessionContextToLogging();
      chain.doFilter(request, response);
    } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
      SecurityContextHolder.clearContext();
      log.error("Internal authentication service exception", internalAuthenticationServiceException);
      httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } catch (AuthenticationException authenticationException) {
      SecurityContextHolder.clearContext();
      if (authenticationException.getMessage().contains("expired")) {
        httpResponse.addHeader(ERROR_MSG,
          authenticationException.getMessage() + "- duration " + TokenManager.duration + "minutes");
        httpResponse.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT, TokenManager.duration);
      } else {
        httpResponse.addHeader(ERROR_MSG, authenticationException.getMessage());
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
      }
      log.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), authenticationException);
    } finally {
      ThreadContext.clearAll();
    }
  }

  private void addSessionContextToLogging() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String tokenValue = "EMPTY";
    if (authentication != null && Objects.nonNull(authentication.getDetails())) {
      PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

      // Salt password should be stored in JNDI
      tokenValue = encoder.encode(authentication.getDetails().toString());
    }
    ThreadContext.put(TOKEN_SESSION_KEY, tokenValue);

    String userValue = "EMPTY";
    if (authentication != null && !StringUtils.isEmpty(authentication.getPrincipal())) {
      userValue = authentication.getPrincipal().toString();
    }
    ThreadContext.put(USER_SESSION_KEY, userValue);
  }

  private HttpServletRequest asHttp(ServletRequest request) {
    return (HttpServletRequest) request;
  }

  private HttpServletResponse asHttp(ServletResponse response) {
    return (HttpServletResponse) response;
  }

  private boolean postToAuthenticate(HttpServletRequest httpRequest, String resourcePath) {
    return "/web/security/login".equalsIgnoreCase(resourcePath) && "POST".equals(httpRequest.getMethod());
  }

  private void processUsernamePasswordAuthentication(HttpServletResponse httpResponse, Optional<String> username,
                                                     Optional<String> password) throws IOException {
    Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
    SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    httpResponse.setStatus(HttpServletResponse.SC_OK);

    TokenResponse tokenResponse = (TokenResponse) resultOfAuthentication.getDetails();
    // tokenResponse.setFacRoleDDValues(UmexRespUtil.getFacRoleDDValues(tokenResponse.getUserInfo()));
    // tokenResponse.setFacDDVals(UmexRespUtil.getFacilityDDValues(tokenResponse.getFacRoleDDValues()));
    // tokenResponse.setRoleDDVals(UmexRespUtil.getRoleDDValues(tokenResponse.getFacRoleDDValues()));
    String tokenJsonResponse = new ObjectMapper().writeValueAsString(tokenResponse);

    httpResponse.addHeader("Content-Type", "application/json");
    httpResponse.getWriter().print(tokenJsonResponse);
  }

  private Authentication tryToAuthenticateWithUsernameAndPassword(Optional<String> username,
                                                                  Optional<String> password) {
    UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username,
      password);
    return tryToAuthenticate(requestAuthentication);
  }

  private void processTokenAuthentication(HttpServletResponse httpResponse, Optional<String> token)
    throws JsonProcessingException {
    Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
    SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    httpResponse.addHeader("Content-Type", "application/json");
    httpResponse.addHeader(WSConstants.AUTH_TOKEN,
      new ObjectMapper().writeValueAsString(resultOfAuthentication.getDetails()));
  }

  private Authentication tryToAuthenticateWithToken(Optional<String> token) {
    PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token,
      null);
    return tryToAuthenticate(requestAuthentication);
  }

  private Authentication tryToAuthenticate(Authentication requestAuthentication) {
    Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
    if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
      throw new InternalAuthenticationServiceException(
        "Unable to authenticate Domain User for provided credentials");
    }
    SecurityContextHolder.getContext().setAuthentication(responseAuthentication);
    log.debug("User successfully authenticated");
    return responseAuthentication;
  }
}