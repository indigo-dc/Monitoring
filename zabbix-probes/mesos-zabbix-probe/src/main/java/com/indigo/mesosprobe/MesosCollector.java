package com.indigo.mesosprobe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.indigo.mesosprobe.mesos.MesosClient;
import com.indigo.mesosprobe.mesos.beans.MesosMasterInfoBean;
import com.indigo.zabbix.utils.MetricsCollector;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.ZabbixMetrics;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jose on 4/10/16.
 */
public class MesosCollector implements MetricsCollector {

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

  private String findLeader() {
    String masterEndpoint = PropertiesManager.getProperty(MesosProbeTags.MESOS_MASTER_ENDPOINT);
    if (masterEndpoint != null) {
      MesosClient client = MesosClientFactory.getMesosClient(masterEndpoint);
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

  @Override
  public ZabbixMetrics getMetrics() {
    String leader = findLeader();
    if (leader != null) {

      List<String> properties = PropertiesManager.getListProperty(
          MesosProbeTags.MESOS_METRIC);

      if (properties != null && !properties.isEmpty()) {
        MesosClient mesosClient = MesosClientFactory.getMesosClient("http://" + leader);

        MesosMasterInfoBean leaderInfo = mesosClient.getInfo();
        if (leaderInfo.getHostname() != null) {

          ZabbixMetrics result = new ZabbixMetrics();

          result.setHostName(leaderInfo.getHostname());

          JsonObject metrics = mesosClient.getMetrics();
          result.setMetrics(readMetrics(metrics, properties));

          result.setTimestamp(new Date().getTime());

          return result;
        }
      }
    }

    return null;
  }

}
