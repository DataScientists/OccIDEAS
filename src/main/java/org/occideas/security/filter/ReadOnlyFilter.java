package org.occideas.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.ReadOnlyPaths;
import org.occideas.security.model.TokenResponse;
import org.occideas.security.model.UserProfileType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class ReadOnlyFilter extends GenericFilterBean {

	private static final Logger logger = LogManager.getLogger(ReadOnlyFilter.class);

	private String extractUserFromToken() {
		TokenManager tokenManager = new TokenManager();
		String token = ((TokenResponse) SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
		return tokenManager.parseUsernameFromToken(token);
	}

	private String extractAuthFromToken() {
		TokenManager tokenManager = new TokenManager();
		String token = ((TokenResponse) SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
		Collection<GrantedAuthority> auth = tokenManager.parseAuthFromToken(token);
		StringBuilder sb = new StringBuilder();
		Iterator<GrantedAuthority> iterator = auth.iterator();
		while (iterator.hasNext()) {
			LinkedHashMap<String, String> ga = (LinkedHashMap<String, String>) iterator.next();
			sb.append(ga.get("authority"));
		}
		return sb.toString();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = asHttp(request);
		HttpServletResponse httpResponse = asHttp(response);
		String uriPath = httpRequest.getRequestURI();
		if (StringUtils.containsAny(uriPath, ReadOnlyPaths.paths())) {
			String auth = extractAuthFromToken();
			if (auth.contains("ROLE_" + UserProfileType.READONLY.name())) {
				String message = "User " + extractUserFromToken() + " has read only access.";
				logger.info(message);
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, message);
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	private HttpServletRequest asHttp(ServletRequest request) {
		return (HttpServletRequest) request;
	}

	private HttpServletResponse asHttp(ServletResponse response) {
		return (HttpServletResponse) response;
	}

}
