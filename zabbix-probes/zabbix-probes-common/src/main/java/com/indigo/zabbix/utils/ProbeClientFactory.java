package com.indigo.zabbix.utils;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

/**
 * Created by jose on 12/08/16.
 */
public class ProbeClientFactory {

  public static <T> T getClient(Class<T> clientClass, String baseUrl) {
    return Feign.builder()
        .decoder(new GsonDecoder())
        .encoder(new GsonEncoder())
        .target(clientClass, baseUrl);
  }

  public static ZabbixWrapperClient getZabbixWrapperClient(String endpoint) {
    return getClient(ZabbixWrapperClient.class, endpoint);
  }
}
