package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper;

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
@JsonPropertyOrder({ "events" })
public class MonitPillarEventResponse implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("events")
  private List<MonitPillarEventDescriptor> events = new ArrayList<MonitPillarEventDescriptor>();

  @JsonProperty("events")
  public List<MonitPillarEventDescriptor> getEvents() {
    return events;
  }

  @JsonProperty("events")
  public void setEvents(List<MonitPillarEventDescriptor> events) {
    this.events = events;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(events).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof MonitPillarEventResponse) == false) {
      return false;
    }
    MonitPillarEventResponse rhs = ((MonitPillarEventResponse) other);
    return new EqualsBuilder().append(events, rhs.events).isEquals();
  }

}