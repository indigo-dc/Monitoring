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
@JsonPropertyOrder({ "machineName", "connection", "metrics", "ip" })
public class IaasMachine implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("machineName")
  private String machineName;
  @JsonProperty("connection")
  private String connection;
  @JsonProperty("metrics")
  private List<IaaSMetric> metrics = new ArrayList<IaaSMetric>();
  @JsonProperty("ip")
  private String ip;

  @JsonProperty("machineName")
  public String getMachineName() {
    return machineName;
  }

  @JsonProperty("machineName")
  public void setMachineName(String machineName) {
    this.machineName = machineName;
  }

  @JsonProperty("connection")
  public String getConnection() {
    return connection;
  }

  @JsonProperty("connection")
  public void setConnection(String connection) {
    this.connection = connection;
  }

  @JsonProperty("metrics")
  public List<IaaSMetric> getMetrics() {
    return metrics;
  }

  @JsonProperty("metrics")
  public void setMetrics(List<IaaSMetric> metrics) {
    this.metrics = metrics;
  }

  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  @JsonProperty("ip")
  public void setIp(String ip) {
    this.ip = ip;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(machineName).append(connection).append(metrics).append(ip)
        .toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof IaasMachine) == false) {
      return false;
    }
    IaasMachine rhs = ((IaasMachine) other);
    return new EqualsBuilder().append(machineName, rhs.machineName)
        .append(connection, rhs.connection).append(metrics, rhs.metrics).append(ip, rhs.ip)
        .isEquals();
  }
}