package com.indigo.zabbix.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Created by jose on 11/21/16.
 */
public abstract class CollectorThread<T extends MetricsCollector> {

  private static final Log logger = LogFactory.getLog(CollectorThread.class);

  protected String category;
  protected String group;
  protected String template;

  protected CollectorThread(String category, String group, String template) {
    this.category = category;
    this.group = group;
    this.template = template;
  }

  protected void run(String propertiesFile) {

    try {
      PropertiesManager.loadProperties(propertiesFile);


      ZabbixMetrics metrics = createCollector().getMetrics();

      if (metrics != null) {
        ZabbixClient client = new ZabbixClient(category, group, template);
        client.sendMetrics(metrics);
      }

    } catch (IOException e) {
      logger.error("Error reading configuration file",e);
    }



  }

  protected abstract T createCollector();


}
