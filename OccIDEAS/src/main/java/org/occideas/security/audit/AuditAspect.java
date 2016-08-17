package org.occideas.security.audit;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.occideas.security.dao.AuditDao;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.AuditLog;
import org.occideas.security.model.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
public class AuditAspect {
	
	private Logger log = Logger.getLogger(this.getClass()); 
	
	@Autowired
	private AuditDao dao;

	@Before(value="@annotation(auditable)")
	@Transactional
	public void logTheAuditActivity(JoinPoint aPoint, Auditable auditable) {
		AuditLog auditLog = new AuditLog();
		auditLog.setUsername(getUserName());
		auditLog.setUserType(getRoles());
		auditLog.setAction(auditable.actionType().getValue());
		String arguments = getArgs(aPoint.getArgs());
		String[] split = aPoint.getThis().toString().split("\\.");
		String classNameLast = split[split.length - 1];
		String methodInvocation =  classNameLast.substring(0, classNameLast.indexOf("@"))+ "-"+aPoint.getSignature().getName();
		if (arguments.length() > 0) {
			auditLog.setArguments(arguments.getBytes());
		}
		auditLog.setMethod(methodInvocation);
		auditLog.setDate(new Timestamp(new Date().getTime()));
		dao.save(auditLog);
	}

	private String getArgs(Object[] args) {
		String arguments = "";
		int argCount = 0;

		for (Object object : args) {
			if (argCount > 0) {
				arguments += ", ";
			}
			arguments += "arg[" + ++argCount + "]=" + "[" + object + "]";
		}
		return arguments;
	}

	private String getUserName() {
		try {
			return extractUserFromToken();
		} catch (NullPointerException npe) {
			log.error("Function getUserName in AuditAspect - No UserName");
			return "";
		}
	}
	
	private String getRoles(){
		try{
			return extractAuthFromToken();
		}catch(NullPointerException npe){
			log.error("Function getRoles in AuditAspect - No Roles");
			return "";
		}
	}

	private String extractAuthFromToken() {
		TokenManager tokenManager = new TokenManager();
		String token = ((TokenResponse)SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
		Collection<GrantedAuthority> auth = tokenManager.parseAuthFromToken(token);
		StringBuilder sb = new StringBuilder();
		Iterator<GrantedAuthority> iterator = auth.iterator();
		while(iterator.hasNext()){
			LinkedHashMap<String, String> ga = (LinkedHashMap<String, String>)iterator.next();
			sb.append(ga.get("authority"));
		}
		return sb.toString();
	}

	private String extractUserFromToken() {
		TokenManager tokenManager = new TokenManager();
		String token = ((TokenResponse)SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
		return tokenManager.parseUsernameFromToken(token);
	}

}
