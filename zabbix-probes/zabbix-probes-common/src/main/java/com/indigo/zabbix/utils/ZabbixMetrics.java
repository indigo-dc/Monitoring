package com.indigo.zabbix.utils;

import com.indigo.zabbix.utils.beans.DocDataType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jose on 4/10/16.
 */
public class ZabbixMetrics {

  private String hostName;
  private DocDataType.ServiceType serviceType;
  private String hostGroup;
  private Map<String, String> metrics = new HashMap<>();
  private long timestamp = new Date().getTime();

  public ZabbixMetrics() {

  }

  /**
   * Constructor for quick building a zabbix metrics object.
   * 
   * @param hostName hostname of the measured host.
   * @param metrics metrics in a key value set.
   * @param timestamp timestamp in which the measurements were taken.
   */
  public ZabbixMetrics(String hostName, Map<String, String> metrics, long timestamp) {
    this.hostName = hostName;
    this.metrics = metrics;
    this.timestamp = timestamp;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public DocDataType.ServiceType getServiceType() {
    return serviceType;
  }

  public void setServiceType(DocDataType.ServiceType serviceType) {
    this.serviceType = serviceType;
  }

  public String getHostGroup() {
    return hostGroup;
  }

  public void setHostGroup(String hostGroup) {
    this.hostGroup = hostGroup;
  }

  public Map<String, String> getMetrics() {
    return metrics;
  }

  public void setMetrics(Map<String, String> metrics) {
    this.metrics = metrics;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
