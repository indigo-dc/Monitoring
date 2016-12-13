package com.indigo.zabbix.utils;

import io.github.hengyunabc.zabbix.sender.SenderResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Created by jose on 11/21/16.
 */
public abstract class CollectorThread<T extends MetricsCollector> {

  private static final Log logger = LogFactory.getLog(CollectorThread.class);

  protected ZabbixClient client;

  protected String category;
  protected String group;
  protected String template;

  /**
   * Constructor used for testing.
   * @param client client zabbix object.
   */
  protected CollectorThread(ZabbixClient client) {
    this.client = client;
  }

  protected CollectorThread(String category, String group, String template) {
    this.category = category;
    this.group = group;
    this.template = template;
  }

  protected void loadConfiguration(String propertiesFile) throws IOException {
    PropertiesManager.loadProperties(propertiesFile);
  }

  protected SenderResult run(String propertiesFile) {

    try {
      loadConfiguration(propertiesFile);

      if (this.client == null) {
        this.client = new ZabbixClient(category, group, template);
      }

      ZabbixMetrics metrics = createCollector().getMetrics();

      if (metrics != null) {
        return client.sendMetrics(metrics);
      }

    } catch (IOException e) {
      logger.error("Error reading configuration file",e);
    }


    return null;
  }

  protected abstract T createCollector();


}
