package com.indigo.zabbix.utils;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

/**
 * Created by jose on 12/08/16.
 */
public class ProbeClientFactory {

  /**
   * Creates a Feign client from an interface with GSON as encoder and decoder.
   * 
   * @param clientClass The Feign client class to instantiate.
   * @param baseUrl The base URL to access the service.
   * @param <T> The class type.
   * @return An initialized client of the desired class to access the service at the provided URL.
   */
  public static <T> T getClient(Class<T> clientClass, String baseUrl) {
    return Feign.builder().decoder(new GsonDecoder()).encoder(new GsonEncoder()).target(clientClass,
        baseUrl);
  }

  /**
   * Utility method to obtain the Zabbix Wrapper client.
   * 
   * @param endpoint The URL to the Zabbix Wrapper service.
   * @return A configured Feign client for the Zabbix Wrapper service.
   */
  public static ZabbixWrapperClient getZabbixWrapperClient(String endpoint) {
    return getClient(ZabbixWrapperClient.class, endpoint);
  }
}
