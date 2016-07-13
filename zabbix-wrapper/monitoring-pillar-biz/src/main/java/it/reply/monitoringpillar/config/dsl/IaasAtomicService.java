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
@JsonPropertyOrder({ "active", "passive", "metrics", "name" })
public class IaasAtomicService {

  @JsonProperty("active")
  private String active;
  @JsonProperty("passive")
  private String passive;
  @JsonProperty("metrics")
  private List<Metric> metrics = new ArrayList<Metric>();
  @JsonProperty("name")
  private String name;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("active")
  public String getActive() {
    return active;
  }

  @JsonProperty("active")
  public void setActive(String active) {
    this.active = active;
  }

  @JsonProperty("passive")
  public String getPassive() {
    return passive;
  }

  @JsonProperty("passive")
  public void setPassive(String passive) {
    this.passive = passive;
  }

  @JsonProperty("metrics")
  public List<Metric> getMetrics() {
    return metrics;
  }

  @JsonProperty("metrics")
  public void setMetrics(List<Metric> metrics) {
    this.metrics = metrics;
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
   * Get metrics names.
   * 
   * @param name
   *          of metrics
   * @return metric pojo
   */
  public Metric getMetric(String name) {
    for (Metric metric : getMetrics()) {
      if (metric.getName().equalsIgnoreCase(name)) {
        return metric;
      }
    }
    return null;
  }

}
