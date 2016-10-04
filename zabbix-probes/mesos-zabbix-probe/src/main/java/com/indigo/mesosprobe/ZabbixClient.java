package com.indigo.mesosprobe;

import feign.Response;

import io.github.hengyunabc.zabbix.sender.DataObject;
import io.github.hengyunabc.zabbix.sender.SenderResult;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jose on 22/09/16.
 */
public class ZabbixClient {

  private static final String ZABBIX_DEFAULT_PORT = "10051";

  private static final Log logger = LogFactory.getLog(ZabbixClient.class);

  private ZabbixWrapperClient wrapperClient;

  /**
   * Default constructor.
   */
  public ZabbixClient() {
    String wrapperBase = PropertiesManager.getProperty(MesosProbeTags.ZABBIX_WRAPPER_ENDPOINT);
    if (wrapperBase != null) {
      wrapperClient = ProbeClient.getZabbixWrapperClient(wrapperBase);
    }
  }

  /**
   * Ensure that a host is registered and if not, register it.
   *
   * @param host The host name.
   * @return The registration status.
   */
  public boolean ensureRegistration(String host) {
    Response hostInfo = wrapperClient.getHostInfo(host);
    if (hostInfo.status() < 300 && hostInfo.status() >= 200) {
      return true;
    } else {
      Response registrationResult = wrapperClient.registerHost(host, new ZabbixHost(host));
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
   * @param host      The host name.
   * @param metrics   The metrics to send.
   * @param timestamp The timestamp in which they were taken.
   */
  public void sendMetrics(String host, Map<String, String> metrics, long timestamp) {
    if (ensureRegistration(host)) {
      long timeSecs = timestamp / 1000;
      String zabbixHost = PropertiesManager.getProperty(MesosProbeTags.ZABBIX_HOST);
      if (zabbixHost != null) {

        String zabbixPort = PropertiesManager.getProperty(
            MesosProbeTags.ZABBIX_PORT, ZABBIX_DEFAULT_PORT);

        ZabbixSender sender = new ZabbixSender(zabbixHost, new Integer(zabbixPort));

        List<DataObject> toSend = metrics.entrySet().stream().map(entry -> {

          DataObject dataObject = new DataObject();
          dataObject.setHost(host);
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
