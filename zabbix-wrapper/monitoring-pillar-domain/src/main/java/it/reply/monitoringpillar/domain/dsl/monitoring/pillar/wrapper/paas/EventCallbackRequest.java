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
@JsonPropertyOrder({ "paaSServiceId", "triggerStatus", "hostId", "hostName", "group", "metric",
    "threshold", "ip", "description" })
public class EventCallbackRequest implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("paaSServiceId")
  private String paaSServiceId;
  @JsonProperty("triggerStatus")
  private String triggerStatus;
  @JsonProperty("hostId")
  private String hostId;
  @JsonProperty("hostName")
  private String hostName;
  @JsonProperty("group")
  private String group;
  @JsonProperty("metric")
  private String metric;
  @JsonProperty("threshold")
  private String threshold;
  @JsonProperty("description")
  private String description;
  @JsonProperty("ip")
  private String ip;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("paaSServiceId")
  public String getPaaSServiceId() {
    return paaSServiceId;
  }

  @JsonProperty("paaSServiceId")
  public void setPaaSServiceId(String paaSServiceId) {
    this.paaSServiceId = paaSServiceId;
  }

  @JsonProperty("triggerStatus")
  public String getTriggerStatus() {
    return triggerStatus;
  }

  @JsonProperty("triggerStatus")
  public void setTriggerStatus(String triggerStatus) {
    this.triggerStatus = triggerStatus;
  }

  @JsonProperty("hostId")
  public String getHostId() {
    return hostId;
  }

  @JsonProperty("hostId")
  public void setHostId(String hostId) {
    this.hostId = hostId;
  }

  @JsonProperty("hostName")
  public String getHostName() {
    return hostName;
  }

  @JsonProperty("hostName")
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  @JsonProperty("group")
  public String getGroup() {
    return group;
  }

  @JsonProperty("group")
  public void setGroup(String group) {
    this.group = group;
  }

  @JsonProperty("metric")
  public String getMetric() {
    return metric;
  }

  @JsonProperty("metric")
  public void setMetric(String metric) {
    this.metric = metric;
  }

  @JsonProperty("threshold")
  public String getThreshold() {
    return threshold;
  }

  @JsonProperty("threshold")
  public void setThreshold(String threshold) {
    this.threshold = threshold;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  @JsonProperty("ip")
  public void setIp(String ip) {
    this.ip = ip;
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
