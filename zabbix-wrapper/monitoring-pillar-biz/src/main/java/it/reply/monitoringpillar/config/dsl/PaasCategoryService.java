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
@JsonPropertyOrder({ "paasAtomicServices", "name" })
public class PaasCategoryService {

  @JsonProperty("paasAtomicServices")
  private List<PaasAtomicService> paasAtomicServices = new ArrayList<PaasAtomicService>();
  @JsonProperty("name")
  private String name;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("paasAtomicServices")
  public List<PaasAtomicService> getPaasAtomicServices() {
    return paasAtomicServices;
  }

  @JsonProperty("paasAtomicServices")
  public void setPaasAtomicServices(List<PaasAtomicService> paasAtomicServices) {
    this.paasAtomicServices = paasAtomicServices;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
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
   * Get Atomic services.
   */
  public PaasAtomicService getPaasAtomicService(String name) {
    for (PaasAtomicService paasAtomicService : getPaasAtomicServices()) {
      if (paasAtomicService.getName().equalsIgnoreCase(name)) {
        return paasAtomicService;
      }
    }
    return null;
  }

}
