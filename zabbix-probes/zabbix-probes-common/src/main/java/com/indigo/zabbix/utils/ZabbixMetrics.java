package com.indigo.zabbix.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jose on 4/10/16.
 */
public class ZabbixMetrics {

  private String hostName;
  private Map<String, String> metrics = new HashMap<>();
  private long timestamp = new Date().getTime();

  public ZabbixMetrics(){

  }

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
