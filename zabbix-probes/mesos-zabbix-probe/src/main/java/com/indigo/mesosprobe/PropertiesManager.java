package com.indigo.mesosprobe;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Created by jose on 16/08/16.
 */
public class PropertiesManager {

  private static PropertiesConfiguration props = new PropertiesConfiguration();

  /**
   * Loads the configuration file from the default location.
   * @throws IOException Exception in case of fail.
   */
  public static void loadProperties() throws IOException {

    String location = "";
    String opSystem = System.getProperty("os.name").toLowerCase();
    if (opSystem.indexOf("win") >= 0) {
      location = "C://zabbixconfig//";
    } else {
      location = "/etc/zabbix/";
    }

    File configFile = new File(location + "/" + MesosProbeTags.CONFIG_FILE);

    loadProperties(new FileReader(configFile));

  }

  /**
   * Load a configuration file into properties.
   * @param confFile Configuration file reader.
   * @throws IOException Exception if some I/O error occurs.
   */
  public static void loadProperties(Reader confFile) throws IOException {
    try {
      props.read(confFile);
    } catch (ConfigurationException e) {
      throw new IOException("Error reading configuration file",e);
    }
  }

  public static String getProperty(String property) {
    return props.getString(property);
  }

  public static List<String> getListProperty(String property) {
    return props.getList(String .class, property);
  }

}
