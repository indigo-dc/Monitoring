package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "machineName", "ip", "serviceCategory", "enabled", "serviceId" })
public class PaasMachineListWrapped implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("machineName")
  private String machineName;
  @JsonProperty("ip")
  private String ip;
  @JsonProperty("serviceCategory")
  private String serviceCategory;
  @JsonProperty("serviceId")
  private String serviceId;
  @JsonProperty("enabled")
  private boolean enabled;

  @JsonProperty("machineName")
  public String getMachineName() {
    return machineName;
  }

  @JsonProperty("machineName")
  public void setMachineName(String machineName) {
    this.machineName = machineName;
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

  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  @JsonProperty("ip")
  public void setIp(String ip) {
    this.ip = ip;
  }

  @JsonProperty("enabled")
  public boolean getEnabled() {
    return enabled;
  }

  @JsonProperty("enabled")
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}