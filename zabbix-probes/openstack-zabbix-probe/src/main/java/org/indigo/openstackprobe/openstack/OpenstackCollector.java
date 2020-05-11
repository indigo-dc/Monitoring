package org.indigo.openstackprobe.openstack;

import com.indigo.zabbix.utils.LifecycleCollector;
import com.indigo.zabbix.utils.ZabbixMetrics;
import com.indigo.zabbix.utils.beans.AppOperation;
import com.indigo.zabbix.utils.beans.DocDataType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.exceptions.ConnectionException;
import org.openstack4j.model.compute.Server;

import java.util.Date;
import java.util.List;

/**
 * @author Reply Santer. Collects the information about cloud providers to be metered and then
 *     implements the LifeCycleCollector by managing the metrics.
 */
public class OpenstackCollector extends LifecycleCollector {

  public String provider;
  public String serviceId;
  public String keystoneEndpoint;
  public String accessToken;

  private static final Logger log = LogManager.getLogger(OpenstackCollector.class);
  protected OpenStackProbeResult probeResult;
  private static final String INSTANCE_NAME = OpenStackProbeTags.INSTANCE_NAME;
  OpenStackClient openstackClient;
  OpenStackComponent openstackComponent = new OpenStackComponent();
  CheckForProviderDifferences checkForProviderDifference = new CheckForProviderDifferences();

  /**
   * This is the main constructor of the class, in order to retrieve the required information for *
   * carrying out the monitoring activities.
   *
   * @param accessToken The access token to use in OpenStack
   * @param serviceId The service identifier to use
   * @param providerId The service provider identifier to use
   * @param keystoneUrl The URL to the keystone service
   * @throws IllegalArgumentException
   */
  protected OpenstackCollector(
      String accessToken, String serviceId, String providerId, String keystoneUrl)
      throws IllegalArgumentException {
    this.provider = providerId;
    this.serviceId = serviceId;
    this.keystoneEndpoint = keystoneUrl;

    openstackClient = new OpenStackClient(accessToken, keystoneUrl, providerId);
  }

  /**
   * Launches the creation, cancellation of VMs for each f the providers taken in cosideration.
   *
   * @return OpenstackProbeResult
   */
  protected OpenStackProbeResult getOsProbeResult() {
    if (probeResult == null) {
      try {
        String project = checkForProviderDifference.checkForOpenstackProject(provider);

        probeResult = openstackClient.getOpenstackMonitoringInfo(project);

      } catch (Exception te) {
        log.debug("Unable to get the information about the provider {}", provider, te);
      }
    }
    return probeResult;
  }

  @Override
  public String getHostName() {
    return this.serviceId;
  }

  @Override
  public String getGroup() {
    return this.provider;
  }

  @Override
  public DocDataType.ServiceType getServiceType() {
    return DocDataType.ServiceType.OPENSTACK;
  }

  @Override
  protected AppOperation create() {
    long currentTime = new Date().getTime();
    VmResultCreation createdProbe = null;
    try {
      createdProbe = getOsProbeResult().getCreateVmElement();
      log.info(
          "Collected creation meters in: "
              + String.valueOf(Math.abs(createdProbe.getCreateVmResponseTime())));
    } catch (ConnectionException ce) {
      return getResultForConnectionException(ce, currentTime);
    } catch (Exception ex) {
      long respTime = new Date().getTime() - currentTime;
      return new AppOperation(AppOperation.Operation.CREATE, false, 404, respTime);
    }
    return new AppOperation(
        AppOperation.Operation.CREATE, true, 200, Math.abs(createdProbe.getCreateVmResponseTime()));
  }

  @Override
  protected AppOperation retrieve() {

    long currentTime = new Date().getTime();
    VmResultInspection vmappRetrieved = null;
    try {
      vmappRetrieved = getOsProbeResult().getInspectVmElement();
      if (vmappRetrieved != null) {
        log.info(
            "Collected inspection meters in: "
                + String.valueOf(vmappRetrieved.getInspectVmResponseTime()));
        return new AppOperation(
            AppOperation.Operation.RUN, true, 200, vmappRetrieved.getInspectVmResponseTime());
      } else {
        long respTime = new Date().getTime() - currentTime;
        log.error("Can't find server instance");
        return new AppOperation(AppOperation.Operation.RUN, false, 404, respTime);
      }
    } catch (ConnectionException ce) {
      return getResultForConnectionException(ce, currentTime);
    } catch (Exception exc) {
      log.error("Error getting instance ", exc);
      long respTime = new Date().getTime() - currentTime;
      return new AppOperation(
          AppOperation.Operation.RUN, false, probeResult.getGlobalAvailability(), respTime);
    }
  }

  /**
   * Manages the reponse in case of connection exception to a specific provider.
   *
   * @param ce ConnectionException
   * @param currentTime currenttime
   * @return AppOperation
   */
  private AppOperation getResultForConnectionException(Exception ce, long currentTime) {
    long respTime = new Date().getTime() - currentTime;
    log.debug("Unable to connect to provider: {}", provider, ce);
    return new AppOperation(AppOperation.Operation.DELETE, false, 404, respTime);
  }

  @Override
  protected AppOperation delete() {

    long currentTime = new Date().getTime();
    VmResultDeletion deleteProbe = null;
    try {
      deleteProbe = getOsProbeResult().getDeleteVmElement();
      log.info(
          "Collected delete meters in: " + String.valueOf(deleteProbe.getDeleteVmResponseTime()));
      return new AppOperation(
          AppOperation.Operation.DELETE,
          deleteProbe != null,
          200,
          deleteProbe.getDeleteVmResponseTime());
    } catch (ConnectionException ce) {
      return getResultForConnectionException(ce, currentTime);
    } catch (Exception exc) {
      long respTime = new Date().getTime() - currentTime;
      log.error("Error deleting server instance ");
      return new AppOperation(AppOperation.Operation.DELETE, false, 404, respTime);
    }
  }

  @Override
  protected AppOperation clear() {
    long currentTime = new Date().getTime();
    try {
      List<? extends Server> instancesList = getOsProbeResult().getOsInstanceList();
      for (Server server : instancesList) {
        if (server.getName().contains(INSTANCE_NAME)) {
          log.debug(
              "Still the instance "
                  + server.getName()
                  + " is listed or is about to be totally removed");

          long respTime = new Date().getTime() - currentTime;
          log.info("Collected clear statistics in: " + String.valueOf(respTime));
          return new AppOperation(AppOperation.Operation.CLEAR, true, 204, respTime);
        }
      }
    } catch (ConnectionException ce) {
      return getResultForConnectionException(ce, currentTime);
    }catch (Exception exc) {
        long respTime = new Date().getTime() - currentTime;
        log.error("Error on clear operation ");
        return new AppOperation(AppOperation.Operation.CLEAR, false, probeResult.getGlobalAvailability(), respTime);
      }
    long respTime = new Date().getTime() - currentTime;
    return new AppOperation(AppOperation.Operation.CLEAR, true, 200, respTime);
  }

  @Override
  public ZabbixMetrics getMetrics() {
    ZabbixMetrics metrics = super.getMetrics();
    OpenStackProbeResult result = getOsProbeResult();
    metrics.getMetrics().put("openstack.status", Integer.toString(result.getGlobalResult()));
    metrics.getMetrics().put("openstack.result", Integer.toString(result.getGlobalAvailability()));
    metrics.getMetrics().put("openstack.responseTime", Long.toString(result.getGlobalResponseTime()));
    return metrics;
  }
}
