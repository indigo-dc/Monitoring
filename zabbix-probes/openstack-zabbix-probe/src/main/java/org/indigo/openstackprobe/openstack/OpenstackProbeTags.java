package org.indigo.openstackprobe.openstack;

/**
 * 
 * Interface for exposing the properties.
 *
 */
public interface OpenstackProbeTags {

//  public static final String KEYSTONE_LOCATION = "openstack.keystoneurl";
//  public static final String KEYSTONE_PORT = "openstack.keystoneport";
//  public static final String COMPUTE_LOCATION = "openstack.novaurl";
//  public static final String COMPUTE_PORT = "openstack.novaport";
//  public static final String OPENSTACK_USER = "openstack.user";
//  public static final String OPENSTACK_PASSWORD = "openstack.password";
  public static final String JAVA_KEYSTORE = "java.keystore";
  public static final String ZABBIX_IP = "zabbix.ip";
//  public static final String ZABBIX_SENDER = "zabbix.sender.location";
  public static final String ZABBIX_WRAPPER = "zabbix.wrapper.location";
  public static final String CMDB_URL = "cmdb.location";
//  public static final String TENANT_NAME = "openstack.tenant.name";
  
  public static final String INSTANCE_NAME = "vMOpenstackZabbixProbe_";
  public static final String WAIT_FOR_CREATION = "wait.real.vm.creation";
  public static final String IAM_AUTHENTICATION = "is-iam-authenticated";
  
  public static final  String IAM_LOCATION = "iam.location";
  public static final  String IAM_USERNAME = "iam.username";
  public static final  String IAM_PASSWORD = "iam.password";
  
  public static final  String PROVIDERS_EXCEPTIONS = "providers.exceptions"; 
  
  
  public static String CONFIG_FILE = "openstackprobe.properties";
  String OPENSTACK_METRIC = "os.metric"; 
  
}
