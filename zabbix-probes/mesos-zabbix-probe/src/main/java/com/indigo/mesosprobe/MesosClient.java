package com.indigo.mesosprobe;

import com.google.gson.JsonObject;

import javax.ws.rs.Path;

import feign.RequestLine;

/**
 * Created by jose on 12/08/16.
 */
@Path("metrics")
public interface MesosClient {

  @RequestLine("GET /metrics/snapshot")
  JsonObject getMetrics();

}
