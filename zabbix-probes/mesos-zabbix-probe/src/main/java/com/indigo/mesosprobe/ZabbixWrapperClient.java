package com.indigo.mesosprobe;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

/**
 * Created by jose on 21/09/16.
 */
public interface ZabbixWrapperClient {

  static String ZABBIX_BASE_PATH =
      "/monitoring/adapters/zabbix/zones/indigo/types/infrastructure/groups";

  @RequestLine("POST " + ZABBIX_BASE_PATH + "/Mesos/hosts/{hostName}")
  @Headers("Content-Type: application/json")
  Response registerHost(@Param("hostName") String hostName, ZabbixHost content);

  @RequestLine("GET " + ZABBIX_BASE_PATH + "/Mesos/hosts/{hostName}")
  @Headers("Content-Type: application/json")
  Response getHostInfo(@Param("hostName") String hostName);

}
