package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "uuid", "hostName", "ip", "hostGroup", "serviceCategory", "serviceId",
    "atomicServices", "activeMode", "port", "proxyName" })
public class HostMonitoringRequest implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("uuid")
  public String uuid;
  @JsonProperty("hostName")
  public String hostName;
  @JsonProperty("ip")
  public String ip;
  @JsonProperty("hostGroup")
  public String hostGroup;
  @JsonProperty("serviceCategory")
  public String serviceCategory;
  @JsonProperty("serviceId")
  public String serviceId;
  @JsonProperty("atomicServices")
  public List<String> atomicServices;
  @JsonProperty("activeMode")
  public Boolean activeMode;
  @JsonProperty("port")
  public List<Port> port;
  @JsonProperty("proxyName")
  public String proxyName;

  @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
  }

  @JsonProperty("uuid")
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @JsonProperty("hostName")
  public String getHostName() {
    return hostName;
  }

  @JsonProperty("hostName")
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  @JsonProperty("ip")
  public void setIp(String ip) {
    this.ip = ip;
  }

  @JsonProperty("hostGroup")
  public String getHostGroup() {
    return hostGroup;
  }

  @JsonProperty("hostGroup")
  public void setHostGroup(String hostGroup) {
    this.hostGroup = hostGroup;
  }

  @JsonProperty("serviceCategory")
  public String getServiceCategory() {
    return serviceCategory;
  }

  @JsonProperty("serviceCategory")
  public void setServiceCategory(String serviceCategory) {
    this.serviceCategory = serviceCategory;
  }

  @JsonProperty("serviceId")
  public String getServiceId() {
    return serviceId;
  }

  @JsonProperty("serviceId")
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  @JsonProperty("atomicServices")
  public List<String> getAtomicServices() {
    return atomicServices;
  }

  @JsonProperty("atomicServices")
  public void setAtomicServices(List<String> atomicServices) {
    this.atomicServices = atomicServices;
  }

  @JsonProperty("activeMode")
  public Boolean getActiveMode() {
    return activeMode;
  }

  @JsonProperty("activeMode")
  public void setActiveMode(Boolean activeMode) {
    this.activeMode = activeMode;
  }

  @JsonProperty("port")
  public List<Port> getPort() {
    return port;
  }

  @JsonProperty("port")
  public void setPort(List<Port> port) {
    this.port = port;
  }

  @JsonProperty("proxyName")
  public String getProxyName() {
    return proxyName;
  }

  @JsonProperty("proxyName")
  public void setProxyName(String proxyName) {
    this.proxyName = proxyName;
  }
}