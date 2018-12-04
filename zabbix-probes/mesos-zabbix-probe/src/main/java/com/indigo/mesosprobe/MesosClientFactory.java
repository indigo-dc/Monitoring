package com.indigo.mesosprobe;

import com.indigo.mesosprobe.mesos.MesosFeignClient;
import com.indigo.zabbix.utils.ProbeClientFactory;
import feign.Logger;

/** Created by jose on 4/10/16. */
public class MesosClientFactory extends ProbeClientFactory {

  public static MesosFeignClient getMesosClient(String endpoint) {
    return getClient(MesosFeignClient.class, endpoint, true, Logger.Level.FULL);
  }
}
