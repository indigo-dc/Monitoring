package com.indigo.mesosprobe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Created by jose on 16/08/16.
 */

public class MesosClientTest {

  @Before
  public void prepare() throws IOException {
    PropertiesManager.loadProperties(
      new InputStreamReader(ClassLoader.getSystemClassLoader()
        .getResourceAsStream(MesosProbeTags.CONFIG_FILE))
    );
  }

  @Test
  public void testClient() {
    List<String> endpoints = PropertiesManager
      .getListProperty(MesosProbeTags.MESOS_MASTER_ENDPOINT);

    List<String> metrics = PropertiesManager
      .getListProperty(MesosProbeTags.MESOS_METRIC);

    for (String endpoint : endpoints) {
      MesosClient client = ProbeClient.getMesosClient(endpoint);

      JsonObject result = client.getMetrics();
      assert result != null;
      assert !result.isJsonNull();

      for (String metric : metrics) {
        JsonElement element = result.get(metric);
        if (element.isJsonPrimitive()) {
          String value = element.getAsString();

          assert value != null;
          assert !value.isEmpty();

        }
      }
    }

  }

  @Test
  public void testChronosClient() {
    List<String> endpoints = PropertiesManager
      .getListProperty(MesosProbeTags.CHRONOS_ENDPOINT);

    for (String endpoint : endpoints) {
      ChronosClient client = ProbeClient.getChronosClient(endpoint);

      JsonObject jobList = client.listJobs();

      assert  jobList != null;
      assert  !jobList.isJsonNull();
    }
  }

  @Test
  public void testMarathonClient() {
    List<String> endpoints = PropertiesManager
      .getListProperty(MesosProbeTags.MARATHON_ENDPOINT);

    for (String endpoint : endpoints) {
      MarathonClient client = ProbeClient.getMarathonClient(endpoint);

      JsonObject status = client.getInfo();

      assert  status != null;
      assert  !status.isJsonNull();
    }
  }

  @Test
  public void testZabbixClient() {
    ZabbixClient client = ProbeClient.getZabbixClient(
      PropertiesManager.getProperty(MesosProbeTags.ZABBIX_WRAPPER_ENDPOINT)
    );

    Response response = client.isContainerRegistered("PruHost");
    assert response.getStatus() == 200;
  }

}
