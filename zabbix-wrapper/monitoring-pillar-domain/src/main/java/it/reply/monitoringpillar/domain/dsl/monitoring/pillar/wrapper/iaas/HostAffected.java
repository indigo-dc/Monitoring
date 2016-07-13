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
@JsonPropertyOrder({ "hostName", "triggerShots" })
public class HostAffected implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("hostName")
  private String hostName;
  @JsonProperty("triggerShots")
  private List<TriggerShot> triggerShots = new ArrayList<TriggerShot>();

  @JsonProperty("hostName")
  public String getHostName() {
    return hostName;
  }

  @JsonProperty("hostName")
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  @JsonProperty("triggerShots")
  public List<TriggerShot> getTriggerShots() {
    return triggerShots;
  }

  @JsonProperty("triggerShots")
  public void setTriggerShots(List<TriggerShot> triggerShots) {
    this.triggerShots = triggerShots;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(hostName).append(triggerShots).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof HostAffected) == false) {
      return false;
    }
    HostAffected rhs = ((HostAffected) other);
    return new EqualsBuilder().append(hostName, rhs.hostName).append(triggerShots, rhs.triggerShots)
        .isEquals();
  }

}