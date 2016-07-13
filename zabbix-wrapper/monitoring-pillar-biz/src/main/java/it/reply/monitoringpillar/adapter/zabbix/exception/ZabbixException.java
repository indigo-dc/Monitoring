package it.reply.monitoringpillar.adapter.zabbix.exception;

/**
 * Base Zabbix Adapter Exception.
 * 
 */
public class ZabbixException extends Exception {

  private static final long serialVersionUID = -4128231016444989222L;

  public ZabbixException(String message) {
    super(message);
  }

  public ZabbixException(Throwable cause) {
    super(cause);
  }

  public ZabbixException(String message, Throwable cause) {
    super(message, cause);
  }

}
