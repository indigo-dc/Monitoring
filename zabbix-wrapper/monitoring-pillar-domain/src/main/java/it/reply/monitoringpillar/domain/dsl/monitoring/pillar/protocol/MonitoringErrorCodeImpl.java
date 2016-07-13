package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol;

import it.reply.domain.dsl.prisma.ErrorCode;

import java.io.Serializable;

public class MonitoringErrorCodeImpl implements ErrorCode, Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  private int code;
  private String name;
  private String description;
  private int httpStatusCode;

  /**
   * Get Error code mapping it in zabbix.
   * 
   * @param code
   *          code
   * @param name
   *          name
   * @param description
   *          string
   * @param httpStatusCode
   *          code
   */
  public MonitoringErrorCodeImpl(int code, String name, String description, int httpStatusCode) {
    super();
    this.code = code;
    this.name = name;
    this.description = description;
    this.httpStatusCode = httpStatusCode;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public int getCode() {
    return this.code;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int getHttpStatusCode() {
    return this.httpStatusCode;
  }

  @Override
  public ErrorCode lookupFromCode(int errorCode) {
    return this;
  }

  @Override
  public ErrorCode lookupFromName(String errorName) {
    return this;
  }

}
