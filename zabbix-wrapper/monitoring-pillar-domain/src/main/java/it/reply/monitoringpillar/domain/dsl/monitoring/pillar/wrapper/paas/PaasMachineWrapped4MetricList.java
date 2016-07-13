package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "machineName", "ip", "serviceCategory", "enabled", "serviceId", "metrics" })
public class PaasMachineWrapped4MetricList implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("machineName")
  private String machineName;
  @JsonProperty("ip")
  private String ip;
  @JsonProperty("enabled")
  private boolean enabled;
  @JsonProperty("metrics")
  private List<MonitoringWrappedMetric> metrics;

  @JsonProperty("machineName")
  public String getMachineName() {
    return machineName;
  }

  @JsonProperty("machineName")
  public void setMachineName(String machineName) {
    this.machineName = machineName;
  }

  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  @JsonProperty("ip")
  public void setIp(String ip) {
    this.ip = ip;
  }

  @JsonProperty("enabled")
  public boolean getEnabled() {
    return enabled;
  }

  @JsonProperty("enabled")
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @JsonProperty("metrics")
  public List<MonitoringWrappedMetric> getMetrics() {
    return metrics;
  }

  @JsonProperty("metrics")
  public void setMetrics(List<MonitoringWrappedMetric> metrics) {
    this.metrics = metrics;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}