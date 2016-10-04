package com.indigo.mesosprobe.mesos;

import com.google.gson.JsonObject;

import com.indigo.mesosprobe.mesos.beans.MesosMasterInfoBean;

import feign.RequestLine;

/**
 * Created by jose on 12/08/16.
 */
public interface MesosClient {

  @RequestLine("GET /metrics/snapshot")
  JsonObject getMetrics();

  @RequestLine("GET /state")
  MesosMasterInfoBean getInfo();

}
