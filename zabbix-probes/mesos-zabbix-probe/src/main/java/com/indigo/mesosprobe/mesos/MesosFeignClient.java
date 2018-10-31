package com.indigo.mesosprobe.mesos;

import com.indigo.mesosprobe.mesos.beans.GetMetricsResponse;
import com.indigo.mesosprobe.mesos.beans.MesosMasterInfoBean;
import feign.Headers;
import feign.RequestLine;

/** Created by jose on 12/08/16. */
public interface MesosFeignClient {

  @RequestLine("POST /api/v1")
  @Headers("Content-type: application/json")
  MesosMasterInfoBean getMaster(MesosMasterInfoBean request);

  @RequestLine("POST /api/v1")
  @Headers("Content-type: application/json")
  GetMetricsResponse getMetrics(GetMetricsResponse request);
}
