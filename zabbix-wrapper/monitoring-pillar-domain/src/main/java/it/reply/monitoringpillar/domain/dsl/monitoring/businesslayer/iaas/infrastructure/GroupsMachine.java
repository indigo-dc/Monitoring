package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.iaas.infrastructure;

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
@JsonPropertyOrder({ "infrastructureGroupName", "infrastructureMachine" })
public class GroupsMachine implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("infrastructureGroupName")
  private String infrastructureGroupName;
  @JsonProperty("infrastructureMachine")
  private List<InfrastructureMachine> infrastructureMachine =
      new ArrayList<InfrastructureMachine>();

  @JsonProperty("infrastructureGroupName")
  public String getInfrastructureGroupName() {
    return infrastructureGroupName;
  }

  @JsonProperty("infrastructureGroupName")
  public void setInfrastructureGroupName(String infrastructureGroupName) {
    this.infrastructureGroupName = infrastructureGroupName;
  }

  @JsonProperty("infrastructureMachine")
  public List<InfrastructureMachine> getInfrastructureMachine() {
    return infrastructureMachine;
  }

  @JsonProperty("infrastructureMachine")
  public void setInfrastructureMachine(List<InfrastructureMachine> infrastructureMachine) {
    this.infrastructureMachine = infrastructureMachine;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(infrastructureGroupName).append(infrastructureMachine)
        .toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof GroupsMachine) == false) {
      return false;
    }
    GroupsMachine rhs = ((GroupsMachine) other);
    return new EqualsBuilder().append(infrastructureGroupName, rhs.infrastructureGroupName)
        .append(infrastructureMachine, rhs.infrastructureMachine).isEquals();
  }

}
