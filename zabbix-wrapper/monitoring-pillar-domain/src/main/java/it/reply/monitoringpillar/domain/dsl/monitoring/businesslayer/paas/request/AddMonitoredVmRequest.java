package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "vmUuid", "vmIp", "vmName", "services" })
public class AddMonitoredVmRequest implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("vmUuid")
  private String vmUuid;
  @JsonProperty("vmIp")
  private String vmIp;
  @JsonProperty("vmName")
  private String vmName;
  @JsonProperty("services")
  private List<Service> services = new ArrayList<Service>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("vmUuid")
  public String getVmUuid() {
    return vmUuid;
  }

  @JsonProperty("vmUuid")
  public void setVmUuid(String vmUuid) {
    this.vmUuid = vmUuid;
  }

  @JsonProperty("vmIp")
  public String getVmIp() {
    return vmIp;
  }

  @JsonProperty("vmIp")
  public void setVmIp(String vmIp) {
    this.vmIp = vmIp;
  }

  @JsonProperty("vmName")
  public String getVmName() {
    return vmName;
  }

  @JsonProperty("vmName")
  public void setVmName(String vmName) {
    this.vmName = vmName;
  }

  @JsonProperty("services")
  public List<Service> getServices() {
    return services;
  }

  @JsonProperty("services")
  public void setServices(List<Service> services) {
    this.services = services;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object other) {
    return EqualsBuilder.reflectionEquals(this, other);
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
