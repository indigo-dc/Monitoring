package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "metricName", "metricValue", "metricUnit", "thresholds" })
public class MonitoringWrappedMetric implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("metricName")
  private String metricName;
  @JsonProperty("metricValue")
  private String metricValue;
  @JsonProperty("metricUnit")
  private String metricUnit;
  @JsonProperty("metricTime")
  private String metricTime;

  @JsonProperty("metricName")
  public String getMetricName() {
    return metricName;
  }

  @JsonProperty("metricName")
  public void setMetricName(String metricName) {
    this.metricName = metricName;
  }

  @JsonProperty("metricValue")
  public String getMetricValue() {
    return metricValue;
  }

  @JsonProperty("metricValue")
  public void setMetricValue(String metricValue) {
    this.metricValue = metricValue;
  }

  @JsonProperty("metricUnit")
  public String getMetricUnit() {
    return metricUnit;
  }

  @JsonProperty("metricUnit")
  public void setMetricUnit(String metricUnit) {
    this.metricUnit = metricUnit;
  }

  @JsonProperty("metricTime")
  public String getMetricTime() {
    return metricTime;
  }

  @JsonProperty("metricTime")
  public void setMetricTime(String metricTime) {
    this.metricTime = metricTime;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
