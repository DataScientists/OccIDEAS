package org.occideas.security.filter;

public class WSConstants {

  public static final String AUTH_TOKEN = "X-Auth-Token";
  public static final String AUTH_USERNAME_PROP = "X-Auth-Username";
  public static final String AUTH_PWD_PROP = "X-Auth-Password";

  public static final String TOKEN_EXPIRY = "token.expiry";

  public static final String LOGIN_URL = "/web/security/login";
  public static final String EXPORT_JMETER_URL = "/web/rest/jmx/exportJMeter";


  private WSConstants() {
  }
}
