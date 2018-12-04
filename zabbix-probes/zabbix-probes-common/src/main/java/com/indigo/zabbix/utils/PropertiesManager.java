package com.indigo.zabbix.utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/** Created by jose on 16/08/16. */
public class PropertiesManager {

  private static final Log logger = LogFactory.getLog(PropertiesManager.class);

  private static PropertiesConfiguration props = new PropertiesConfiguration();

  private static final String DEFAULT_LOG_FILE = "probes-log.properties";

  private static File getConfigFile(String file, String[] args) {
    try {
      Options options =
          new Options()
              .addOption("l", "config-location", true, "Set the configuration file location");
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd = parser.parse(options, args);
      if (cmd.hasOption("l")) {
        return new File(cmd.getOptionValue("l") + "/" + file);
      }
    } catch (ParseException e) {
      logger.error(
          "Error parsing command line options: "
              + e.getMessage()
              + "\nUsing default configuration file location");
    }

    return getConfigFile(file);
  }

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
   * Loads the configuration file from a potential custom location passed as argument.
   *
   * @throws IOException Exception in case of fail.
   */
  public static void loadProperties(String configFileName, String[]args) throws IOException {
    File configFile = getConfigFile(configFileName, args);
    loadProperties(new FileReader(configFile));
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
