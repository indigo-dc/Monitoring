package com.indigo.zabbix.utils;

/**
 * Created by jose on 4/10/16.
 */
public interface ProbesTags {

  String ZABBIX_WRAPPER_ENDPOINT = "zabbix.wrapper.location";
  String ZABBIX_WRAPPER_LOG_LEVEL = "zabbix.wrapper.log.level";

  String ZABBIX_HOST = "zabbix.ip";
  String ZABBIX_PORT = "zabbix.port";

  String HOSTS_CATEGORY = "hosts.category";
  String HOSTS_GROUP = "hosts.group";

  String OPENSTACK_PROJECT = "openstack.project";

  String IAM_LOCATION = "iam.location";
  String IAM_USERNAME = "iam.username";
  String IAM_PASSWORD = "iam.password";
  String IAM_CLIENTID = "iam.clientid";
  String IAM_CLIENTSECRET = "iam.clientsecret";
  String CMDB_URL = "cmdb.location";
  String IAM_PROTOCOL = "iam.protocol";
  String IDENTITY_PROVIDER = "iam.identity.provider";

  String LOG_CONFIG = "log.config";

}

