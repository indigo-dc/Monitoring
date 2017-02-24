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
  private String zabbixTemplate;

  private static final Log logger = LogFactory.getLog(ZabbixClient.class);

  private ZabbixWrapperClient wrapperClient;
  private ZabbixSender sender;
  
  /*
   * TODO
   * To remove...
   */
  private static final String CONFIG_FILE = "openstackprobe.properties";
  private static  PropertiesManagerTest propertiesManagerTest = new PropertiesManagerTest(CONFIG_FILE);

  /**
   * Constructor used for testing.
   * @param wrapperClient mock wrapper client.
   * @param sender mock wrapper sender.
   */
  public ZabbixClient(String category, String group, String template,
                      ZabbixWrapperClient wrapperClient, ZabbixSender sender) {
    this.zabbixCategory = category;
    this.zabbixGroup = group;
    this.zabbixTemplate = template;
    this.wrapperClient = wrapperClient;
    this.sender = sender;
  }

  /**
   * Default constructor that will read the information from the configuration properties.
   */
  /*TODO
   * To remove and take it back to the original situation...
   */
  public ZabbixClient(String category, String group, String template) {
    this(//PropertiesManager.getProperty(ProbesTags.ZABBIX_WRAPPER_ENDPOINT),
		propertiesManagerTest.getProperty(ProbesTags.ZABBIX_WRAPPER_ENDPOINT),
		category,
        group,
        template,
        //PropertiesManager.getProperty(ProbesTags.ZABBIX_HOST), new Integer(PropertiesManager.getProperty(ProbesTags.ZABBIX_PORT, ZABBIX_DEFAULT_PORT.toString())));
        propertiesManagerTest.getProperty(ProbesTags.ZABBIX_HOST), 10051);
  }

  /**
   * Default constructor.
   */
  public ZabbixClient(String wrapperEndpoint, String category, String group, String template,
                      String zabbixHost, Integer zabbixPort) {

    wrapperClient = ProbeClientFactory.getZabbixWrapperClient(wrapperEndpoint);

    Integer port = (zabbixPort != null) ? zabbixPort : ZABBIX_DEFAULT_PORT;

    sender = new ZabbixSender(zabbixHost, port);

    zabbixCategory = category;
    zabbixGroup = group;
    zabbixTemplate = template;
  }

  /**
   * Ensure that a host is registered and if not, register it.
   *
   * @param host The host name.
   * @return The registration status.
   */
  public boolean ensureRegistration(String host, boolean register) {
    Response hostInfo = wrapperClient.getHostInfo(host, zabbixGroup);
    if (hostInfo.status() < 300 && hostInfo.status() >= 200) {
      return true;
    } else {
      if (register) {
        Response registrationResult = wrapperClient.registerHost(host, zabbixGroup,
            new ZabbixHost(host, zabbixCategory, zabbixGroup, zabbixTemplate));
        if (registrationResult.status() < 300 && registrationResult.status() >= 200) {
          return ensureRegistration(host, false);
        } else {
          return false;
        }
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
  public SenderResult sendMetrics(ZabbixMetrics metrics) {
    if (ensureRegistration(metrics.getHostName(), true)) {
      long timeSecs = metrics.getTimestamp() / 1000;
      /*
       * TODO:
       * To decomment when working	
       */
      //String zabbixHost = PropertiesManager.getProperty(ProbesTags.ZABBIX_HOST);
      String zabbixHost = propertiesManagerTest.getProperty(ProbesTags.ZABBIX_HOST);
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
            /* Sometimes zabbix fails to process data the first time it's sent. Specially when the
             * host is first registered. The reason why? It's a mystery and given the huge amount
             * of data we have to debug the situation, let's just try to send it once again before
             * failing. */
            sendResult = sender.send(toSend);
            if (!sendResult.success()) {
              logger.error("Error sending values for host " + metrics.getHostName() + ": "
                  + "\nTotal: " + sendResult.getTotal()
                  + "\nProcessed: " + sendResult.getProcessed()
                  + "\nFailed: " + sendResult.getFailed());
            } else {
              logger.info("Successfully sent " + sendResult.getProcessed()
                  + " metrics for host " + metrics.getHostName());
            }
          } else {
            logger.info("Successfully sent " + sendResult.getProcessed()
                + " metrics for host " + metrics.getHostName());
          }
          return sendResult;
        } catch (IOException e) {
          logger.error("Error sending values for host " + metrics.getHostName(), e);
        }
      }
    }
    return null;
  }

}