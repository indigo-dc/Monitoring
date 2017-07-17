package org.indigo.openstackprobe.openstack;

/**
 * 
 * Interface for exposing the properties.
 *
 */
public interface OpenstackProbeTags {

  public static final String JAVA_KEYSTORE = "java.keystore";
  public static final String ZABBIX_IP = "zabbix.ip";
  public static final String ZABBIX_WRAPPER = "zabbix.wrapper.location";
  public static final String CMDB_URL = "cmdb.location";  
  public static final String INSTANCE_NAME = "vMOpenstackZabbixProbe_";
  public static final String WAIT_FOR_CREATION = "wait.real.vm.creation";
  public static final String IAM_AUTHENTICATION = "is-iam-authenticated";
  
  public static String CONFIG_FILE = "openstackprobe.properties";
  public static String OPENSTACK_METRIC = "os.metric"; 
  
  public static final String IAM_LOCATION = "iam.location";
  public static final String IAM_USER = "iam.user";
  public static final String IAM_PASSWORD = "iam.password";
}
