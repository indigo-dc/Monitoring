package com.indigo.zabbix.utils;

import com.indigo.zabbix.utils.beans.ZabbixWrapperGroupCreationRequest;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

/** Created by jose on 21/09/16. */
public interface ZabbixWrapperClient {

  String ZABBIX_BASE_PATH = "/monitoring/adapters/zabbix/zones/indigo/types/infrastructure/groups";

  @RequestLine("POST " + ZABBIX_BASE_PATH + "/{group}/hosts/{hostName}")
  @Headers("Content-Type: application/json")
  Response registerHost(
      @Param("hostName") String hostName, @Param("group") String group, ZabbixHost content);

  @RequestLine("GET " + ZABBIX_BASE_PATH + "/{group}/hosts/{hostName}")
  @Headers("Content-Type: application/json")
  Response getHostInfo(@Param("hostName") String hostName, @Param("group") String group);

  @RequestLine("GET " + ZABBIX_BASE_PATH + "/{group}")
  @Headers("Content-Type: application/json")
  Response getGroup(@Param("group") String group);

  @RequestLine("POST " + ZABBIX_BASE_PATH)
  @Headers("Content-Type: application/json")
  Response registerGroup(ZabbixWrapperGroupCreationRequest group);
}
