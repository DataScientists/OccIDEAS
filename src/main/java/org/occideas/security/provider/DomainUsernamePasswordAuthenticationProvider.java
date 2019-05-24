package org.occideas.security.provider;

import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.occideas.jmx.service.JMXServiceInterface;
import org.occideas.security.filter.WSConstants;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.AuthenticationWithToken;
import org.occideas.security.service.ExternalServiceAuthenticator;
import org.occideas.utilities.PropUtil;
import org.occideas.vo.HeaderVO;
import org.occideas.vo.JMXLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Transactional
public class DomainUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	private ExternalServiceAuthenticator externalServiceAuthenticator;
	private TokenManager tokenManager;
	
	@Autowired
    private JMXServiceInterface service;

	public DomainUsernamePasswordAuthenticationProvider(ExternalServiceAuthenticator externalServiceAuthenticator,
			TokenManager tokenManager) {
		this.externalServiceAuthenticator = externalServiceAuthenticator;
		this.tokenManager = tokenManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Optional<String> username = (Optional<String>) authentication.getPrincipal();
		Optional<String> password = (Optional<String>) authentication.getCredentials();

		if (!username.isPresent() || !password.isPresent()) {
			throw new BadCredentialsException("Invalid Domain User Credentials");
		}
		AuthenticationWithToken resultOfAuthentication = null;
		try {
			resultOfAuthentication = externalServiceAuthenticator.authenticate(username.get(),
					password.get());
			if(resultOfAuthentication != null){
			    String tokenForUser = tokenManager.createTokenForUser(username.get(),
		            (List<GrantedAuthority>)resultOfAuthentication.getToken().getUserInfo().get("roles"));
			resultOfAuthentication.getToken().setToken(tokenForUser);
			onSuccessfulLoginSaveToJMeter(username, password, tokenForUser);
			SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
			}
		} catch (GeneralSecurityException ex) {
			throw new BadCredentialsException(ex.getMessage());
		} catch (Exception e) {
			throw new BadCredentialsException(e.getMessage());
		}
		return resultOfAuthentication;
	}
	
	private void onSuccessfulLoginSaveToJMeter(Optional<String> username, Optional<String> password, String tokenForUser)
    {
        if(skipJmeterLogging()){
            return;
        }
        JMXLogVO vo = new JMXLogVO();
        vo.setFunction(WSConstants.LOGIN_URL);
        vo.setDeleted(0);
        List<HeaderVO> list = new ArrayList<>();
        list.add(new HeaderVO(WSConstants.AUTH_USERNAME_PROP,username.get()));
        list.add(new HeaderVO(WSConstants.AUTH_PWD_PROP,password.get()));
        Gson gson = new Gson();
        Type listType = new TypeToken<List<HeaderVO>>() {}.getType();
        String headers = gson.toJson(list,listType);
        vo.setHeader(headers);
        vo.setSessionId(tokenForUser.substring(0, tokenForUser.indexOf("."))+tokenManager.parseUsernameFromToken(tokenForUser)+tokenManager.parseExpiryFromToken(tokenForUser));
        vo.setUserId(username.get());
        vo.setDeleted(0);
        service.save(vo);
    }
    
    private boolean skipJmeterLogging()
    {
        String property = PropUtil.getInstance().getProperty("jmx.skiplogging");
        if(property == null || property.isEmpty() || "false".equals(property)){
            return false;
        }
        return true;
    }

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
