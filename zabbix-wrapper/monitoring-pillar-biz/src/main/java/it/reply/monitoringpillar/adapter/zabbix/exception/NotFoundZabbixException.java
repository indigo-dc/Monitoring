package it.reply.monitoringpillar.adapter.zabbix.exception;

/**
 * Zabbix Auth Exception.
 * 
 */
public class NotFoundZabbixException extends ZabbixException {

  private static final long serialVersionUID = -4128231016444989222L;

  public NotFoundZabbixException(String message) {
    super(message);
  }

  public NotFoundZabbixException(Throwable cause) {
    super(cause);
  }

  public NotFoundZabbixException(String message, Throwable cause) {
    super(message, cause);
  }

}
