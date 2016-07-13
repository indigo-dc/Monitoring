package it.reply.monitoringpillar;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.interceptor.AroundConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public class LoggingInterceptor {

  private static final Logger LOG = LogManager.getLogger();

  // Logger logger = Logger.getLogger(LoggingInterceptor.class);

  @Context
  HttpServletRequest servletRequest;

  @AroundConstruct
  private void init(InvocationContext ic) throws Exception {
    LOG.info("Entering constructor: " + ic.getClass().getName());
    try {
      ic.proceed();
    } finally {
      LOG.info("Exiting constructor: " + ic.getClass().getName());
    }
  }

  /**
   * Method for logging.
   * 
   * @param ic
   *          invocation context
   * @return generic object
   * @throws Exception
   *           just a generic one
   */
  @AroundInvoke
  public Object logMethod(InvocationContext ic) throws Exception {

    LOG.info("Entering method: " + ic.getMethod().toGenericString());

    try {
      return ic.proceed();
    } finally {

      HttpServletRequest req = getHttpServletRequest(ic);

      String scheme = req.getScheme(); // http
      String serverName = req.getServerName(); // hostname.com
      int serverPort = req.getServerPort(); // 80
      String contextPath = req.getContextPath(); // /mywebapp
      String servletPath = req.getServletPath(); // /servlet/MyServlet

      // Reconstruct original requesting URL
      StringBuilder url = new StringBuilder();
      url.append(scheme).append("://").append(serverName);

      if (serverPort != 80 && serverPort != 443) {
        url.append(":").append(serverPort);
      }

      url.append(contextPath).append(servletPath);

      String pathInfo = req.getPathInfo(); // /a/b;c=123
      if (pathInfo != null) {
        url.append(pathInfo);
      }
      String queryString = req.getQueryString(); // d=789
      if (queryString != null) {
        url.append("?").append(queryString);
      }
      HttpWrappedRequest wrappedRequest = new HttpWrappedRequest((HttpServletRequest) req);
      if ("POST".equalsIgnoreCase(req.getMethod()) || "PUT".equalsIgnoreCase(req.getMethod())) {
        url.append(" BODY " + IOUtils.toString(wrappedRequest.getReader()));
      }
      LOG.info("Receiving request of verb: " + req.getMethod() + "from: " + url.toString());

      LOG.info("Exiting method: " + ic.getMethod().toGenericString());
    }
  }

  private HttpServletRequest getHttpServletRequest(InvocationContext ic) {

    for (Object parameter : ic.getParameters()) {
      if (parameter instanceof HttpServletRequest) {

        return (HttpServletRequest) parameter;
      }
    }
    return null;
  }
}
