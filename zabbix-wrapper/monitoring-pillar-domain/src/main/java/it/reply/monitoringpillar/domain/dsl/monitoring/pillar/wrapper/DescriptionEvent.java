package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "gruopName", "hostName", "metricName" })
public class DescriptionEvent implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("gruopName")
  private String gruopName;
  @JsonProperty("hostName")
  private String hostName;
  @JsonProperty("metricName")
  private String metricName;

  @JsonProperty("gruopName")
  public String getGruopName() {
    return gruopName;
  }

  @JsonProperty("gruopName")
  public void setGruopName(String gruopName) {
    this.gruopName = gruopName;
  }

  @JsonProperty("hostName")
  public String getHostName() {
    return hostName;
  }

  @JsonProperty("hostName")
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  @JsonProperty("metricName")
  public String getMetricName() {
    return metricName;
  }

  @JsonProperty("metricName")
  public void setMetricName(String metricName) {
    this.metricName = metricName;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}