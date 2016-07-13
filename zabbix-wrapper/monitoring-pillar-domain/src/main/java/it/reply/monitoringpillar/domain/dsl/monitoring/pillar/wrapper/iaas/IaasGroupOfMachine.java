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
@JsonPropertyOrder({ "iaasGroupName", "iaasMachines" })
public class IaasGroupOfMachine implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("iaasGroupName")
  private String iaasGroupName;
  @JsonProperty("iaasMachines")
  private List<IaasMachine> iaasMachines = new ArrayList<IaasMachine>();

  @JsonProperty("iaasGroupName")
  public String getIaasGroupName() {
    return iaasGroupName;
  }

  @JsonProperty("iaasGroupName")
  public void setIaasGroupName(String iaasGroupName) {
    this.iaasGroupName = iaasGroupName;
  }

  @JsonProperty("iaasMachines")
  public List<IaasMachine> getIaasMachines() {
    return iaasMachines;
  }

  @JsonProperty("iaasMachines")
  public void setIaasMachinesList(List<IaasMachine> iaasMachines) {
    this.iaasMachines = iaasMachines;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(iaasGroupName).append(iaasMachines).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof IaasGroupOfMachine) == false) {
      return false;
    }
    IaasGroupOfMachine rhs = ((IaasGroupOfMachine) other);
    return new EqualsBuilder().append(iaasGroupName, rhs.iaasGroupName)
        .append(iaasMachines, rhs.iaasMachines).isEquals();
  }

}