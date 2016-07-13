package it.reply.monitoringpillar.adapter.zabbix.exception;

/**
 * ZAbbix Auth Exception.
 * 
 */
public class DuplicateResourceZabbixException extends ZabbixException {

  private static final long serialVersionUID = -4128231016444989222L;

  public DuplicateResourceZabbixException(String message) {
    super(message);
  }

  public DuplicateResourceZabbixException(Throwable cause) {
    super(cause);
  }

  public DuplicateResourceZabbixException(String message, Throwable cause) {
    super(message, cause);
  }

}
