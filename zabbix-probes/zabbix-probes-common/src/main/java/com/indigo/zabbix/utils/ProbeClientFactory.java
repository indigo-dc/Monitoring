package com.indigo.zabbix.utils;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;

/** Created by jose on 12/08/16. */
public class ProbeClientFactory {

  /**
   * Returns a default Feign builder configured to use GSON and, depending on the parameters,
   * integrate with the IAM and log its activity.
   *
   * @param authenticate If true, it returns a client that will put the OpenID Connect headers on
   *     every request automatically.
   * @param logLevel Sets the log level for the client. For debugging purposes.
   * @return A builder with the above options configured.
   */
  public static Feign.Builder getDefaultBuilder(boolean authenticate, Logger.Level logLevel) {
    Feign.Builder result =
        Feign.builder()
            .decoder(new GsonDecoder())
            .encoder(new GsonEncoder())
            .logger(new Slf4jLogger())
            .logLevel(logLevel);
    if (authenticate) {
      result.client(new AuthenticatedFeignClient());
    }

    return result;
  }

  public static <T> T getClient(
      Class<T> clientClass, String baseUrl, boolean authenticate, Logger.Level logLevel) {
    return getDefaultBuilder(authenticate, logLevel).target(clientClass, baseUrl);
  }

  public static <T> T getClient(Class<T> clientClass, String baseUrl, boolean authenticate) {
    return getClient(clientClass, baseUrl, authenticate, Logger.Level.NONE);
  }

  /**
   * Creates a Feign client from an interface with GSON as encoder and decoder.
   *
   * @param clientClass The Feign client class to instantiate.
   * @param baseUrl The base URL to access the service.
   * @param <T> The class type.
   * @return An initialized client of the desired class to access the service at the provided URL.
   */
  public static <T> T getClient(Class<T> clientClass, String baseUrl) {
    return getClient(clientClass, baseUrl, false, Logger.Level.NONE);
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

  /**
   * Utility method to obtain the CMDB client.
   *
   * @param endpoint The URL to the CMDB service.
   * @return A configured Feign client for the CMDB service.
   */
  public static CmdbFeignClient getCmdbClient(String endpoint) {
    return getClient(CmdbFeignClient.class, endpoint);
  }
}
