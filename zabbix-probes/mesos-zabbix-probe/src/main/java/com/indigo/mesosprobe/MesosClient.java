package com.indigo.mesosprobe;

import com.google.gson.JsonObject;

import feign.RequestLine;

/**
 * Created by jose on 12/08/16.
 */
public interface MesosClient {

  @RequestLine("GET /metrics/snapshot")
  JsonObject getMetrics();

}
