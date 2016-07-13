package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas;

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
@JsonPropertyOrder({ // "testbed",
    "iaasMachineGroups" })
public class MonitoringWrappedResponseIaas implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("iaasMachineGroups")
  private List<IaasGroupOfMachine> iaasMachineGroups = new ArrayList<IaasGroupOfMachine>();

  @JsonProperty("iaasMachineGroups")
  public List<IaasGroupOfMachine> getIaasMachineGroups() {
    return iaasMachineGroups;
  }

  @JsonProperty("iaasMachineGroups")
  public void setIaasMachineGroups(List<IaasGroupOfMachine> iaasMachineGroups) {
    this.iaasMachineGroups = iaasMachineGroups;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
