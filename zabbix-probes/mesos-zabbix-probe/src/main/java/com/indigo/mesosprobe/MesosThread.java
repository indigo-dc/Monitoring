package com.indigo.mesosprobe;

import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.ZabbixClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Created by jose on 22/09/16.
 */
public class MesosThread {

  private static final Log logger = LogFactory.getLog(MesosThread.class);

  private ZabbixClient zabbixMesosClient =
      new ZabbixClient("IaaS", "Mesos", "TemplateMesos");

  private void startMonitoring() {
    MesosCollector collector = new MesosCollector();
    zabbixMesosClient.sendMetrics(collector.getMetrics());
  }



  /**
   * Start monitoring process.
   * @param args Arguments will be ignored.
   */
  public static void main(String[] args) {

    try {
      PropertiesManager.loadProperties(MesosProbeTags.CONFIG_FILE);
      MesosThread thread = new MesosThread();
      thread.startMonitoring();
    } catch (IOException e) {
      logger.error("Error reading configuration file", e);
    }

  }

}
