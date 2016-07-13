package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "available_nodes", "storage", "network", "total_nodes" })
public class PrismaIaasScript implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("available_nodes")
  private Integer availableNodes;
  @JsonProperty("storage")
  private String storage;
  @JsonProperty("network")
  private String network;
  @JsonProperty("total_nodes")
  private Integer totalNodes;

  @JsonProperty("available_nodes")
  public Integer getAvailableNodes() {
    return availableNodes;
  }

  @JsonProperty("available_nodes")
  public void setAvailableNodes(Integer availableNodes) {
    this.availableNodes = availableNodes;
  }

  @JsonProperty("storage")
  public String getStorage() {
    return storage;
  }

  @JsonProperty("storage")
  public void setStorage(String storage) {
    this.storage = storage;
  }

  @JsonProperty("network")
  public String getNetwork() {
    return network;
  }

  @JsonProperty("network")
  public void setNetwork(String network) {
    this.network = network;
  }

  @JsonProperty("total_nodes")
  public Integer getTotalNodes() {
    return totalNodes;
  }

  @JsonProperty("total_nodes")
  public void setTotalNodes(Integer totalNodes) {
    this.totalNodes = totalNodes;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(availableNodes).append(storage).append(network)
        .append(totalNodes).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof PrismaIaasScript) == false) {
      return false;
    }
    PrismaIaasScript rhs = ((PrismaIaasScript) other);
    return new EqualsBuilder().append(availableNodes, rhs.availableNodes)
        .append(storage, rhs.storage).append(network, rhs.network)
        .append(totalNodes, rhs.totalNodes).isEquals();
  }

}
