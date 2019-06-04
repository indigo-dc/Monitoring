package com.indigo.zabbix.utils;

/**
 * Created by jose on 4/10/16.
 */
public interface MetricsCollector {

  ZabbixMetrics getMetrics();

  String getHostName();

  String getGroup();

}
