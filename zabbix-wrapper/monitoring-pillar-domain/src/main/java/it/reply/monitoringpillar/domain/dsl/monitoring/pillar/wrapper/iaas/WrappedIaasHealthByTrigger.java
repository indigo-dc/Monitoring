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
@JsonPropertyOrder({ "hostGroup", "hostAffecteds" })
public class WrappedIaasHealthByTrigger implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("hostGroup")
  private String hostGroup;
  @JsonProperty("hostAffecteds")
  private List<HostAffected> hostAffecteds = new ArrayList<HostAffected>();

  @JsonProperty("hostGroup")
  public String getHostGroup() {
    return hostGroup;
  }

  @JsonProperty("hostGroup")
  public void setHostGroup(String hostGroup) {
    this.hostGroup = hostGroup;
  }

  @JsonProperty("hostAffecteds")
  public List<HostAffected> getHostAffecteds() {
    return hostAffecteds;
  }

  @JsonProperty("hostAffecteds")
  public void setHostAffecteds(List<HostAffected> hostAffecteds) {
    this.hostAffecteds = hostAffecteds;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(hostGroup).append(hostAffecteds).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof WrappedIaasHealthByTrigger) == false) {
      return false;
    }
    WrappedIaasHealthByTrigger rhs = ((WrappedIaasHealthByTrigger) other);
    return new EqualsBuilder().append(hostGroup, rhs.hostGroup)
        .append(hostAffecteds, rhs.hostAffecteds).isEquals();
  }

}