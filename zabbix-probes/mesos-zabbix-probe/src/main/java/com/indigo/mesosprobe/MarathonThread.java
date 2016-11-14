package com.indigo.mesosprobe;


import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.ZabbixClient;
import com.indigo.zabbix.utils.ZabbixMetrics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;


public class MarathonThread {

  private static final Log logger = LogFactory.getLog(MarathonThread.class);

  public static void main(String[] args) {

    try {
      PropertiesManager.loadProperties(MesosProbeTags.CONFIG_FILE);

      MarathonCollector collector = new MarathonCollector();
      ZabbixMetrics metrics = collector.getMetrics();

      if (metrics != null) {
        ZabbixClient client = new ZabbixClient("IaaS", "Marathon");
        client.sendMetrics(metrics);
      }

    } catch (IOException e) {
      logger.error("Error reading configuration file",e);
    }



  }
}
