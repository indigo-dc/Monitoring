package com.indigo.mesosprobe;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

/**
 * Created by jose on 12/08/16.
 */
public class ProbeClient {

  private static <T> T getClient(Class<T> clientClass, String baseUrl) {
    return Feign.builder()
      .decoder(new GsonDecoder())
      .encoder(new GsonEncoder())
      .target(clientClass,baseUrl);
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
