package com.indigo.mesosprobe;

/**
 * Created by jose on 16/08/16.
 */
public interface MesosProbeTags {

  String MESOS_MASTER_ENDPOINT = "mesos.master.endpoint";
  String CHRONOS_ENDPOINT = "chronos.endpoint";
  String MARATHON_ENDPOINT = "marathon.endpoint";

  String ZABBIX_WRAPPER_ENDPOINT = "zabbix.wrapper.location";
  String ZABBIX_HOST = "zabbix.ip";
  String ZABBIX_PORT = "zabbix.port";

  String CHRONOS_USERNAME = "chronos.username";
  String CHRONOS_PASSWORD = "chronos.password";

  String MARATHON_USERNAME = "marathon.username";
  String MARATHON_PASSWORD = "marathon.password";

  String CONFIG_FILE = "mesosprobe.properties";
  String MESOS_METRIC = "mesos.metric";
}
