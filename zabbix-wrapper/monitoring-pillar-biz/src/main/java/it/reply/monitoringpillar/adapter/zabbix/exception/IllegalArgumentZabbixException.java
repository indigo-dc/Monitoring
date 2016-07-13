package it.reply.monitoringpillar.adapter.zabbix.exception;

/**
 * Zabbix Illegal parameters exception Exception.
 * 
 */
public class IllegalArgumentZabbixException extends ZabbixException {

  private static final long serialVersionUID = -4128231016444989222L;

  public IllegalArgumentZabbixException(String message) {
    super(message);
  }

  public IllegalArgumentZabbixException(Throwable cause) {
    super(cause);
  }

  public IllegalArgumentZabbixException(String message, Throwable cause) {
    super(message, cause);
  }

}
