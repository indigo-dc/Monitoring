package com.indigo.zabbix.utils;

import com.google.gson.JsonElement;

import com.indigo.zabbix.utils.beans.CmdbResponse;
import com.indigo.zabbix.utils.beans.ProviderInfo;
import com.indigo.zabbix.utils.beans.ServiceInfo;

import feign.Param;
import feign.RequestLine;

/**
 * Created by jose on 8/02/17.
 */
public interface CmdbFeignClient {

  @RequestLine("GET /service/list?include_docs=true")
  CmdbResponse<ServiceInfo> services();

  @RequestLine("GET /provider/id/{providerId}/has_many/services?include_docs=true")
  JsonElement providerInfo(@Param("providerId") String providerId);

  @RequestLine("GET /provider/list")
  CmdbResponse<ProviderInfo> providerList();

  @RequestLine("GET /image/list")
  JsonElement providerImages();

}
