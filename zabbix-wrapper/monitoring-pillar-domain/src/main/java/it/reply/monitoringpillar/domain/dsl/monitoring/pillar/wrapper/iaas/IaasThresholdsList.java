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
@JsonPropertyOrder({ "triggerExpression", "triggerKey", "triggerStatus", "triggerValue" })
public class IaasThresholdsList implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("triggerExpression")
  private String triggerExpression;
  @JsonProperty("triggerKey")
  private String triggerKey;
  @JsonProperty("triggerStatus")
  private String triggerStatus;
  @JsonProperty("triggerValue")
  private String triggerValue;

  @JsonProperty("triggerExpression")
  public String getTriggerExpression() {
    return triggerExpression;
  }

  @JsonProperty("triggerExpression")
  public void setTriggerExpression(String triggerExpression) {
    this.triggerExpression = triggerExpression;
  }

  @JsonProperty("triggerKey")
  public String getTriggerKey() {
    return triggerKey;
  }

  @JsonProperty("triggerKey")
  public void setTriggerKey(String triggerKey) {
    this.triggerKey = triggerKey;
  }

  @JsonProperty("triggerStatus")
  public String getTriggerStatus() {
    return triggerStatus;
  }

  @JsonProperty("triggerStatus")
  public void setTriggerStatus(String triggerStatus) {
    this.triggerStatus = triggerStatus;
  }

  @JsonProperty("triggerValue")
  public String getTriggerValue() {
    return triggerValue;
  }

  @JsonProperty("triggerValue")
  public void setTriggerValue(String triggerValue) {
    this.triggerValue = triggerValue;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(triggerExpression).append(triggerKey).append(triggerStatus)
        .append(triggerValue).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof IaasThresholdsList) == false) {
      return false;
    }
    IaasThresholdsList rhs = ((IaasThresholdsList) other);
    return new EqualsBuilder().append(triggerExpression, rhs.triggerExpression)
        .append(triggerKey, rhs.triggerKey).append(triggerStatus, rhs.triggerStatus)
        .append(triggerValue, rhs.triggerValue).isEquals();
  }
}