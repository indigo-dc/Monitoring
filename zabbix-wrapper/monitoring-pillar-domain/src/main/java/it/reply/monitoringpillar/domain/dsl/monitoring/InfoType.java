package it.reply.monitoringpillar.domain.dsl.monitoring;

import java.util.ArrayList;
import java.util.List;

/**
 * This Enum represents the possible values of {type} in the Web Service Path.
 *
 */
public enum InfoType {

  INFRASTRUCTURE("infrastructure"), SERVICE("service"), WATCHER("watcher"), PAAS("paas"), ALL(
      "all");

  private String infoType;

  private InfoType(String infoType) {
    this.infoType = infoType;
  }

  public String getInfoType() {
    return this.infoType;
  }

  public String toString() {
    return this.infoType;
  }

  /**
   * Method for returning the list of zabbix server types.
   * 
   * @return list of zabbix server types
   */
  public static List<String> getAllType() {
    List<String> types = new ArrayList<String>();
    for (InfoType a : values()) {
      types.add(a.getInfoType());
    }
    return types;
  }

  /**
   * Method for looping in infotypte enum.
   * 
   * @param name
   *          of type
   * @return info type enum attribute
   * @throws IllegalArgumentException
   *           500 error
   */
  public static InfoType lookupFromName(String name) throws IllegalArgumentException {
    for (InfoType s : values()) {
      if (name.equalsIgnoreCase(s.getInfoType())) {
        return s;
      }
    }
    throw new IllegalArgumentException("Cannot find [" + name + "] into InfoType enum");
  }
}
