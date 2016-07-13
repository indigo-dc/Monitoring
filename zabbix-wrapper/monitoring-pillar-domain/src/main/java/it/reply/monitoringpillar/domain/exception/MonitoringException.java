package it.reply.monitoringpillar.domain.exception;

/**
 * Basic Monitoring exception.
 */
public class MonitoringException extends RuntimeException {

  private static final long serialVersionUID = 2834951083585038374L;

  public MonitoringException(String message) {
    super(message);
  }

  public MonitoringException(Throwable te) {
    super(te);
  }

  public MonitoringException(String message, Throwable te) {
    super(message, te);
  }
}