package org.occideas.security.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.occideas.security.filter.AuthenticationFilter;
import org.occideas.security.filter.ManagementEndpointAuthenticationFilter;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.provider.BackendAdminUsernamePasswordAuthenticationProvider;
import org.occideas.security.provider.DomainUsernamePasswordAuthenticationProvider;
import org.occideas.security.provider.TokenAuthenticationProvider;
import org.occideas.security.service.ExternalServiceAuthenticator;
import org.occideas.security.service.UmexServiceAuthenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(2)
@ImportResource({
    "classpath:applicationContext.xml" 
})
public class SecurityConfig extends WebSecurityConfigurerAdapter{


	@Value("${backend.admin.role}")
	private String backendAdminRole;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(actuatorEndpoints())
				.hasRole(backendAdminRole)
				.anyRequest()
				.authenticated()
				.and()
				.anonymous()
				.disable()
				.exceptionHandling()
				.authenticationEntryPoint(unauthorizedEntryPoint());

		http.addFilterBefore(new AuthenticationFilter(authenticationManager()),
				BasicAuthenticationFilter.class).addFilterBefore(
				new ManagementEndpointAuthenticationFilter(
						authenticationManager()),
				BasicAuthenticationFilter.class);

	}
	
	@Bean
	public TokenManager tokenManager() {
		return new TokenManager();
	}

	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				response
						.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		};
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(
				domainUsernamePasswordAuthenticationProvider())
				.authenticationProvider(
						backendAdminUsernamePasswordAuthenticationProvider())
				.authenticationProvider(tokenAuthenticationProvider());
	}

	private String[] actuatorEndpoints() {
		return new String[] { "/web", "/mobile", "/desktop" };
	}

	@Bean
	public ExternalServiceAuthenticator getExternalServiceAuthenticator() {
		return new UmexServiceAuthenticator();
	}

	@Bean
	public AuthenticationProvider domainUsernamePasswordAuthenticationProvider() {
		return new DomainUsernamePasswordAuthenticationProvider(getExternalServiceAuthenticator(), tokenManager());
	}

	@Bean
	public AuthenticationProvider backendAdminUsernamePasswordAuthenticationProvider() {
		return new BackendAdminUsernamePasswordAuthenticationProvider();
	}

	@Bean
	public AuthenticationProvider tokenAuthenticationProvider() {
		return new TokenAuthenticationProvider(tokenManager());
	}
	
}