package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PillarZone implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("zones")
  private List<String> zones = new ArrayList<String>();

  @JsonProperty("zones")
  public List<String> getZones() {
    return zones;
  }

  @JsonProperty("zones")
  public void setZones(List<String> list) {
    this.zones = list;
  }

  public PillarZone withZones(List<String> zones) {
    this.zones = zones;
    return this;
  }

}
