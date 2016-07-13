package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "available_nodes", "storage", "network", "total_nodes", "compute" })
public class IaaSHealth implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("available_nodes")
  private Integer availableNodes;
  @JsonProperty("storage")
  private String storage;
  @JsonProperty("network")
  private String network;
  @JsonProperty("total_nodes")
  private Integer totalNodes;
  @JsonProperty("compute")
  private String compute;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("available_nodes")
  public Integer getAvailableNodes() {
    return availableNodes;
  }

  @JsonProperty("available_nodes")
  public void setAvailableNodes(Integer availableNodes) {
    this.availableNodes = availableNodes;
  }

  public IaaSHealth withAvailableNodes(Integer availableNodes) {
    this.availableNodes = availableNodes;
    return this;
  }

  @JsonProperty("storage")
  public String getStorage() {
    return storage;
  }

  @JsonProperty("storage")
  public void setStorage(String storage) {
    this.storage = storage;
  }

  public IaaSHealth withStorage(String storage) {
    this.storage = storage;
    return this;
  }

  @JsonProperty("network")
  public String getNetwork() {
    return network;
  }

  @JsonProperty("network")
  public void setNetwork(String network) {
    this.network = network;
  }

  public IaaSHealth withNetwork(String network) {
    this.network = network;
    return this;
  }

  @JsonProperty("compute")
  public String getCompute() {
    return compute;
  }

  @JsonProperty("compute")
  public void setCompute(String compute) {
    this.compute = compute;
  }

  public IaaSHealth withCompute(String compute) {
    this.compute = compute;
    return this;
  }

  @JsonProperty("total_nodes")
  public Integer getTotalNodes() {
    return totalNodes;
  }

  @JsonProperty("total_nodes")
  public void setTotalNodes(Integer totalNodes) {
    this.totalNodes = totalNodes;
  }

  public IaaSHealth withTotalNodes(Integer totalNodes) {
    this.totalNodes = totalNodes;
    return this;
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
  public String toString() {
    return "OpenstackIaaSHealth [availableNodes=" + availableNodes + ", storage=" + storage
        + ", network=" + network + ", totalNodes=" + totalNodes + ", compute=" + compute
        + ", additionalProperties=" + additionalProperties + "]";
  }

}