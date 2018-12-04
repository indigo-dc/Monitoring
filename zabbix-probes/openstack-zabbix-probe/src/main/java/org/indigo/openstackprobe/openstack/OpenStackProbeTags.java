package org.indigo.openstackprobe.openstack;

/** Interface for exposing the properties. */
public interface OpenStackProbeTags {

  String JAVA_KEYSTORE = "java.keystore";
  String ZABBIX_IP = "zabbix.ip";
  String ZABBIX_WRAPPER = "zabbix.wrapper.location";
  String CMDB_URL = "cmdb.location";

  String INSTANCE_NAME = "vMOpenstackZabbixProbe_";
  String WAIT_FOR_CREATION = "wait.real.vm.creation";
  String IAM_AUTHENTICATION = "is-iam-authenticated";

  String IAM_LOCATION = "iam.location";
  String IAM_USERNAME = "iam.username";
  String IAM_PASSWORD = "iam.password";

  String PROVIDERS_EXCEPTIONS = "providers.exceptions";

  String CONFIG_FILE = "openstackprobe.properties";
  String OPENSTACK_METRIC = "os.metric";
}
