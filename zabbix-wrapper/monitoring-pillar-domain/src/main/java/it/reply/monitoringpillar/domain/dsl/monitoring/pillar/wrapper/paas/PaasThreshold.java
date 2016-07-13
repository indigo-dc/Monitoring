package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

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
@JsonPropertyOrder({ "thresholdName", "thresholdStatus", "thresholdExpression" })
public class PaasThreshold implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  public enum PaasThresholdStatus {
    OK, PROBLEM;
  }

  @JsonProperty("thresholdName")
  private String thresholdName;
  @JsonProperty("thresholdStatus")
  private PaasThresholdStatus thresholdStatus;
  @JsonProperty("thresholdExpression")
  private String thresholdExpression;

  @JsonProperty("thresholdExpression")
  public String getThresholdExpression() {
    return thresholdExpression;
  }

  @JsonProperty("thresholdExpression")
  public void setThresholdExpression(String thresholdExpression) {
    this.thresholdExpression = thresholdExpression;
  }

  @JsonProperty("thresholdStatus")
  public PaasThresholdStatus getThresholdStatus() {
    return thresholdStatus;
  }

  @JsonProperty("thresholdStatus")
  public void setThresholdStatus(PaasThresholdStatus thresholdStatus) {
    this.thresholdStatus = thresholdStatus;
  }

  @JsonProperty("thresholdName")
  public String getThresholdName() {
    return thresholdName;
  }

  @JsonProperty("thresholdName")
  public void setThresholdName(String thresholdName) {
    this.thresholdName = thresholdName;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(thresholdExpression).append(thresholdStatus)
        .append(thresholdName).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof PaasThreshold) == false) {
      return false;
    }
    PaasThreshold rhs = ((PaasThreshold) other);
    return new EqualsBuilder().append(thresholdExpression, rhs.thresholdExpression)
        .append(thresholdStatus, rhs.thresholdStatus).append(thresholdName, rhs.thresholdName)
        .isEquals();
  }

}