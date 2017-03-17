package com.indigo.zabbix.utils;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.hengyunabc.zabbix.sender.SenderResult;

/**
 * Created by jose on 11/21/16.
 */
public abstract class ProbeThread<T extends MetricsCollector> {

  private static final Log logger = LogFactory.getLog(ProbeThread.class);

  protected ZabbixClient client;

  protected String category;
  protected String group;
  protected String template;

  /**
   * Constructor used for testing.
   * @param client client zabbix object.
   */
  protected ProbeThread(ZabbixClient client) {
    this.client = client;
  }

  protected ProbeThread(String category, String group, String template) {
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