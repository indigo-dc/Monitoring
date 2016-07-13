package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "machineName", "ip", "serviceCategory", "serviceId", "services" })
public class PaasMachine implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("machineName")
  private String machineName;
  @JsonProperty("ip")
  private String ip;
  @JsonProperty("serviceCategory")
  private String serviceCategory;
  @JsonProperty("serviceId")
  private String serviceId;
  @JsonProperty("services")
  private List<Service> services = new ArrayList<Service>();

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
}