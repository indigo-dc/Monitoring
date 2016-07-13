package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol;

import it.reply.domain.dsl.prisma.ErrorCode;

public enum MonitoringErrorCode implements ErrorCode {

  MONITORING_PILLAR_ERROR(1102, "Error performing the required operation"),

  // IaaS Platform (Openstack) Error Codes
  MONIT_IAAS_GENERIC_ERROR(100, "Generic IaaS Error"),

  // @formatter:off
  // MISC
  MONIT_GENERIC_ERROR(950, "A generic error occurred", 500), MONIT_LRT_NOT_FOUND(800,
      "Specified LRT not found",
      404), MONIT_ITEM_NOT_FOUND(904, "Item not found", 404), MONIT_ITEM_ALREADY_EXISTS(905,
          "Item already exists", 409), MONIT_INTERNAL_SERVER_ERROR(500, "Internal Server Error",
              500), MONIT_SERVICE_UNAVAILABLE(503, "Service Unavailable", 503),

  MONIT_PLATFORM_STATUS_NOT_READY(906, "Platform status not ready", 500), MONIT_BAD_REQUEST(907,
      "Bad request", 400), MONIT_NOT_IMPLEMENTED(501, "Not implemented", 501),

  MONIT_WF_DEPLOY_GENERIC_ERROR(999, "Problem during deployment"), MONIT_WF_DEPLOY_MONITORING_ERROR(
      998, "Problem connecting to monitoring platform"), MONIT_WF_DEPLOY_TIMEOUT(997,
          "Timeout occurred during deployment operation"),
          // WF DEPLOY TO PAAS
          MONIT_WF_DEPLOY_PAAS_DEPLOYING_ERROR(989,
              "Problem deploying to PaaS"), MONIT_WF_DEPLOY_PAAS_DEPLOYING_CUSTOM_RECIPE_ERROR(987,
                  "Problem customizing Cloudify recipe"), 
          MONIT_WF_DEPLOY_PAAS_CFY_APP_NAME_ALREAD_EXISTS_ERROR(
                      988, "Problem deploying to PaaS: Cloudify application name already exists"),
                      // WF APPaaS - DEPLOY
                      MONIT_WF_DEPLOY_APPAAS_GENERIC_ERROR(700, "APPaaS deploy process error"),

  MONIT_WF_DEPLOY_APPAAS_ENV_CREATION_ERROR(703, "APPaaSEnvironment cannot be created"),

  MONIT_ILLEGAL_SERVICE_STATE(200001, "Illegal service state"),

  // WF IAAS
  MONIT_WF_IAAS_GENERIC_ERROR(1000, "IaaS generic error"), MONIT_WF_IAAS_INVALID_DATA(1001,
      "IaaS input invalid data"), MONIT_WF_IAAS_RESOURCE_ALREADY_EXIXTS(1001,
          "IaaS resource already exists", 409),

  MONIT_SERVICE_OPERATION_ERROR(1102, "Error performing the requuired operation"),

  MONIT_CONFIGURATION_KEY_NOT_FOUND(600, "Configuration Key not found", 404),

  MONIT_PAAS_SVC_ILLEGAL_STATE(1101, "Illegal service state"),

  MONIT_AUTHENTICATION_EXCEPTION(2401, "Authentication exception"), MONIT_INVALID_TOKEN(2402,
      "Invalid token"), MONIT_TOKEN_EXPIRED(2403, "Token expired"), MONIT_AUTHENTICATION_REQUIRED(
          2404, "Authentication required", 401), MONIT_NOT_AUTHORIZED(2405, "Not authorized", 403);

  // @formatter:on

  private final int code;
  private String description;
  private final int httpStatusCode;

  private MonitoringErrorCode(int code, String description) {
    this.code = code;
    this.description = description;
    this.httpStatusCode = 500;
  }

  private MonitoringErrorCode(int code, String description, int httpStatusCode) {

    this.code = code;
    this.description = description;
    this.httpStatusCode = httpStatusCode;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getCode() {
    return code;
  }

  public int getHttpStatusCode() {
    return httpStatusCode;
  }

  public String getName() {
    return name();
  }

  @Override
  public String toString() {
    return code + ": " + description;
  }

  /**
   * Get Error code.
   */
  public MonitoringErrorCode lookupFromCode(int errorCode) {
    for (MonitoringErrorCode e : values()) {
      if (e.code == errorCode) {
        return e;
      }
    }
    return null;
  }

  /**
   * Get Error code by looping in.
   */
  public MonitoringErrorCode lookupFromName(String errorName) {
    for (MonitoringErrorCode e : values()) {
      if (errorName.equals(e.name())) {
        return e;
      }
    }
    return null;
  }
}
