package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.iaas.infrastructure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.reply.monitoringpillar.domain.dsl.monitoring.MonitoringFeatureUtility.InfrastructureBareMetalStatus;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "environment", "storage", "network", "compute", "availableNodes", "totalNodes",
    "machineGroups" })
public class InfrastructurePicture implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("environment")
  private String environment;
  @JsonProperty("storage")
  private InfrastructureBareMetalStatus storage;
  @JsonProperty("network")
  private InfrastructureBareMetalStatus network;
  @JsonProperty("compute")
  private InfrastructureBareMetalStatus compute;
  @JsonProperty("availableNodes")
  private int availableNodes;
  @JsonProperty("totalNodes")
  private int totalNodes;
  @JsonProperty("machineGroups")
  private List<GroupsMachine> machineGroups = new ArrayList<GroupsMachine>();

  @JsonProperty("environment")
  public String getEnvironment() {
    return environment;
  }

  @JsonProperty("environment")
  public void setTestbed(String environment) {
    this.environment = environment;
  }

  @JsonProperty("storage")
  public InfrastructureBareMetalStatus getStorage() {
    return storage;
  }

  @JsonProperty("storage")
  public void setStorage(InfrastructureBareMetalStatus storage) {
    this.storage = storage;
  }

  @JsonProperty("network")
  public InfrastructureBareMetalStatus getNetwork() {
    return network;
  }

  @JsonProperty("network")
  public void setNetwork(InfrastructureBareMetalStatus network) {
    this.network = network;
  }

  @JsonProperty("compute")
  public InfrastructureBareMetalStatus getCompute() {
    return compute;
  }

  @JsonProperty("compute")
  public void setCompute(InfrastructureBareMetalStatus compute) {
    this.compute = compute;
  }

  @JsonProperty("availableNodes")
  public int getAvailable_nodes() {
    return availableNodes;
  }

  @JsonProperty("availableNodes")
  public void setAvailable_nodes(int availableNodes) {
    this.availableNodes = availableNodes;
  }

  @JsonProperty("totalNodes")
  public int getTotal_nodes() {
    return totalNodes;
  }

  @JsonProperty("totalNodes")
  public void setTotal_nodes(int totalNodes) {
    this.totalNodes = totalNodes;
  }

  @JsonProperty("machineGroups")
  public List<GroupsMachine> getGroupsMachine() {
    return machineGroups;
  }

  @JsonProperty("machineGroups")
  public void setMachineGroups(List<GroupsMachine> machineGroups) {
    this.machineGroups = machineGroups;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(environment).append(storage).append(network).append(compute)
        .append(availableNodes).append(totalNodes).append(machineGroups).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof InfrastructurePicture) == false) {
      return false;
    }
    InfrastructurePicture rhs = ((InfrastructurePicture) other);
    return new EqualsBuilder().append(environment, rhs.environment).append(storage, rhs.storage)
        .append(network, rhs.network).append(compute, rhs.compute)
        .append(availableNodes, rhs.availableNodes).append(totalNodes, rhs.totalNodes)
        .append(machineGroups, rhs.machineGroups).isEquals();
  }

}
