package it.reply.utils.web.ws.rest.apiclients.zabbix.exception;

/**
 * Base Zabbix Client Exception.
 */
public class ZabbixClientException extends Exception {

  private static final long serialVersionUID = 3965412299072282395L;

  public ZabbixClientException(String message) {
    super(message);
  }

  public ZabbixClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public ZabbixClientException(Throwable cause) {
    super(cause);
  }
}