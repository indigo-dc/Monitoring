package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper;

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
@JsonPropertyOrder({ "description", "key", "clock", "descriptionEvent" })
public class MonitPillarEventDescriptor implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("description")
  private String description;
  @JsonProperty("key")
  private String key;
  @JsonProperty("clock")
  private String clock;
  @JsonProperty("descriptionEvent")
  private DescriptionEvent descriptionEvent;

  @JsonProperty("descriptionEvent")
  public DescriptionEvent getDescriptionEvent() {
    return descriptionEvent;
  }

  @JsonProperty("descriptionEvent")
  public void setDescription(DescriptionEvent descriptionEvent) {
    this.descriptionEvent = descriptionEvent;
  }

  @JsonProperty("key")
  public String getKey() {
    return key;
  }

  @JsonProperty("key")
  public void setKey(String key) {
    this.key = key;
  }

  @JsonProperty("clock")
  public String getClock() {
    return clock;
  }

  @JsonProperty("clock")
  public void setClock(String clock) {
    this.clock = clock;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(description).append(key).append(clock).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof MonitPillarEventDescriptor) == false) {
      return false;
    }
    MonitPillarEventDescriptor rhs = ((MonitPillarEventDescriptor) other);
    return new EqualsBuilder().append(description, rhs.description).append(key, rhs.key)
        .append(clock, rhs.clock).isEquals();
  }

}