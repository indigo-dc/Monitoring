package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "hostId", "vmName", "vmIp", "uuid", "uptime", "storage", "networkin",
    "networkout", "memory", "cpuload", "serviceCategory", "tagService", "service",
    "serviceCategory", "tagService" })
public class ParameterPillarRequest implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("hostid")
  public String hostid;
  @JsonProperty("vmName")
  public String vmName;
  @JsonProperty("vmIp")
  public String vmIp;
  @JsonProperty("uuid")
  public String uuid;
  @JsonProperty("uptime")
  public Boolean uptime;
  @JsonProperty("storage")
  public Boolean storage;
  @JsonProperty("networkin")
  public Boolean networkin;
  @JsonProperty("networkout")
  public Boolean networkout;
  @JsonProperty("memory")
  public Boolean memory;
  @JsonProperty("cpuload")
  public Boolean cpuload;

  @JsonProperty("service")
  public ArrayList<String> service;

  @JsonProperty("serviceCategory")
  public String serviceCategory;

  @JsonProperty("tagService")
  public String tagService;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();
  public String getMonitoringType;

  public String getHostid() {
    return hostid;
  }

  public void setHostId(String hostid) {
    this.hostid = hostid;
  }

  public String getVmName() {
    return vmName;
  }

  public void setVmName(String vmName) {
    this.vmName = vmName;
  }

  public String getVmIp() {
    return vmIp;
  }

  public void setVmIp(String vmIp) {
    this.vmIp = vmIp;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Boolean getUptime() {
    return uptime;
  }

  public void setUptime(Boolean uptime) {
    this.uptime = uptime;
  }

  public Boolean getStorage() {
    return storage;
  }

  public void setStorage(Boolean storage) {
    this.storage = storage;
  }

  public Boolean getNetworkIn() {
    return networkin;
  }

  public void setNetworkIn(Boolean networkin) {
    this.networkin = networkin;
  }

  public Boolean getNetworkOut() {
    return networkout;
  }

  public void setNetworkOut(Boolean networkout) {
    this.networkout = networkout;
  }

  public Boolean getMemory() {
    return memory;
  }

  public void setMemory(Boolean memory) {
    this.memory = memory;
  }

  public Boolean getCpuload() {
    return cpuload;
  }

  public void setCpuload(Boolean cpuload) {
    this.cpuload = cpuload;
  }

  public ArrayList<String> getService() {
    return service;
  }

  public void setService(ArrayList<String> service) {
    this.service = service;
  }

  public String getServiceCategory() {
    return serviceCategory;
  }

  public void setServiceCategory(String serviceCategory) {
    this.serviceCategory = serviceCategory;
  }

  // Service TagService
  public String getTagService() {
    return tagService;
  }

  public void setTagService(String tagService) {
    this.tagService = tagService;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  public ParameterPillarRequest() {
  }

  /**
   * Build Pillar Request.
   */
  @JsonCreator
  public ParameterPillarRequest(@JsonProperty("hostid") String hostid,

      @JsonProperty("vmname") String vmName, @JsonProperty("iaas-type") List<String> iaasType,
      @JsonProperty("vmuuid") String uuid, @JsonProperty("vmip") String vmIp,
      @JsonProperty("uptime") Boolean uptime, @JsonProperty("storage") Boolean storage,
      @JsonProperty("networkin") Boolean networkin, @JsonProperty("networkout") Boolean networkout,
      @JsonProperty("memory") Boolean memory, @JsonProperty("cpuload") Boolean cpuload,
      @JsonProperty("metric-type") String metricType,

      @JsonProperty("itemid") String itemid,

      @JsonProperty("service-type") ArrayList<String> service,

      @JsonProperty("service-category") String serviceCategory,
      @JsonProperty("tag-service") String tagService

  ) {
    this.vmName = vmName;
    this.hostid = hostid;
    this.uuid = uuid;
    this.vmIp = vmIp;
    this.uptime = uptime;
    this.storage = storage;
    this.networkin = networkin;
    this.networkout = networkout;
    this.memory = memory;
    this.cpuload = cpuload;
    this.service = service;
    this.serviceCategory = serviceCategory;
    this.tagService = tagService;
  }

}