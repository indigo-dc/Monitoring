package it.reply.monitoringpillar.domain.dsl.monitoring;

import java.util.ArrayList;
import java.util.List;

public enum Zone {

  INFN_BA("INFN_Bari"), INFN_PG("INFN_Perugia");

  private String zone;

  private Zone(String zone) {
    this.zone = zone;
  }

  public String getZone() {
    return zone;
  }

  public void setZone(String zone) {
    this.zone = zone;
  }

  /**
   * The method returns the zone by looping in property file.
   * 
   * @param name
   *          of zone
   * 
   * @return the zone name
   */
  public static Zone findByName(String name) {
    for (Zone a : values()) {
      if (a.getZone().equals(name)) {
        return a;
      }
    }
    throw new IllegalArgumentException(
        "Cannot find [" + name + "] for " + Zone.class.getCanonicalName());
  }

  /**
   * The method returns the list of all zones.
   */
  public static List<String> getAllZone() {
    List<String> zones = new ArrayList<String>();
    for (Zone a : values()) {
      zones.add(a.getZone());
    }
    return zones;
  }

}
