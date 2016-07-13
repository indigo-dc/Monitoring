package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "hypervisors" })
public class HypervisorInfo implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("hypervisors")
  private List<Hypervisor> hypervisors = new ArrayList<Hypervisor>();

  @JsonProperty("hypervisors")
  public List<Hypervisor> getHypervisors() {
    return hypervisors;
  }

  @JsonProperty("hypervisors")
  public void setHypervisors(List<Hypervisor> hypervisors) {
    this.hypervisors = hypervisors;
  }

  @Override
  public String toString() {
    return "HypervisorInfo [hypervisors=" + hypervisors + "]";
  }

}