package org.indigo.openstackprobe.openstack;

import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;


/**
 * @author Reply Santer. This class is defined for getting the credentials from oszone.yml file in
 *         case IAM is skipped.
 */
public class OpenstackConfiguration {

  /**
   * The Constant log.
   **/
  private static final Logger log = LogManager.getLogger(OpenstackConfiguration.class);
  public static final String OS_ZONE_PROPERTY_FILE = "oszones.yml";
  public static String zone;

  /**
   * The openstack zones.
   **/
  private OpenstackZones openstackZones;

  /**
   * Instantiates a new openstack configuration.
   */
  public OpenstackConfiguration() {
    try {
      log.info("Retrieving openstack properties per zone");
      openstackZones = readYaml(getConfigFile(OS_ZONE_PROPERTY_FILE));

    } catch (Exception ex) {
      throw new RuntimeException("Failed to read property file " + OS_ZONE_PROPERTY_FILE, ex);
    }
  }

  /**
   * Instantiates a new openstack configuration used for test only.
   */
  public OpenstackConfiguration(String testZoneFile) {
    try {
      log.info("Retrieving openstack properties per zone");
      openstackZones = readYaml(getConfigFile(testZoneFile));

    } catch (Exception ex) {
      throw new RuntimeException("Failed to read property file " + OS_ZONE_PROPERTY_FILE, ex);
    }
  }

  /**
   * Read yaml.
   * 
   * @param file the file
   * @return the openstack zones
   */
  public OpenstackZones readYaml(final File file) {
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    OpenstackZones zoneFile = new OpenstackZones();
    try {
      zoneFile = mapper.readValue(file, OpenstackZones.class);
    } catch (JSONException | JsonMappingException je) {
      log.debug("Unable to parse the file: " + je.getMessage());
    } catch (IOException ioe) {
      log.debug("Unable to get the file: " + ioe.getCause());
    }
    return zoneFile;
  }

  /**
   * Gets the config file.
   * 
   * @param file the filesS
   * @return the config file
   * @throws URISyntaxException uri
   */
  protected File getConfigFile(String file) throws URISyntaxException {
    String location = "";
    String opSystem = System.getProperty("os.name").toLowerCase();
    if (file.contains("test")) {
      URL url = getClass().getResource("/testoszones.yml");
      location = url.toURI().getPath();
    } else {
      if (opSystem.indexOf("win") >= 0) {
        location = "C://zabbixconfig//";
      } else {
        location = "/etc/zabbix/";
      }
      return new File(location + "/" + file);
    }
    return new File(location);
  }

  /**
   * Gets the openstack zones.
   * 
   * @return the openstack zones
   */
  public OpenstackZones getMonitoringZones() {
    return openstackZones;
  }
}
