package org.occideas.security.audit;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
public class AuditAspect {
	
	private Logger log = Logger.getLogger(this.getClass());

	@Before(value="@annotation(auditable)")
	public void logTheAuditActivity(JoinPoint aPoint, Auditable auditable) {
		String userName = getUserName();
		String actionMade = auditable.actionType().getValue();
		String arguments = getArgs(aPoint.getArgs());
		String methodInvocation = aPoint.getThis() + "-"+aPoint.getSignature();
		if (arguments.length() > 0) {
		}
		log.info("Audit Log ---- >>>> username:"+userName+"---action made >>>"+actionMade);
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
			TokenManager tokenManager = new TokenManager();
			String token = ((TokenResponse)SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
			return tokenManager.parseUsernameFromToken(token);
		} catch (NullPointerException npe) {
			return "Unknown User";
		}
	}

}
