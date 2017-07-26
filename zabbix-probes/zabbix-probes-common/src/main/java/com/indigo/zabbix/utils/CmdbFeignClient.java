package com.indigo.zabbix.utils;

import com.google.gson.JsonElement;
import com.indigo.zabbix.utils.beans.CmdbResponse;
import com.indigo.zabbix.utils.beans.ServiceType;

import feign.Param;
import feign.RequestLine;

/**
 * Created by jose on 8/02/17.
 */
public interface CmdbFeignClient {

  @RequestLine("GET /service/list?include_docs=true")
  CmdbResponse<ServiceType> services();

  @RequestLine("GET /provider/id/{providerId}/has_many/services?include_docs=true")
  JsonElement providerInfo(@Param("providerId") String providerId);
  
  @RequestLine("GET /provider/list")
  JsonElement providerList();
  
  @RequestLine("GET /image/list")
  JsonElement providerImages();
  
  @RequestLine("GET /provider/id/{providerId}/has_many/services?include_docs=true")
  JsonElement servicesPerProvider(@Param("providerId") String providerId);
  
}