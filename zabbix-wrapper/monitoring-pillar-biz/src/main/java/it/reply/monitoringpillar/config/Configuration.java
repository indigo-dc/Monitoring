package it.reply.monitoringpillar.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import it.reply.monitoringpillar.adapter.zabbix.exception.IllegalArgumentZabbixException;
import it.reply.monitoringpillar.config.PathHelper.ResourceType;
import it.reply.monitoringpillar.config.dsl.Metric;
import it.reply.monitoringpillar.config.dsl.MonitoringConfigurations;
import it.reply.monitoringpillar.config.dsl.MonitoringMappings;
import it.reply.monitoringpillar.config.dsl.MonitoringZones;
import it.reply.monitoringpillar.config.dsl.PortsMacro;
import it.reply.monitoringpillar.config.dsl.Server;
import it.reply.monitoringpillar.config.dsl.Zone;
import it.reply.monitoringpillar.domain.dsl.monitoring.InfoType;
import it.reply.monitoringpillar.domain.dsl.monitoring.MonitoringConstant;
import it.reply.monitoringpillar.domain.exception.IllegalArgumentMonitoringException;
import it.reply.monitoringpillar.domain.exception.MonitoringException;
import it.reply.monitoringpillar.domain.exception.NotFoundMonitoringException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class Configuration {

  public static final String PROJECT_BUILD_VERSION = "build.version";
  public static final String PROJECT_BUILD_REVISION = "build.revision";
  public static final String PROJECT_BUILD_TIMESTAMP = "build.timestamp";

  private static final Logger LOG = LogManager.getLogger(Configuration.class);

  // General monitoring properties
  private MonitoringConfigurations monitoringConfigurations;

  // Properties read from {CURRENT_ENVIRONMENT}/services-endpoints.properties
  private Properties serviceProperties;
  private Properties versionProperties;

  private MonitoringZones monitoringZones;

  private MonitoringMappings monitoringMappings;

  public final String varResourceProfilesBasePath =
      PathHelper.getResourcesPath(ResourceType.CONFIG_ENV_VARIABLE_PROPERTIES_PROFILES);

  public MonitoringConfigurations getMonitoringConfigurations() {
    return monitoringConfigurations;
  }

  public MonitoringZones getMonitoringZones() {
    return monitoringZones;
  }

  public MonitoringMappings getMonitoringMappings() {
    return monitoringMappings;
  }

  @PostConstruct
  private void init() {
    try {
      LOG.info("Init configuration");

      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

      InputStream stream = getResourceAsStream(ConfigProperties.MONITORING_PROPERTY_FILE);
      if (stream == null) {
        throw new RuntimeException(
            "Property file " + ConfigProperties.MONITORING_PROPERTY_FILE + " not found.");
      }
      try {
        monitoringConfigurations = mapper.readValue(stream, MonitoringConfigurations.class);

        LOG.info("MonitoringConfiguration Properties loaded successfully!");
      } catch (Exception ex) {
        throw new RuntimeException(
            "Failed to read property file " + ConfigProperties.MONITORING_PROPERTY_FILE, ex);
      }

      stream = getResourceAsStream(ConfigProperties.ZONE_PROPERTY_FILE);
      if (stream == null) {
        throw new RuntimeException(
            "Property file " + ConfigProperties.ZONE_PROPERTY_FILE + " not found.");
      }
      try {
        monitoringZones = mapper.readValue(stream, MonitoringZones.class);
        LOG.info("MonitoringZone Properties loaded successfully!");
      } catch (Exception ex) {
        throw new RuntimeException(
            "Failed to read property file " + ConfigProperties.ZONE_PROPERTY_FILE, ex);
      }

      stream = getResourceAsStream(ConfigProperties.MAPPING_PROPERTY_FILE);
      if (stream == null) {
        throw new RuntimeException(
            "Property file " + ConfigProperties.MAPPING_PROPERTY_FILE + " not found.");
      }
      try {
        monitoringMappings = mapper.readValue(stream, MonitoringMappings.class);
        LOG.info("MonitoringMapping Properties loaded successfully!");
      } catch (Exception ex) {
        throw new RuntimeException(
            "Failed to read property file " + ConfigProperties.MAPPING_PROPERTY_FILE, ex);
      }

      loadVersionProperties();

    } catch (Exception ex) {
      throw new Error("CANNOT LOAD ENVIRONMENT CONFIGURATION !", ex);
    }
  }

  private void loadVersionProperties() throws IOException {
    try {
      Properties props = new Properties();

      InputStream in = getResourceAsStream("version.properties");
      props.load(in);
      versionProperties = new Properties(props);

      in.close();

      LOG.info("Version properties loaded successfully !!");

      if (LOG.isDebugEnabled()) {
        for (Entry<Object, Object> item : props.entrySet()) {
          LOG.debug("[Version] - K: " + item.getKey() + ", V: " + item.getValue());
        }
      }
    } catch (IOException ioe) {
      LOG.error("ERROR: Cannot load 'version.properties'.");
      throw new Error("Cannot load 'version.properties'");
    }
  }

  private InputStream getResourceAsStream(String path) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
  }

  /**
   * Verifies that the given zone exists in the property files.
   * 
   * @param zoneName
   *          name
   * @return true if the zone exists, false otherwise.
   */
  public boolean isZoneExisting(String zoneName) {
    return monitoringZones.getZone(zoneName) != null ? true : false;
  }

  /**
   * Get the list of zones where pillar operates into.
   * 
   * @return List of zones
   */
  public List<String> getZoneNames() {
    List<String> zoneNames = new ArrayList<>();
    for (Zone zone : monitoringZones.getZones()) {
      zoneNames.add(zone.getName());
    }
    if (zoneNames.isEmpty()) {
      throw new NotFoundMonitoringException("No zone found into property file");
    }
    return zoneNames;
  }

  public boolean isServerExisting(String zoneName, String serverType) {
    return monitoringZones.getZone(zoneName).getServer(serverType) != null ? true : false;
  }

  /**
   * Get the list of servers.
   * 
   * @param zoneName
   *          (name of the zone)
   * @return List of Servers
   */
  public List<String> getServerNames(String zoneName) {
    isZoneExisting(zoneName);
    List<String> serverNames = new ArrayList<>();
    String[] servernamesChecked = new String[serverNames.size()];
    servernamesChecked = serverNames.toArray(servernamesChecked);
    for (Zone zone : monitoringZones.getZones()) {
      for (Server server : zone.getServers()) {
        // check whether the server name exists or not
        if (!stringContainsItemFromList(server.getType(), servernamesChecked)) {
          serverNames.add(server.getType());
          servernamesChecked = serverNames.toArray(servernamesChecked);
        } else {
          ;
        }
      }
    }
    return serverNames;
  }

  /**
   * Get servers per zone.
   * 
   * @param zoneName
   *          name
   * @return map of servers per zone
   */
  public Map<String, List<String>> getServersPerZone(String zoneName) {
    List<String> serverNames = new ArrayList<>();
    Boolean zoneFound = false;
    Map<String, List<String>> serversPerZoneMap = new HashMap<String, List<String>>();
    for (Zone zone : monitoringZones.getZones()) {

      if (zoneName.equalsIgnoreCase(zone.getName())) {
        zoneFound = true;
        for (Server server : zone.getServers()) {
          serverNames.add(server.getType());
          serversPerZoneMap.put(zoneName, serverNames);
        }
      }

    }
    if (!zoneFound) {
      throw new NotFoundMonitoringException("Unable to find zone: " + zoneName);
    }
    return serversPerZoneMap;
  }

  /**
   * Get tool for string.
   * 
   * @param inputString
   *          tool
   * @param items
   *          metrics
   * @return condition
   */
  public static boolean stringContainsItemFromList(String inputString, String[] items) {
    for (int i = 0; i < items.length; i++) {
      if (inputString.contains(items[i])) {
        return true;
      }
    }
    return false;
  }

  public boolean isProxyArchitectureExisting(String zoneName) {
    return monitoringConfigurations.getOptions().isProxyArchitecture() != false ? true : false;
  }

  /**
   * Get Iaas ceilometer metric.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param metricName
   *          name
   * @return name of metric
   */
  public boolean isIaaSCeilometerMetric(String zone, String serverType, String metricName) {
    boolean metricFound = false;
    if (monitoringConfigurations.getOptions().isIaasMonitoring()) {
      List<String> metrics = getCeilometerMetrics();
      for (String metric : metrics) {
        if (metric.equalsIgnoreCase(metricName)) {
          metricFound = true;
        }
      }
      if (!metricFound) {
        throw new IllegalArgumentMonitoringException("Wrong Ceilometer metric name inserted: "
            + metricName + "or not existing into proerty file: " + metrics.toString());
      }
    }
    return metricFound;
  }

  /**
   * Get metrics from ceilometer.
   * 
   * @return list of metrics for vmaas
   */
  public List<String> getCeilometerMetrics() {
    List<String> metricNames = new ArrayList<>();
    for (Metric metric : monitoringMappings.getServiceMonitoring().getIaasCategoryService("IaaS")
        .getIaasAtomicService("IaaS").getMetrics()) {
      metricNames.add(metric.getName());
    }
    return metricNames;
  }

  /**
   * Get the existing port name.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param atomicService
   *          service
   * @param portName
   *          name
   * @return name of existing port
   */
  public String getExistingPaasPort(String zone, String serverType, String atomicService,
      String portName) {
    boolean metricFound = false;

    List<String> ports = getPortperAtomicService(atomicService, portName);
    for (PortsMacro port : monitoringMappings.getServiceMonitoring()
        .getPaasCategoryService(MonitoringConstant.ATOMIC_SERVICE_PAAS)
        .getPaasAtomicService(MonitoringConstant.ATOMIC_SERVICE_PAAS).getPortsMacros()) {
      if (port.getName().equalsIgnoreCase(portName)) {
        metricFound = true;
        return port.getName();
      }
    }
    if (!metricFound) {
      throw new IllegalArgumentMonitoringException("Wrong PORT MACRO  inserted: " + portName
          + "or not existing into proerty file: " + ports.toString());
    }
    return null;
  }

  /**
   * Get ports names.
   * 
   * @param atomicService
   *          name
   * @param portName
   *          name
   * @return list of ports Names
   */
  public List<String> getPortperAtomicService(String atomicService, String portName) {
    List<String> portNames = new ArrayList<>();
    boolean portNameFound = true;
    for (PortsMacro port : monitoringMappings.getServiceMonitoring()
        .getPaasCategoryService(atomicService).getPaasAtomicService(atomicService)
        .getPortsMacros()) {
      portNames.add(port.getName());
      for (String portname : portNames) {
        if (portname.equalsIgnoreCase(portName)) {
          portNameFound = true;
          break;
        }
      }
      if (!portNameFound) {
        throw new MonitoringException("Unable to find portName passed as parameter");
      }
    }
    return portNames;
  }

  public String getSvcEndpointProperty(String name) {
    return serviceProperties.getProperty(name);
  }

  /**
   * Get Iaas monitoring url.
   * 
   * @param zone
   *          name
   * @return name iof zone
   */
  public String getZabbixIaaSUrl(String zone) {
    return monitoringZones.getZone(zone).getServer(InfoType.INFRASTRUCTURE.getInfoType()).getUrl();
  }

  /**
   * Get metrics monitoring url.
   * 
   * @param zone
   *          name
   * @return url
   */
  public String getZabbixMetricsUrl(String zone) {
    return monitoringZones.getZone(zone).getServer(InfoType.SERVICE.getInfoType()).getUrl();
  }

  /**
   * Get wacther url.
   * 
   * @param zone
   *          name
   * @return url of watcher
   */
  public String getZabbixWatcherUrl(String zone) {
    return monitoringZones.getZone(zone).getServer(InfoType.WATCHER.getInfoType()).getUrl();
  }

  /**
   * Get url.
   * 
   * @param serverType
   *          type
   * @return url
   * @throws IllegalArgumentZabbixException
   *           excep
   */
  public String getZabbixServerUrl(InfoType serverType) throws IllegalArgumentZabbixException {
    try {
      return getZabbixServerUrl(serverType);
    } catch (IllegalArgumentException ie) {
      throw new IllegalArgumentZabbixException(ie.getMessage(), ie);
    }
  }

  /**
   * Get url.
   * 
   * @param zone
   *          name
   * @param server
   *          type
   * @return url
   * @throws IllegalArgumentZabbixException
   *           excep
   */
  public String getZabbixServerUrl(String zone, String server)
      throws IllegalArgumentZabbixException {

    if (server.equalsIgnoreCase(InfoType.INFRASTRUCTURE.getInfoType())) {
      return getZabbixIaaSUrl(zone);
    } else if (server.equalsIgnoreCase(InfoType.SERVICE.getInfoType())) {
      return getZabbixMetricsUrl(zone);
    } else if (server.equalsIgnoreCase(InfoType.PAAS.getInfoType())) {
      return getZabbixMetricsUrl(zone);
    } else if (server.equalsIgnoreCase(InfoType.WATCHER.getInfoType())) {
      return getZabbixWatcherUrl(zone);
    } else {
      throw new IllegalArgumentZabbixException("Cannot find " + server + " into [InfoType] Enum");
    }
  }

  /**
   * Returns the proxy name depending on the given zone and server type. <br/>
   * <br/>
   * In case the <b>Distributed Architecture</b> is enabled, the proxy name will be generated
   * replacing the placeholder in the nameTemplate prorperty with the given workgroup.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param workgroupId
   *          id
   * @return name of proxy
   */
  public String getProxyName(String zone, String serverType, String workgroupId) {
    String proxyName = null;
    if (getMonitoringZones().getZone(zone).getDistributedArchitecture() != null
        && getMonitoringZones().getZone(zone).getDistributedArchitecture() == true) {
      try {
        proxyName = String.format(
            getMonitoringZones().getZone(zone).getServer(serverType).getProxy().getNameTemplate(),
            workgroupId);
      } catch (Exception ex) {
        throw new IllegalArgumentException("Proxy template name generation failed: ", ex);
      }
    } else {
      proxyName = getMonitoringZones().getZone(zone).getServer(serverType).getProxy().getName();
    }
    if (proxyName == null || proxyName.length() == 0) {
      throw new IllegalArgumentException("Proxy name cannot be empty");
    }
    return proxyName;
  }

  /**
   * @see Configuration#getProxyName(String, ServerType, String).
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param workgroup
   *          id or name
   * @return name of proxy
   */
  public String getProxyNames(String zone, String serverType, String workgroup) {
    return getProxyName(zone, serverType, workgroup);
  }

  public static String getWorkgroupId(String hostgroup) {
    return hostgroup.replace(MonitoringConstant.WG_PREFIX, "");
  }

  public String getVersionProperty(String name) {
    return versionProperties.getProperty(name);
  }

  public static class ConfigProperties {

    /**
     * Monitoring property file.
     */
    public static final String MONITORING_PROPERTY_FILE = "monitoring.yml";
    public static final String ZONE_PROPERTY_FILE = "zones.yml";
    public static final String MAPPING_PROPERTY_FILE = "mapping.yml";
    public static final String ZONES = "zones";

    /**
     * Monitoring Environment property file.
     */
    public static final String MONITORING_ENVIRONMENT_FILE = "services-endpoints.properties";

    /**
     * Monitoring mapping Strings.
     */
    public static final String ENVIRONMENTS = "environments";
    public static final String FORCE_ENVIRONMENT = "force_environment";
    public static final String WEB_SERVICE_DEBUG = "web_service_debug";
    public static final String PROXY_CONF = "proxy_configuration";
    // public static final String DISTRIB_PROXIES_ARCH =
    // "distributed_proxies_architecture";
    public static final String DISTRIB_ARCH = "distributed_architecture_implemented";
    public static final String DEFAULT_ARCH = "default_architecture_implemented";

    // The current environment
    public static String CURRENT_ENVIRONMENT;

    /**
     * services-endpoints.properties Strings
     */
    public static final String LABEL = "label";
    public static final String ZABBIX_IAAS_URL = "server_zabbix_iaas_URL";
    public static final String ZABBIX_PROXY_IAAS_NAME = "proxy_zabbix_iaas_name";
    public static final String ZABBIX_METRICS_URL = "server_zabbix_metrics_URL";
    public static final String ZABBIX_PROXY_METRICS_NAME = "proxy_zabbix_metrics_name";
    public static final String ZABBIX_WATCHER_URL = "server_zabbix_watcher_URL";
    public static final String ZABBIX_PROXY_WATCHER_NAME = "proxy_zabbix_watcher_name";
    public static final String ZABBIX_RPC_VERSION = "server_zabbix_rpc_version";
    public static final String ZABBIX_USERNAME = "usernameZabbixServer";
    public static final String ZABBIX_PASSWORD = "pswdZabbixServer";

    /**
     * Ceilometer Metrics.
     */
    public static final String CEILOMETER_SCRIPT = "ceilometer_script";
    public static final String TEMPLATE_IAAS_CEILOMETER = "template_iaas_ceilometer";
    public static final String CEILOMETER_CPU_LOAD = "cpu_load";
    public static final String CEILOMETER_CPU_UPTIME = "cpu_uptime";
    public static final String CEILOMETER_VCPUs = "vcpus";
    public static final String CEILOMETER_RAM = "ram";
    public static final String CEILOMETER_DISK_EPHEM = "disk_ephemeral";
    public static final String CEILOMETER_DISK_SIZE = "disk_size";
    public static final String CEILOMETER_NETIN = "netIN";
    public static final String CEILOMETER_NETOUT = "netOUT";
    // Ceilometer Aggregated Metrics
    public static final String AGGREGATED_RAM = "aggrRAM";
    public static final String AGGREGATED_CPULOAD = "aggrCPULoad";
    public static final String AGGREGATED_DISK = "aggrDISK";

  }

}
