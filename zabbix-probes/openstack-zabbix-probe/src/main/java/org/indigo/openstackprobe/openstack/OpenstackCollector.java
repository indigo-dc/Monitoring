package org.indigo.openstackprobe.openstack;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.indigo.zabbix.utils.LifecycleCollector;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.beans.AppOperation;

public class OpenstackCollector extends LifecycleCollector {

  private OpenStackClient openstackClient;
  private static final Log logger = LogFactory.getLog(OpenstackCollector.class);
  private static final String APP_NAME = "zabbix-test-app";
  private String provider;
  private static final Logger log = LogManager.getLogger(OpenstackCollector.class);
  private CmdbClient cmdbClient = new CmdbClient();
  private final ScheduledExecutorService scheduler;
  private long initialDelay = 10;
  private int numThreads = 2;

  /**
   * Default constructor.
   */
  /**
   * This is the main constructor of the class, in order to retrieve the required information for
   * carrying out the monitoring activities.
   * 
   * @param providerId
   *          String with the identifier of the provider evaluated
   * @param providerURL
   *          String representing the Openstack API URL
   * @param keystoneURL
   *          String representing the Keystone API URL
   */
  protected OpenstackCollector(String providerId, String providerUrl, String keystoneUrl) {

    String url = PropertiesManager.getProperty(OpenstackProbeTags.COMPUTE_LOCATION);
    String username = PropertiesManager.getProperty(OpenstackProbeTags.OPENSTACK_USER);
    String password = PropertiesManager.getProperty(OpenstackProbeTags.OPENSTACK_PASSWORD);
    scheduler = Executors.newScheduledThreadPool(numThreads);

    provider = providerId;
//    openstackClient = new OpenStackClient(keystoneUrl, providerUrl, providerId);

    log.info("Nova Endpoint: " + providerUrl);
    log.info("Keystone Endpoint: " + keystoneUrl);
  }

  @Override
  protected String getHostName() {
      return provider;
  }
       
  @Override
  protected AppOperation clear() {

    int status = 0;
    long currentTime = new Date().getTime();

//    AppOperation getAppResponse = null;
    OpenstackProbeResult appResponse = null;
    try {
      appResponse = openstackClient.getOpenstackMonitoringInfo();
    } catch (Exception e) {
      if (404 == appResponse.getGlobalResult()) {
        long respTime = new Date().getTime() - currentTime;
        return new AppOperation(AppOperation.Operation.CLEAR, true, appResponse.getGlobalResult(), respTime);
      } else {
        long respTime = new Date().getTime() - currentTime;
        return new AppOperation(AppOperation.Operation.CLEAR, false, appResponse.getGlobalResult(), respTime);
      }
    }

    if (appResponse != null) {

      DeleteVmResult delResult = null;
      try {
        delResult = openstackClient.deleteVm(openstackClient.getOsImage());
      } catch (Exception e) {
        logger.error("Can't clear test application", e);
        long respTime = new Date().getTime() - currentTime;
        return new AppOperation(AppOperation.Operation.CLEAR, false, delResult.getDeleteVmResult(), respTime);
      }
    }

    long respTime = new Date().getTime() - currentTime;
    return new AppOperation(AppOperation.Operation.CLEAR, true, 200, respTime);
  }

  @Override
  protected AppOperation create() {
    long time = new Date().getTime();
//    String instanceName = INSTANCE_NAME + new BigInteger(37, new SecureRandom()).toString(16);
    
//    ServerCreate create = openstackClient.createOsServer(instanceName);
    CreateVmResult createdProbe = null;
    try {
      createdProbe = openstackClient.getOpenstackMonitoringInfo().getCreateVmElement();
    } catch (Exception e) {
      long respTime = new Date().getTime() - time;
      return new AppOperation(AppOperation.Operation.CREATE, false, createdProbe.getCreateVmResult(), respTime);
    }
    long respTime = new Date().getTime() - time;
    return new AppOperation(AppOperation.Operation.CREATE, true, 200, respTime);
  }

  @Override
  protected AppOperation retrieve() {
    long currentTime = new Date().getTime();
    
    OpenstackProbeResult appResponse = null;
    InspectVmResult vmapp =  appResponse.getInspectVmElement();
    try {
      appResponse = openstackClient.getOpenstackMonitoringInfo();
      if (appResponse != null) {
        long respTime = new Date().getTime() - currentTime;
        return new AppOperation(AppOperation.Operation.RUN, true, 200, respTime);
      } else {
        long respTime = new Date().getTime() - currentTime;
        logger.error("Can't finde application by name " + APP_NAME);
        return new AppOperation(AppOperation.Operation.RUN, false, 404, respTime);
      }
    } catch (Exception e) {
      logger.error("Error getting instance ", e);
      long respTime = new Date().getTime() - currentTime;
      return new AppOperation(AppOperation.Operation.RUN, false, vmapp.getInspectVmResult(), respTime);
    }
  }

  @Override
  protected AppOperation delete() {

    long currentTime = new Date().getTime();

    DeleteVmResult deleteProbe = null;
    try {
      deleteProbe = openstackClient.getOpenstackMonitoringInfo().getDeleteVmElement();
      long respTime = new Date().getTime() - currentTime;
      return new AppOperation(AppOperation.Operation.DELETE,
          (deleteProbe != null), 200, respTime);
    } catch (Exception e) {
      long respTime = new Date().getTime() - currentTime;
      logger.error("Error deleting application " + APP_NAME);
      return new AppOperation(AppOperation.Operation.DELETE, false, deleteProbe.getDeleteVmResult(), respTime);
    }

  }

}
