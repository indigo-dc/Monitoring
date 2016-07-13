package it.reply.monitoringpillar.domain.dsl.monitoring;

import java.util.ArrayList;
import java.util.List;

public enum Adapter {

  ZABBIX("zabbix"), NAGIOS("nagios");

  private String adapter;

  private Adapter(String adapter) {
    this.adapter = adapter;
  }

  public String getAdapter() {
    return adapter;
  }

  public void setAdapter(String adapter) {
    this.adapter = adapter;
  }

  /**
   * Get Adapter by name by loopin into file.
   * 
   * @param name
   *          of adapter
   * @return Adapter object
   */
  public static Adapter findByName(String name) {
    for (Adapter a : values()) {
      if (a.getAdapter().equals(name)) {
        return a;
      }
    }
    throw new IllegalArgumentException(
        "Cannot find [" + name + "] for " + Adapter.class.getCanonicalName());
  }

  /**
   * Get all adapters.
   * 
   * @return list of adapters.
   */
  public static List<String> getAllAdapter() {
    List<String> adapters = new ArrayList<String>();
    for (Adapter a : values()) {
      adapters.add(a.getAdapter());
    }
    return adapters;
  }

}
