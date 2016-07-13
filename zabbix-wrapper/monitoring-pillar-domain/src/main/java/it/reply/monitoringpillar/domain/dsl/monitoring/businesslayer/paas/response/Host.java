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
@JsonPropertyOrder({ "name", "ip", "metrics" })
public class Host implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("name")
  private String name;
  @JsonProperty("ip")
  private String ip;
  @JsonProperty("metrics")
  private List<Metric> metrics = new ArrayList<Metric>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("ip")
  public String getip() {
    return ip;
  }

  @JsonProperty("ip")
  public void setip(String ip) {
    this.ip = ip;
  }

  @JsonProperty("metrics")
  public List<Metric> getMetrics() {
    return metrics;
  }

  @JsonProperty("metrics")
  public void setMetrics(List<Metric> metrics) {
    this.metrics = metrics;
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
    return new HashCodeBuilder().append(name).append(ip).append(metrics)
        .append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Host) == false) {
      return false;
    }
    Host rhs = ((Host) other);
    return new EqualsBuilder().append(name, rhs.name).append(ip, rhs.ip)
        .append(metrics, rhs.metrics).append(additionalProperties, rhs.additionalProperties)
        .isEquals();
  }

}