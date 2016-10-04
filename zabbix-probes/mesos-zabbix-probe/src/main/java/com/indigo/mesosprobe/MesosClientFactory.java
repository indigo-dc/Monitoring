package com.indigo.mesosprobe;

import com.indigo.mesosprobe.mesos.MesosClient;
import com.indigo.zabbix.utils.ProbeClientFactory;

/**
 * Created by jose on 4/10/16.
 */
public class MesosClientFactory extends ProbeClientFactory {

  public static MesosClient getMesosClient(String endpoint) {
    return getClient(MesosClient.class, endpoint);
  }

}
