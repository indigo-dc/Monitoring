package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

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
@JsonPropertyOrder({ "groupName", "paasMachines" })
public class Group4WrappedMetric implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("groupName")
  private String groupName;
  @JsonProperty("paasMachines")
  private List<PaasMachine4WrappedMetric> paasMachines = new ArrayList<PaasMachine4WrappedMetric>();

  @JsonProperty("groupName")
  public String getGroupName() {
    return groupName;
  }

  @JsonProperty("groupName")
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  @JsonProperty("paasMachines")
  public List<PaasMachine4WrappedMetric> getPaasMachines() {
    return paasMachines;
  }

  @JsonProperty("paasMachines")
  public void setPaasMachines(List<PaasMachine4WrappedMetric> paasMachines) {
    this.paasMachines = paasMachines;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(groupName).append(paasMachines).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Group4WrappedMetric) == false) {
      return false;
    }
    Group4WrappedMetric rhs = ((Group4WrappedMetric) other);
    return new EqualsBuilder().append(groupName, rhs.groupName)
        .append(paasMachines, rhs.paasMachines).isEquals();
  }

}