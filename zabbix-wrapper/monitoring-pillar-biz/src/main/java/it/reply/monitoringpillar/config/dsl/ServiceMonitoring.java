package it.reply.monitoringpillar.config.dsl;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "paasCategoryServices", "iaasCategoryServices" })
public class ServiceMonitoring {

  @JsonProperty("paasCategoryServices")
  private List<PaasCategoryService> paasCategoryServices = new ArrayList<PaasCategoryService>();
  @JsonProperty("iaasCategoryServices")
  private List<IaasCategoryService> iaasCategoryServices = new ArrayList<IaasCategoryService>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("paasCategoryServices")
  public List<PaasCategoryService> getPaasCategoryServices() {
    return paasCategoryServices;
  }

  @JsonProperty("paasCategoryServices")
  public void setPaasCategoryServices(List<PaasCategoryService> paasCategoryServices) {
    this.paasCategoryServices = paasCategoryServices;
  }

  @JsonProperty("iaasCategoryServices")
  public List<IaasCategoryService> getIaasCategoryServices() {
    return iaasCategoryServices;
  }

  @JsonProperty("iaasCategoryServices")
  public void setIaasCategoryServices(List<IaasCategoryService> iaasCategoryServices) {
    this.iaasCategoryServices = iaasCategoryServices;
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

  /**
   * Get Paas category.
   */
  public PaasCategoryService getPaasCategoryService(String name) {
    for (PaasCategoryService paasCategoryService : getPaasCategoryServices()) {
      if (paasCategoryService.getName().equalsIgnoreCase(name)) {
        return paasCategoryService;
      }
    }
    return null;
  }

  /**
   * Get Iaas category.
   */
  public IaasCategoryService getIaasCategoryService(String name) {
    for (IaasCategoryService iaasCategoryService : getIaasCategoryServices()) {
      if (iaasCategoryService.getName().equalsIgnoreCase(name)) {
        return iaasCategoryService;
      }
    }
    return null;
  }

}
