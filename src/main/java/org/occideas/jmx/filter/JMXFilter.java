package org.occideas.jmx.filter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.occideas.jmx.service.JMXServiceInterface;
import org.occideas.security.filter.WSConstants;
import org.occideas.security.handler.TokenManager;
import org.occideas.utilities.PropUtil;
import org.occideas.vo.HeaderVO;
import org.occideas.vo.JMXLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class JMXFilter extends GenericFilterBean {

  @Autowired
  private JMXServiceInterface service;
  @Autowired
  private TokenManager tokenManager;
  private UrlPathHelper urlPathHelper = new UrlPathHelper();

  private static String getRequestData(final HttpServletRequest request) throws UnsupportedEncodingException {
    String payload = null;
    ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    if (wrapper != null) {
      byte[] buf = wrapper.getContentAsByteArray();
      if (buf.length > 0) {
        payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
      }
    }
    return payload;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest servletRequest = asHttp(request);
    HttpServletRequest httpRequest = new ContentCachingRequestWrapper(servletRequest);
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    String resourcePath = urlPathHelper
      .getPathWithinApplication(httpRequest);
    if (skipJmeterForSpecificURLs(resourcePath) || skipJmeterLogging()) {
      chain.doFilter(request, response);
      return;
    }
    chain.doFilter(httpRequest, response);
    String username = httpRequest.getHeader(WSConstants.AUTH_USERNAME_PROP);
    String token = httpRequest.getHeader(WSConstants.AUTH_TOKEN);
    JMXLogVO vo = new JMXLogVO();
    vo.setFunction(resourcePath);
    vo.setDeleted(0);
    setGetOrPostParameters(vo, httpRequest);
    String headers = buildHeaders(httpRequest);
    vo.setHeader(headers);
    if (!StringUtils.isEmpty(token)) {
      vo.setSessionId(token.substring(0, token.indexOf(".")) + tokenManager.parseUsernameFromToken(token) + tokenManager.parseExpiryFromToken(token));
      vo.setUserId(tokenManager.parseUsernameFromToken(token));
    } else {
      vo.setSessionId(token);
      vo.setUserId(username);
    }
    service.save(vo);
  }

  private void setGetOrPostParameters(JMXLogVO vo, HttpServletRequest httpRequest) {
    Gson gson = new Gson();
    if ("GET".equals(httpRequest.getMethod())) {
      String requestParam = "";
      if (!httpRequest.getParameterMap().isEmpty()) {
        requestParam = gson.toJson(httpRequest.getParameterMap());
      }
      vo.setGetParameters(requestParam);
    } else {
      try {
        String post = "";
        post = getRequestData(httpRequest);
        vo.setPostParameters(post);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


  private String buildHeaders(HttpServletRequest httpRequest) {
    Gson gson = new Gson();
    List<HeaderVO> list = new ArrayList<>();
    @SuppressWarnings("unchecked")
    Enumeration<String> headerNames = httpRequest.getHeaderNames();

    if (headerNames != null) {
      while (headerNames.hasMoreElements()) {
        String name = headerNames.nextElement();
        if (name.contains("x-auth-token") || name.contains("cookie")) {
          continue;
        }
        list.add(new HeaderVO(name, httpRequest.getHeader(name)));
      }
    }
    Type listType = new TypeToken<List<HeaderVO>>() {
    }.getType();
    return gson.toJson(list, listType);
  }


  private boolean skipJmeterLogging() {
    String property = PropUtil.getInstance().getProperty("jmx.skiplogging");
    return property != null && !property.isEmpty() && !"false".equals(property);
  }


  private boolean skipJmeterForSpecificURLs(String resourcePath) {
    String property = PropUtil.getInstance().getProperty("jmx.ignoreUrl");
    if (property == null || property.isEmpty()) {
      return false;
    }
    List<String> listOfURLToIgnore = splitCommaDelimitedProperty(property);
    return listOfURLToIgnore.contains(resourcePath);
  }


  private List<String> splitCommaDelimitedProperty(String property) {
    String commaDelimiter = ",";
    if (property.contains(commaDelimiter)) {
      String[] split = property.split(commaDelimiter);
      return Arrays.asList(split);
    } else {
      List<String> result = new ArrayList<>();
      result.add(property);
      return result;
    }
  }


  private HttpServletRequest asHttp(ServletRequest request) {
    return (HttpServletRequest) request;
  }


  private HttpServletResponse asHttp(ServletResponse response) {
    return (HttpServletResponse) response;
  }
}
