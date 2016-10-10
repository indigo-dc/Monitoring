package com.indigo.zabbix.utils;

import feign.Response;

import io.github.hengyunabc.zabbix.sender.DataObject;
import io.github.hengyunabc.zabbix.sender.SenderResult;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jose on 22/09/16.
 */
public class ZabbixClient {

  public static final Integer ZABBIX_DEFAULT_PORT = 10051;

  private String zabbixCategory;
  private String zabbixGroup;

  private static final Log logger = LogFactory.getLog(ZabbixClient.class);

  private ZabbixWrapperClient wrapperClient;
  private ZabbixSender sender;

  /**
   * Default constructor that will read the information from the configuration properties.
   */
  public ZabbixClient() {
    this(PropertiesManager.getProperty(ProbesTags.ZABBIX_WRAPPER_ENDPOINT),
        PropertiesManager.getProperty(ProbesTags.ZABBIX_CATEGORY),
        PropertiesManager.getProperty(ProbesTags.ZABBIX_GROUP),
        PropertiesManager.getProperty(ProbesTags.ZABBIX_HOST),
        new Integer(PropertiesManager.getProperty(ProbesTags.ZABBIX_PORT,
            ZABBIX_DEFAULT_PORT.toString())));
  }

  /**
   * Default constructor.
   */
  public ZabbixClient(String wrapperEndpoint, String category, String group,
                      String zabbixHost, Integer zabbixPort) {

    wrapperClient = ProbeClientFactory.getZabbixWrapperClient(wrapperEndpoint);

    Integer port = (zabbixPort != null) ? zabbixPort : ZABBIX_DEFAULT_PORT;

    sender = new ZabbixSender(zabbixHost, port);

    zabbixCategory = category;
    zabbixGroup = group;
  }

  /**
   * Ensure that a host is registered and if not, register it.
   *
   * @param host The host name.
   * @return The registration status.
   */
  public boolean ensureRegistration(String host) {
    Response hostInfo = wrapperClient.getHostInfo(host, zabbixGroup);
    if (hostInfo.status() < 300 && hostInfo.status() >= 200) {
      return true;
    } else {
      Response registrationResult = wrapperClient.registerHost(host, zabbixGroup,
          new ZabbixHost(host, zabbixCategory, zabbixGroup));
      if (registrationResult.status() < 300 && registrationResult.status() >= 200) {
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * Sends a series of metrics associated to a host.
   *
   * @param metrics   The metrics to send.
   */
  public void sendMetrics(ZabbixMetrics metrics) {
    if (ensureRegistration(metrics.getHostName())) {
      long timeSecs = metrics.getTimestamp() / 1000;
      String zabbixHost = PropertiesManager.getProperty(ProbesTags.ZABBIX_HOST);
      if (zabbixHost != null) {

        List<DataObject> toSend = metrics.getMetrics().entrySet().stream().map(entry -> {

          DataObject dataObject = new DataObject();
          dataObject.setHost(metrics.getHostName());
          dataObject.setKey(entry.getKey());
          dataObject.setValue(entry.getValue());
          dataObject.setClock(timeSecs);

          return dataObject;
        })
            .collect(Collectors.toList());

        try {
          SenderResult sendResult = sender.send(toSend);
          if (!sendResult.success()) {
            logger.error("Error sending values: "
                + "\nTotal: " + sendResult.getTotal()
                + "\nProcessed: " + sendResult.getProcessed()
                + "\nFailed: " + sendResult.getFailed());
          }
        } catch (IOException e) {
          logger.error("Error sending values", e);
        }
      }
    }
  }

}
