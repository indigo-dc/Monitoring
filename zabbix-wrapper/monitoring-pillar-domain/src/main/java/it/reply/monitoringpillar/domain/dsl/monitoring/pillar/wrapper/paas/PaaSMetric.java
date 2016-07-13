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
@JsonPropertyOrder({ "metricName", "metricKey", "metricValue", "metricTime", "metricUnit",
    "metricStatus", "paasThresholds", "historyClocks", "historyValues" })
public class PaaSMetric implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("metricName")
  private String metricName;
  @JsonProperty("metricKey")
  private String metricKey;
  @JsonProperty("metricValue")
  private Float metricValue;
  @JsonProperty("metricTime")
  private Object metricTime;

  @JsonProperty("metricUnit")
  private String metricUnit;

  @JsonProperty("paasThresholds")
  private List<PaasThreshold> paasThresholds = new ArrayList<PaasThreshold>();
  @JsonProperty("historyClocks")
  private List<String> historyClocks = new ArrayList<String>();
  @JsonProperty("historyValues")
  private List<Float> historyValues = new ArrayList<>();

  @JsonProperty("metricName")
  public String getMetricName() {
    return metricName;
  }

  @JsonProperty("metricName")
  public void setMetricName(String metricName) {
    this.metricName = metricName;
  }

  @JsonProperty("metricKey")
  public String getMetricKey() {
    return metricKey;
  }

  @JsonProperty("metricKey")
  public void setMetricKey(String metricKey) {
    this.metricKey = metricKey;
  }

  @JsonProperty("metricValue")
  public Float getMetricValue() {
    return metricValue;
  }

  @JsonProperty("metricValue")
  public void setMetricValue(Float metricValue) {
    this.metricValue = metricValue;
  }

  @JsonProperty("metricTime")
  public Object getMetricTime() {
    return metricTime;
  }

  @JsonProperty("metricTime")
  public void setMetricTime(Object metricTime) {
    this.metricTime = metricTime;
  }

  @JsonProperty("paasThresholds")
  public List<PaasThreshold> getPaasThresholds() {
    return paasThresholds;
  }

  @JsonProperty("paasThresholds")
  public void setPaasThresholds(List<PaasThreshold> paasThresholds) {
    this.paasThresholds = paasThresholds;
  }

  // USEFUL FOR HISTORY and creating GRAPHS

  @JsonProperty("historyClocks")
  public List<String> getHistoryClocks() {
    return historyClocks;
  }

  @JsonProperty("historyClocks")
  public void setHistoryClock(List<String> historyClocks) {
    this.historyClocks = historyClocks;
  }

  @JsonProperty("historyValues")
  public List<Float> getHistoryValues() {
    return historyValues;
  }

  @JsonProperty("historyValues")
  public void setHistoryValues(List<Float> historyValues) {
    this.historyValues = historyValues;
  }

  @JsonProperty("metricUnit")
  public String getMetricUnit() {
    return metricUnit;
  }

  @JsonProperty("metricUnit")
  public void setMetricUnit(String metricUnit) {
    this.metricUnit = metricUnit;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}