package it.reply.monitoringpillar.domain.exception;

public class NotFoundMonitoringException extends MonitoringException {

  private static final long serialVersionUID = 4377449055260705550L;

  public NotFoundMonitoringException(String message) {
    super(message);
  }

  public NotFoundMonitoringException(Throwable te) {
    super(te);
  }

  public NotFoundMonitoringException(String message, Throwable te) {
    super(message, te);
  }
}