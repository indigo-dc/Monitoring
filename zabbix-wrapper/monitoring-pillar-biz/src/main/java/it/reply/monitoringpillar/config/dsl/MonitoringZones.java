
package it.reply.monitoringpillar.config.dsl;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.reply.monitoringpillar.domain.exception.MonitoringException;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "zones" })
public class MonitoringZones {

  @JsonProperty("zones")
  private List<Zone> zones = new ArrayList<Zone>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("zones")
  public List<Zone> getZones() {
    return zones;
  }

  @JsonProperty("zones")
  public void setZones(List<Zone> zones) {
    this.zones = zones;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  /**
   * GET ZONE.
   * 
   */
  public Zone getZone(String name) {
    boolean zoneFound = false;
    for (Zone zone : getZones()) {
      if (zone.getName().equalsIgnoreCase(name) || zone.getId().toString().equalsIgnoreCase(name)) {
        zoneFound = true;
        return zone;
      }
    }
    if (!zoneFound) {
      throw new MonitoringException("Unable to find zone: " + name);
    }
    return null;
  }
}
