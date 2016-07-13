package it.reply.monitoringpillar.adapter.zabbix.exception;

/**
 * Zabbix NotImplemented Exception.
 * 
 */
public class NotImplementedZabbixException extends ZabbixException {

  private static final long serialVersionUID = -4128231016444989222L;

  public NotImplementedZabbixException(String message) {
    super(message);
  }

  public NotImplementedZabbixException(Throwable cause) {
    super(cause);
  }

  public NotImplementedZabbixException(String message, Throwable cause) {
    super(message, cause);
  }

}
