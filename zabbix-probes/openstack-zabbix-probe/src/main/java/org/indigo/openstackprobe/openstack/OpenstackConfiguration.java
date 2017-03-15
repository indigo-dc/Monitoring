package org.indigo.openstackprobe.openstack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class OpenstackConfiguration.
 */
public class OpenstackConfiguration {

	/** The Constant log. */
	private static final Logger log = LogManager.getLogger(OpenstackConfiguration.class);
	public static final String OS_ZONE_PROPERTY_FILE = "oszones.yml";
	
	/** The openstack zones. */
	private OpenstackZones openstackZones;

	/**
	 * Instantiates a new openstack configuration.
	 */
	public OpenstackConfiguration() {
			try {
				log.info("Retrieving openstack properties per zone");
				openstackZones = readYaml(getConfigFile(OS_ZONE_PROPERTY_FILE));
			
			} catch (Exception ex) {
				throw new RuntimeException("Failed to read property file " + OS_ZONE_PROPERTY_FILE,
						ex);
			}
	}
	
	/**
	 * Read yaml.
	 *
	 * @param file the file
	 * @return the openstack zones
	 */
	public OpenstackZones readYaml(final File file) {
	    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind
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
	 * @param file the file
	 * @return the config file
	 */
	private File getConfigFile(String file) {
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
	 * Gets the openstack zones.
	 *
	 * @return the openstack zones
	 */
	public OpenstackZones getMonitoringZones() {
		return openstackZones;
	}
}
