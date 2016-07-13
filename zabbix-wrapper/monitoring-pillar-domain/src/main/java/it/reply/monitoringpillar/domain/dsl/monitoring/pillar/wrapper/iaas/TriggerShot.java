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
@JsonPropertyOrder({ "triggerId", "description", "time", "expression" })
public class TriggerShot implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("triggerId")
  private String triggerId;
  @JsonProperty("description")
  private String description;
  @JsonProperty("time")
  private String time;
  @JsonProperty("expression")
  private String expression;

  @JsonProperty("triggerId")
  public String getTriggerId() {
    return triggerId;
  }

  @JsonProperty("triggerId")
  public void setTriggerId(String triggerId) {
    this.triggerId = triggerId;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("time")
  public String getTime() {
    return time;
  }

  @JsonProperty("time")
  public void setTime(String time) {
    this.time = time;
  }

  @JsonProperty("expression")
  public String getExpression() {
    return expression;
  }

  @JsonProperty("expression")
  public void setExpression(String expression) {
    this.expression = expression;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(triggerId).append(description).append(time)
        .append(expression).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof TriggerShot) == false) {
      return false;
    }
    TriggerShot rhs = ((TriggerShot) other);
    return new EqualsBuilder().append(triggerId, rhs.triggerId).append(description, rhs.description)
        .append(time, rhs.time).append(expression, rhs.expression).isEquals();
  }
}