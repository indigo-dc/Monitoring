package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "metricName", "metricValue", "metricStatus", "metricTime", "iaasThresholds",
    "metricKey" })
public class IaaSMetric implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("metricName")
  private String metricName;
  @JsonProperty("metricValue")
  private Object metricValue;
  @JsonProperty("metricStatus")
  private String metricStatus;
  @JsonProperty("metricTime")
  private String metricTime;
  @JsonProperty("iaasThresholds")
  private List<IaasThresholdsList> iaasThresholds = new ArrayList<>();
  @JsonProperty("metricKey")
  private String metricKey;

  @JsonProperty("metricName")
  public String getMetricName() {
    return metricName;
  }

  @JsonProperty("metricName")
  public void setMetricName(String metricName) {
    this.metricName = metricName;
  }

  @JsonProperty("metricValue")
  public Object getMetricValue() {
    return metricValue;
  }

  @JsonProperty("metricValue")
  public void setMetricValue(Object metricValue) {
    this.metricValue = metricValue;
  }

  @JsonProperty("metricStatus")
  public String getMetricStatus() {
    return metricStatus;
  }

  @JsonProperty("metricStatus")
  public void setMetricStatus(String metricStatus) {
    this.metricStatus = metricStatus;
  }

  @JsonProperty("metricTime")
  public String getMetricTime() {
    return metricTime;
  }

  @JsonProperty("metricTime")
  public void setMetricTime(String metricTime) {
    this.metricTime = metricTime;
  }

  @JsonProperty("iaasThresholds")
  public List<IaasThresholdsList> getIaasThresholds() {
    return iaasThresholds;
  }

  @JsonProperty("iaasThresholds")
  public void setIaasThresholds(List<IaasThresholdsList> iaasThresholds) {
    this.iaasThresholds = iaasThresholds;
  }

  @JsonProperty("metricKey")
  public String getMetricKey() {
    return metricKey;
  }

  @JsonProperty("metricKey")
  public void setMetricKey(String metricKey) {
    this.metricKey = metricKey;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(metricName).append(metricValue).append(metricStatus)
        .append(metricTime).append(iaasThresholds).append(metricKey).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof IaaSMetric) == false) {
      return false;
    }
    IaaSMetric rhs = ((IaaSMetric) other);
    return new EqualsBuilder().append(metricName, rhs.metricName)
        .append(metricValue, rhs.metricValue).append(metricStatus, rhs.metricStatus)
        .append(metricTime, rhs.metricTime).append(iaasThresholds, rhs.iaasThresholds)
        .append(metricKey, rhs.metricKey).isEquals();
  }

}