package it.reply.utils.web.ws.rest.apiclients.zabbix.exception;

import javax.ws.rs.core.Response.Status;

/**
 * Base Exception for HTTP Errors during Operations.
 * 
 */
public class ResponseException extends ZabbixClientException {

  private static final long serialVersionUID = 7294957362769575271L;

  protected Status status;

  public ResponseException(String message, Status status) {
    super(message);
    this.status = status;
  }

  public ResponseException(String message, Status status, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  /**
   * Maps an Exception based on the underlying status code.
   * 
   * @param message
   *          the message
   * @param status
   *          the status
   * @return the response exception
   */
  public static ResponseException mapException(String message, Status status) {
    return mapException(message, status, null);
  }

  /**
   * Maps an Exception based on the underlying status code.
   * 
   * @param message
   *          the message
   * @param status
   *          the status
   * @param cause
   *          the cause
   * @return the response exception
   */
  public static ResponseException mapException(String message, Status status, Throwable cause) {
    switch (status.getFamily()) {

      case CLIENT_ERROR:
        if (status.getStatusCode() == 401) {
          return new AuthenticationException(message, status, cause);
        }
        return new ClientResponseException(message, status, cause);
      case SERVER_ERROR:
        return new ServerResponseException(message, status, cause);
      default:
        return new ResponseException(message, status, cause);
    }
  }

}