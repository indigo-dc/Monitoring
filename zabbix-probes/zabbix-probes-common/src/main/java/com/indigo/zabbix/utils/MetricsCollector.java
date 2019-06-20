package com.indigo.zabbix.utils;

import com.indigo.zabbix.utils.beans.DocDataType;

/**
 * Created by jose on 4/10/16.
 */
public interface MetricsCollector {

  ZabbixMetrics getMetrics();

  String getHostName();

  String getGroup();

  DocDataType.ServiceType getServiceType();

}
