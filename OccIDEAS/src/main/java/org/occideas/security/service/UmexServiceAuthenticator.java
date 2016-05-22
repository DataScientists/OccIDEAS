package org.occideas.security.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.occideas.security.model.AuthenticatedExternalWebService;
import org.occideas.security.model.DomainUser;
import org.occideas.security.model.TokenResponse;
import org.springframework.security.core.authority.AuthorityUtils;

public class UmexServiceAuthenticator implements ExternalServiceAuthenticator {

	private Logger log = Logger.getLogger(UmexServiceAuthenticator.class);
	
//	public Jaxb2Marshaller getMarshaller() {
//		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//		marshaller.setContextPath("com.dhl.um");
//		return marshaller;
//	}
	
	
    @Override
    public AuthenticatedExternalWebService authenticate(String username, String password) {
    	 AuthenticatedExternalWebService authenticatedExternalWebService = null;
        try {
        	if("test".equals(username) && "test".equals(password)){
        		TokenResponse tokenResponse = new TokenResponse();
        		tokenResponse.getUserInfo().put("roles", "Admin,Interviewer,Assessor");
            	tokenResponse.getUserInfo().put("userId", username);
            	authenticatedExternalWebService = new AuthenticatedExternalWebService(new DomainUser(username), null,
            			AuthorityUtils.commaSeparatedStringToAuthorityList("Admin,Interviewer,Assessor"));
            	authenticatedExternalWebService.setToken(tokenResponse);
        	}else{
        		return authenticatedExternalWebService;
        	}
        	
//        	LoginWSClient.getInstance().setMarshaller(getMarshaller());
//        	LoginWSClient.getInstance().setUnmarshaller(getMarshaller());
//        	UserProfilesResult userProfileFromUM = LoginWSClient.getInstance().
//        			getUserProfileFromUM(username, password);
//        	if(userProfileFromUM == null || 
//        			"1".equalsIgnoreCase(userProfileFromUM.getRespStatus().getCode())) {
//        		return authenticatedExternalWebService;
//        	}
//        	TokenResponse tokenResponse = new TokenResponse(); 
//        	List<ApplicationInfoWithPriority> applicationInfo = userProfileFromUM.getProfiles().getApplicationInfos()
//			.getApplicationInfo();
//			if ( applicationInfo == null 
//			  || applicationInfo.isEmpty() 
//			  || applicationInfo.get(0).getRoles() == null
//			  || applicationInfo.get(0).getRoles().getRole() == null
//			  || applicationInfo.get(0).getRoles().getRole().isEmpty()) {
//				log.error("Error on calling UMEX , applicationInfo or Role is null.");
//				return null;
//			}
//        	Roles roles = applicationInfo.get(0).getRoles();
//        	tokenResponse.getUserInfo().put("roles", roles);
//        	tokenResponse.getUserInfo().put("userId", username);
//        	
//        	tokenResponse.getUserInfo().put("facilities", new String[] { "KUL" });
//        	authenticatedExternalWebService = new AuthenticatedExternalWebService(new DomainUser(username), null,
//		                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));
//        	authenticatedExternalWebService.setToken(tokenResponse);
        } catch (Exception e) {
			log.error("Error during login in UMEX.",e);
		}

        return authenticatedExternalWebService;
    }
}
