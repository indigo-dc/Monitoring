package com.indigo.mesosprobe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.indigo.mesosprobe.mesos.MesosClient;
import com.indigo.mesosprobe.mesos.beans.MesosMasterInfoBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jose on 22/09/16.
 */
public class ProbeThread {

  private static final Log logger = LogFactory.getLog(ProbeThread.class);

  private String findLeader() {
    String masterEndpoint = PropertiesManager.getProperty(MesosProbeTags.MESOS_MASTER_ENDPOINT);
    if (masterEndpoint != null) {
      MesosClient client = ProbeClient.getMesosClient(masterEndpoint);
      MesosMasterInfoBean redirect = client.getInfo();
      String leader = redirect.getLeader();
      if (leader != null) {
        String[] leaderInfo = leader.split("@");
        if (leaderInfo.length == 2) {
          return leaderInfo[1];
        }
      }
    }
    return null;
  }

  private MesosClient mesosClient;
  private ZabbixClient zabbixClient = new ZabbixClient();

  private void startMonitoring() {

    String leader = findLeader();
    if (leader != null) {
      List<String> properties = PropertiesManager.getListProperty(
          MesosProbeTags.MESOS_METRIC);

      if (properties != null && !properties.isEmpty()) {
        mesosClient = ProbeClient.getMesosClient("http://" + leader);

        MesosMasterInfoBean leaderInfo = mesosClient.getInfo();
        if (leaderInfo.getHostname() != null) {

          JsonObject metrics = mesosClient.getMetrics();
          Map<String, String> metricsValues = readMetrics(metrics, properties);

          zabbixClient.sendMetrics(leaderInfo.getHostname(), metricsValues, new Date().getTime());
        }


      }
    }

  }

  private Map<String, String> readMetrics(JsonObject metrics, List<String> properties) {
    Map<String, String> result = new HashMap<>();
    properties.forEach(property -> {
      JsonElement elem = metrics.get(property);
      if (elem != null && elem.isJsonPrimitive()) {
        result.put(property.replace("/", "."), elem.getAsString());
      }
    });
    return result;
  }

  /**
   * Start monitoring process.
   * @param args Arguments will be ignored.
   */
  public static void main(String[] args) {

    try {
      PropertiesManager.loadProperties();
      ProbeThread thread = new ProbeThread();
      thread.startMonitoring();
    } catch (IOException e) {
      logger.error("Error reading configuration file", e);
    }

  }

}
