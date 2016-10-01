package org.occideas.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

public class AuthenticationFilter extends GenericFilterBean {
	
	public static final String TOKEN_SESSION_KEY = "token";
	public static final String USER_SESSION_KEY = "user";
	private AuthenticationManager authenticationManager;
	private UrlPathHelper urlPathHelper = new UrlPathHelper(); 
	private static final Logger log = Logger.getLogger(AuthenticationFilter.class);
	

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = asHttp(request);
		HttpServletResponse httpResponse = asHttp(response);

		Optional<String> username = Optional.fromNullable(httpRequest
				.getHeader(WSConstants.AUTH_USERNAME_PROP));
		Optional<String> password = Optional.fromNullable(httpRequest
				.getHeader(WSConstants.AUTH_PWD_PROP));
		Optional<String> token = Optional.fromNullable(httpRequest
				.getHeader(WSConstants.AUTH_TOKEN));

		String resourcePath = urlPathHelper
				.getPathWithinApplication(httpRequest);

		try {
			if (postToAuthenticate(httpRequest, resourcePath)) {
				log.debug(
						"Trying to authenticate user {} by X-Auth-Username method -"+
						username);
				processUsernamePasswordAuthentication(httpResponse, username,
						password);
				return;
			}

			if (token.isPresent()) {
				log.debug(
						"Trying to authenticate user by X-Auth-Token method. Token: {}-"+
						token);
				processTokenAuthentication(httpResponse,token);
			}

			log.debug("AuthenticationFilter is passing request down the filter chain");
			addSessionContextToLogging();
			chain.doFilter(request, response);
		} catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
			SecurityContextHolder.clearContext();
			log.error("Internal authentication service exception",
					internalAuthenticationServiceException);
			httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (AuthenticationException authenticationException) {
			SecurityContextHolder.clearContext();
			if(authenticationException.getMessage().contains("expired")){
				httpResponse.addHeader("ErrorMsg", authenticationException.getMessage()+"- duration "
						+TokenManager.duration+"minutes");
				httpResponse.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT, TokenManager.duration);	
			}else{
				httpResponse.addHeader("ErrorMsg", authenticationException.getMessage());
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
			}
			log.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), authenticationException);
		} finally {
			MDC.remove(TOKEN_SESSION_KEY);
			MDC.remove(USER_SESSION_KEY);
		}
	}

	private void addSessionContextToLogging() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		String tokenValue = "EMPTY";
		if (authentication != null
				&& !StringUtils.isEmpty(authentication.getDetails())){
			MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder(
					"SHA-1");
			//Salt password should be stored in JNDI
			tokenValue = encoder.encodePassword(authentication.getDetails()
					.toString(), "secret_salt_password");
		}
		MDC.put(TOKEN_SESSION_KEY, tokenValue);

		String userValue = "EMPTY";
		if (authentication != null
				&& !StringUtils.isEmpty(authentication.getPrincipal())) {
			userValue = authentication.getPrincipal().toString();
		}
		MDC.put(USER_SESSION_KEY, userValue);
	}

	private HttpServletRequest asHttp(ServletRequest request) {
		return (HttpServletRequest) request;
	}

	private HttpServletResponse asHttp(ServletResponse response) {
		return (HttpServletResponse) response;
	}

	private boolean postToAuthenticate(HttpServletRequest httpRequest,
			String resourcePath) {
		return "/web/security/login".equalsIgnoreCase(resourcePath)
				&& "POST".equals(httpRequest.getMethod());
	}

	private void processUsernamePasswordAuthentication(
			HttpServletResponse httpResponse, Optional<String> username,
			Optional<String> password) throws IOException {
		Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(
				username, password);
		SecurityContextHolder.getContext().setAuthentication(
				resultOfAuthentication);
		httpResponse.setStatus(HttpServletResponse.SC_OK);
		
		TokenResponse tokenResponse = (TokenResponse)resultOfAuthentication.getDetails();
//		tokenResponse.setFacRoleDDValues(UmexRespUtil.getFacRoleDDValues(tokenResponse.getUserInfo()));
//		tokenResponse.setFacDDVals(UmexRespUtil.getFacilityDDValues(tokenResponse.getFacRoleDDValues()));
//		tokenResponse.setRoleDDVals(UmexRespUtil.getRoleDDValues(tokenResponse.getFacRoleDDValues()));
		String tokenJsonResponse = new ObjectMapper().writeValueAsString(tokenResponse);
		
		httpResponse.addHeader("Content-Type", "application/json");
		httpResponse.getWriter().print(tokenJsonResponse);
	}

	private Authentication tryToAuthenticateWithUsernameAndPassword(
			Optional<String> username, Optional<String> password) {
		UsernamePasswordAuthenticationToken requestAuthentication = 
				new UsernamePasswordAuthenticationToken(username, password); 
		return tryToAuthenticate(requestAuthentication);
	}

	private void processTokenAuthentication(HttpServletResponse httpResponse,Optional<String> token) throws JsonProcessingException {
		Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
		SecurityContextHolder.getContext().setAuthentication(
				resultOfAuthentication);
		httpResponse.addHeader("Content-Type", "application/json");
		httpResponse.addHeader(WSConstants.AUTH_TOKEN, new ObjectMapper()
				.writeValueAsString(resultOfAuthentication.getDetails()));
	}

	private Authentication tryToAuthenticateWithToken(Optional<String> token) {
		PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(
				token, null);
		return tryToAuthenticate(requestAuthentication);
	}

	private Authentication tryToAuthenticate(
			Authentication requestAuthentication) {
		Authentication responseAuthentication = authenticationManager
				.authenticate(requestAuthentication);
		if (responseAuthentication == null
				|| !responseAuthentication.isAuthenticated()) {
			throw new InternalAuthenticationServiceException(
					"Unable to authenticate Domain User for provided credentials");
		}
		SecurityContextHolder.getContext().setAuthentication(responseAuthentication);
		log.debug("User successfully authenticated");
		return responseAuthentication;
	}
}