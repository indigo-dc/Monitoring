package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.trigger;

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
@JsonPropertyOrder({ "paaSServiceId", "triggerStatus", "hostId" })
public class TriggerStatusUpdate implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("paaSServiceId")
  private Long paaSServiceId;
  @JsonProperty("triggerStatus")
  private TriggerStatus triggerStatus;
  @JsonProperty("hostId")
  private String hostId;

  @JsonProperty("paaSServiceId")
  public Long getId() {
    return paaSServiceId;
  }

  @JsonProperty("paaSServiceId")
  public void setId(Long id) {
    this.paaSServiceId = id;
  }

  public TriggerStatusUpdate withId(Long id) {
    this.paaSServiceId = id;
    return this;
  }

  @JsonProperty("triggerStatus")
  public TriggerStatus getStatus() {
    return this.triggerStatus;
  }

  @JsonProperty("triggerStatus")
  public void setStatus(TriggerStatus status) {
    this.triggerStatus = status;
  }

  public TriggerStatusUpdate withStatus(TriggerStatus status) {
    this.triggerStatus = status;
    return this;
  }

  @JsonProperty("hostId")
  public String getHostId() {
    return this.hostId;
  }

  @JsonProperty("hostId")
  public void setHostId(String hostId) {
    this.hostId = hostId;
  }

  public TriggerStatusUpdate withHostId(String hostId) {
    this.hostId = hostId;
    return this;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(paaSServiceId).append(triggerStatus).append(hostId)
        .toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof TriggerStatusUpdate) == false) {
      return false;
    }
    TriggerStatusUpdate rhs = ((TriggerStatusUpdate) other);
    return new EqualsBuilder().append(paaSServiceId, rhs.paaSServiceId)
        .append(triggerStatus, rhs.triggerStatus).append(hostId, rhs.hostId).isEquals();
  }

}