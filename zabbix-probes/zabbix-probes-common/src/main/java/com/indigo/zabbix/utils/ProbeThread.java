package com.indigo.zabbix.utils;

import io.github.hengyunabc.zabbix.sender.SenderResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by jose on 11/21/16. */
public abstract class ProbeThread<T extends MetricsCollector> {

  private static final Log logger = LogFactory.getLog(ProbeThread.class);

  protected ZabbixClient client;

  protected String category;
  protected String group;
  protected String template;

  /**
   * Constructor used for testing.
   *
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

  protected void loadConfiguration(String propertiesFile, String[] args) throws IOException {
    PropertiesManager.loadProperties(propertiesFile, args);
  }

  protected Map<String, SenderResult> run(String propertiesFile, String[] args) {

    Map<String, SenderResult> result = new HashMap<>();
    try {
      if (propertiesFile != null) {
        loadConfiguration(propertiesFile, args);
      }

      String category = PropertiesManager.getProperty(ProbesTags.HOSTS_CATEGORY, this.category);
      String group = PropertiesManager.getProperty(ProbesTags.HOSTS_GROUP, this.group);

      if (this.client == null) {
        this.client = new ZabbixClient(category, group, template);
      }

      for (T collector : createCollectors()) {
        try {
          ZabbixMetrics metrics = collector.getMetrics();

          if (metrics != null) {
            result.put(collector.getHostName(), client.sendMetrics(metrics));
          }
        } catch (Throwable e) {
          logger.error("Failed to register metrics for host " + collector.getHostName(), e);
          logger.error("Continuing with next provider");
        }
      }

    } catch (IOException e) {
      logger.error("Error reading configuration file", e);
    }

    return result;
  }

  protected abstract List<T> createCollectors();
}
