package com.indigo.zabbix.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class is focused on retrieving configuration parameters, serving them to other classes
 * which may need them in an easy way. It loads the configuration set in the occiprobe.properties
 * file.
 * @author ATOS
 *
 */
public class PropertiesManagerTest {
  public static final String KEYSTONE_LOCATION = "openstack.keystoneurl";
  public static final String KEYSTONE_PORT = "openstack.keystoneport";
  public static final String NOVA_LOCATION = "openstack.novaurl";
  public static final String NOVA_PORT = "openstack.novaport";
  public static final String OPENSTACK_USER = "openstack.user";
  public static final String OPENSTACK_PASSWORD = "openstack.password";
  public static final String JAVA_KEYSTORE = "java.keystore";
  public static final String ZABBIX_IP = "zabbix.ip";
  public static final String ZABBIX_SENDER = "zabbix.sender.location";
  public static final String ZABBIX_WRAPPER = "zabbix.wrapper.location";
  public static final String CMDB_URL = "cmdb.location";
  
  private HashMap<String, String> propertiesList;
  
  /**
   * This is the constructor of the class. It loads the properties file and it
   * retrieves all the properties available in the file, setting the values of the
   * configuration variables.
   */
  public PropertiesManagerTest(String propertyFile) {
    propertiesList = new HashMap<String, String>();
    readProperties(propertyFile);
  }  
  
  public void readProperties(String propertyFile) {
    Properties prop = new Properties();
         
    // Get class loader, for avoiding problems with Tomcat (loading local files)
    ClassLoader loader = PropertiesManager.class.getClassLoader();
    if (loader == null) {
      loader = ClassLoader.getSystemClassLoader();
    }
    String fileName = propertyFile;//"openstackprobe.properties"; 
    
    try {
      // We want to load file located in /resources      
      String location = "";
      String opSystem = System.getProperty("os.name").toLowerCase();
      if (opSystem.indexOf("win") >= 0) {
        location = "C://zabbixconfig//";
      } else {
        location = "/etc/zabbix/";
      }
      InputStream is = new FileInputStream(location + fileName);
      prop.load(is);
      is.close();
    } catch (IOException e) {
      System.out.println(e.toString());
      System.out.println("Using the default config file...");

      try {
        // We want to load file located in WEB-INF/classes/
        InputStream is = loader.getResourceAsStream(fileName);
        prop.load(is);
      } catch (Exception ex) {
        System.out.println(ex.toString());
        return;
      }
    }
    
    //Put all the properties into the map
    for (Enumeration e = prop.keys(); e.hasMoreElements() ; ) {
      // Get the property
      String obj = (String) e.nextElement();
      propertiesList.put(obj, prop.getProperty(obj));           
    }
  }
  
  /**
   * It provides the current value of the configuration property requested.
   * @param propertyName An String with a valid property name.
   * @return A String with the value loaded from the configuration file.
   */
  public String getProperty(String propertyName) {
    if (propertiesList.isEmpty()) {
      return null;
    }
    return propertiesList.get(propertyName);
  }
  
  /**
   * Typical main method for testing.
   * @param args Typical args.
   */
  public static void main(String[] args) {
    PropertiesManagerTest myProp = new PropertiesManagerTest("openstackprobe.properties");
    
    System.out.println("NOVA URL: " + myProp.getProperty(NOVA_LOCATION));
    System.out.println("NOVA URL: " + myProp.getProperty(NOVA_LOCATION));
  }
}