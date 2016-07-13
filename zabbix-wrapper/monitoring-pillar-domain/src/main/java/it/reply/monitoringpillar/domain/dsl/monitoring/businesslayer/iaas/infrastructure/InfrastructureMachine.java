package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.iaas.infrastructure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "machineName", "machineIp", "machineStatus", "machineMetrics", "description" })
public class InfrastructureMachine implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("machineName")
  private String machineName;
  @JsonProperty("machineIp")
  private String machineIp;
  @JsonProperty("machineStatus")
  private String machineStatus;
  @JsonProperty("machineMetrics")
  private ArrayList<String> machineMetrics;
  // @JsonIgnore
  // private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("machineName")
  public String getMachineName() {
    return machineName;
  }

  @JsonProperty("machineName")
  public void setMachineName(String machineName) {
    this.machineName = machineName;
  }

  @JsonProperty("machineIp")
  public String getMachineIp() {
    return machineIp;
  }

  @JsonProperty("machineIp")
  public void setMachineIp(String machineIp) {
    this.machineIp = machineIp;
  }

  @JsonProperty("machineStatus")
  public String getMachineStatus() {
    return machineStatus;
  }

  @JsonProperty("machineStatus")
  public void setMachineStatus(String machineStatus) {
    this.machineStatus = machineStatus;
  }

  @JsonProperty("machineMetrics")
  public ArrayList<String> getMachineMetrics() {
    return machineMetrics;
  }

  @JsonProperty("machineMetrics")
  public void setMachineMetrics(ArrayList<String> machineMetrics) {
    this.machineMetrics = machineMetrics;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(machineName).append(machineIp).append(machineStatus)
        .append(machineStatus).append(machineMetrics).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof InfrastructureMachine) == false) {
      return false;
    }
    InfrastructureMachine rhs = ((InfrastructureMachine) other);
    return new EqualsBuilder().append(machineName, rhs.machineName).append(machineIp, rhs.machineIp)
        .append(machineStatus, rhs.machineStatus).append(machineMetrics, rhs.machineMetrics)
        .isEquals();
  }
}
