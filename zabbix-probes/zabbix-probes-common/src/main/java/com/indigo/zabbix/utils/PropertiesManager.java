package com.indigo.zabbix.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Created by jose on 16/08/16.
 */
public class PropertiesManager {

  private static PropertiesConfiguration props = new PropertiesConfiguration();

  private static final String DEFAULT_LOG_FILE = "probes-log.properties";

  private static File getConfigFile(String file) {
    String location = "";
    String opSystem = System.getProperty("os.name").toLowerCase();
    if (opSystem.indexOf("win") >= 0) {
      location = "C://zabbixconfig//";
    } else {
      location = "/etc/zabbix/";
    }

    return new File(location + "/" + file);
  }

  /**
   * Loads the configuration file from the default location.
   *
   * @throws IOException Exception in case of fail.
   */
  public static void loadProperties(String configFileName) throws IOException {
    File configFile = getConfigFile(configFileName);
    loadProperties(new FileReader(configFile));
  }

  /**
   * Load a configuration file into properties.
   *
   * @param confFile Configuration file reader.
   * @throws IOException Exception if some I/O error occurs.
   */
  public static void loadProperties(Reader confFile) throws IOException {
    try {
      props.read(confFile);
    } catch (ConfigurationException e) {
      throw new IOException("Error reading configuration file", e);
    }
  }

  /**
   * Get an individual property value.
   * 
   * @param property The property name.
   * @return The property value.
   */
  public static String getProperty(String property) {
    return props.getString(property);
  }

  /**
   * Get a property value providing a default in case it's not found.
   * 
   * @param property The property name.
   * @param defaultValue The property default value.
   * @return The property value.
   */
  public static String getProperty(String property, String defaultValue) {
    String value = getProperty(property);
    if (value != null) {
      return value;
    } else {
      return defaultValue;
    }
  }

  /**
   * Get a property list value.
   * 
   * @param property The property name.
   * @return The property value.
   */
  public static List<String> getListProperty(String property) {
    return props.getList(String.class, property);
  }
}
