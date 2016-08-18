package com.indigo.mesosprobe;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.zalando.jersey.gson.GsonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by jose on 12/08/16.
 */
public class ProbeClient {

  private static <T> T getClient(Class<T> clientClass, String baseUrl) {
    ClientConfig config = new ClientConfig();
    config.register(GsonFeature.class);
    Client client = ClientBuilder.newClient(config);
    return WebResourceFactory.newResource(clientClass,client.target(baseUrl));
  }

  public static MesosClient getMesosClient(String endpoint) {
    return getClient(MesosClient.class, endpoint);
  }

  public static ZabbixClient getZabbixClient(String endpoint) {
    return getClient(ZabbixClient.class, endpoint);
  }

  public  static ChronosClient getChronosClient(String endpoint) {
    return getClient(ChronosClient.class, endpoint);
  }

  public static MarathonClient getMarathonClient(String endpoint) {
    return getClient(MarathonClient.class, endpoint);
  }
}
