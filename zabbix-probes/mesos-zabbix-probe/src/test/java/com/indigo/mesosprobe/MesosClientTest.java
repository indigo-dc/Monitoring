package com.indigo.mesosprobe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.indigo.mesosprobe.mesos.MesosClient;

import org.junit.Before;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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


  //@Test
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

  //@Test
  public void testChronosClient() {
    ChronosClient client = new ChronosClient();
    assert client.testChronos();
  }

  //@Test
  public void testMarathonClient() {
    MarathonClient client = new MarathonClient();
    assert client.testMarathon();
  }


}
