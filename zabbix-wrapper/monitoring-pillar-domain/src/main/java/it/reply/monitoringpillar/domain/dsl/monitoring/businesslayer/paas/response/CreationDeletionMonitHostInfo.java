package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.response;

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
@JsonPropertyOrder({ "status", "hostId", "hostname", "serviceId", "hostCategory", "hostIp",
    "atomicServices", "error" })
public class CreationDeletionMonitHostInfo implements Serializable {

  private static final long serialVersionUId = -8425673865503069848L;

  @JsonProperty("status")
  private String status;
  @JsonProperty("hostId")
  private String hostId;
  @JsonProperty("hostname")
  private String hostname;
  @JsonProperty("serviceId")
  private String serviceId;
  @JsonProperty("hostCategory")
  private String hostCategory;
  @JsonProperty("hostIp")
  private String hostIp;
  @JsonProperty("atomicServices")
  private List<String> atomicServices = new ArrayList<String>();
  @JsonProperty("error")
  private Boolean error;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  @JsonProperty("hostId")
  public String getHostId() {
    return hostId;
  }

  @JsonProperty("hostId")
  public void setHostId(String hostId) {
    this.hostId = hostId;
  }

  @JsonProperty("hostname")
  public String getHostname() {
    return hostname;
  }

  @JsonProperty("hostname")
  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  @JsonProperty("serviceId")
  public String getServiceId() {
    return serviceId;
  }

  @JsonProperty("serviceId")
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  @JsonProperty("hostCategory")
  public String getHostCategory() {
    return hostCategory;
  }

  @JsonProperty("hostCategory")
  public void setHostCategory(String hostCategory) {
    this.hostCategory = hostCategory;
  }

  @JsonProperty("hostIp")
  public String getHostIp() {
    return hostIp;
  }

  @JsonProperty("hostIp")
  public void setHostIp(String hostIp) {
    this.hostIp = hostIp;
  }

  @JsonProperty("atomicServices")
  public List<String> getAtomicServices() {
    return atomicServices;
  }

  @JsonProperty("atomicServices")
  public void setAtomicServices(List<String> atomicServices) {
    this.atomicServices = atomicServices;
  }

  @JsonProperty("error")
  public Boolean getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(Boolean error) {
    this.error = error;
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

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(status).append(hostId).append(hostname).append(serviceId)
        .append(hostCategory).append(hostIp).append(atomicServices).append(error)
        .append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof CreationDeletionMonitHostInfo) == false) {
      return false;
    }
    CreationDeletionMonitHostInfo rhs = ((CreationDeletionMonitHostInfo) other);
    return new EqualsBuilder().append(status, rhs.status).append(hostId, rhs.hostId)
        .append(hostname, rhs.hostname).append(serviceId, rhs.serviceId)
        .append(hostCategory, rhs.hostCategory).append(hostIp, rhs.hostIp)
        .append(atomicServices, rhs.atomicServices).append(error, rhs.error)
        .append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
