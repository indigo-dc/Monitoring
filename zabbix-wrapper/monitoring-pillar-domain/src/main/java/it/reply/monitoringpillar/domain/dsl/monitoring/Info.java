package it.reply.monitoringpillar.domain.dsl.monitoring;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Info implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  private String projectVersion;
  private String serverHostname;
  private String projectRevision;
  private String projectTimestamp;
  private String runtimeEnvironment;

  public String getRuntimeEnvironment() {
    return runtimeEnvironment;
  }

  public void setRuntimeEnvironment(String runtimeEnvironment) {
    this.runtimeEnvironment = runtimeEnvironment;
  }

  public String getProjectRevision() {
    return projectRevision;
  }

  public void setProjectRevision(String projectRevision) {
    this.projectRevision = projectRevision;
  }

  public String getProjectTimestamp() {
    return projectTimestamp;
  }

  public void setProjectTimestamp(String projectTimestamp) {
    this.projectTimestamp = projectTimestamp;
  }

  public String getProjectVersion() {
    return projectVersion;
  }

  public void setProjectVersion(String projectVersion) {
    this.projectVersion = projectVersion;
  }

  public String getServerHostname() {
    return serverHostname;
  }

  public void setServerHostname(String serverHostname) {
    this.serverHostname = serverHostname;
  }

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}