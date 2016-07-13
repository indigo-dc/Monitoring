package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "serviceId", "status", "vmuuid", "vmName", "tenant", "ip", "time",
    "description" })
public class MonitoringPillarEventCallbackResponse implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("serviceId")
  private String serviceId;
  @JsonProperty("status")
  private String status;
  @JsonProperty("vmuuid")
  private String vmuuid;
  @JsonProperty("vmName")
  private String vmName;
  @JsonProperty("tenant")
  private String tenant;
  @JsonProperty("ip")
  private String ip;
  @JsonProperty("metricName")
  private String metricName;
  @JsonProperty("triggerName")
  private String triggerName;
  @JsonProperty("description")
  private String description;
  @JsonProperty("time")
  private Long time;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("serviceId")
  public String getServiceId() {
    return serviceId;
  }

  @JsonProperty("serviceId")
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  @JsonProperty("vmuuid")
  public String getVmuuid() {
    return vmuuid;
  }

  @JsonProperty("vmuuid")
  public void setVmuuid(String vmuuid) {
    this.vmuuid = vmuuid;
  }

  @JsonProperty("vmName")
  public String getVmName() {
    return vmName;
  }

  @JsonProperty("vmName")
  public void setVmName(String vmName) {
    this.vmName = vmName;
  }

  @JsonProperty("tenant")
  public String getTenant() {
    return tenant;
  }

  @JsonProperty("tenant")
  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  @JsonProperty("ip")
  public void setIp(String ip) {
    this.ip = ip;
  }

  @JsonProperty("metricName")
  public String getMetricName() {
    return metricName;
  }

  @JsonProperty("metricName")
  public void setMetricName(String metricName) {
    this.metricName = metricName;
  }

  @JsonProperty("triggerName")
  public String geTriggerName() {
    return triggerName;
  }

  @JsonProperty("triggerName")
  public void setTriggerName(String triggerName) {
    this.triggerName = triggerName;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("time")
  public Long getTime() {
    return time;
  }

  @JsonProperty("time")
  public void setTime(Long time) {
    this.time = time;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
