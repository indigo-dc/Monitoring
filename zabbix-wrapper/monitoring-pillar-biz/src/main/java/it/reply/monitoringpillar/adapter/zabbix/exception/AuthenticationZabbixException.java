package it.reply.monitoringpillar.adapter.zabbix.exception;

/**
 * Zabbix Auth Exception.
 * 
 */
public class AuthenticationZabbixException extends ZabbixException {

  private static final long serialVersionUID = -4128231016444989222L;

  public AuthenticationZabbixException(String message) {
    super(message);
  }

  public AuthenticationZabbixException(Throwable cause) {
    super(cause);
  }

  public AuthenticationZabbixException(String message, Throwable cause) {
    super(message, cause);
  }

}
