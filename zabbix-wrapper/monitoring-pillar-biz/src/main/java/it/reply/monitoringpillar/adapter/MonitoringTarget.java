package it.reply.monitoringpillar.adapter;

import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request.Port;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.MonitPillarEventResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.FilterTimeRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringPillarEventCallbackResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaasGroups;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaasGroups4HostList;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaasGroups4MetricList;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.SendMailRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.UpdateGroupName;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.HostGroupResponse;
import it.reply.monitoringpillar.domain.exception.MonitoringException;
import it.reply.monitoringpillar.domain.exception.NotFoundMonitoringException;

import java.util.List;

import javax.inject.Named;

// @Stateless
@Named
public interface MonitoringTarget {

  /**
   * UPDATE GROUPNAME.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param groupName
   *          name
   * @param newGroupName
   *          name
   * @return HostGroupResponse poojo
   * @throws MonitoringException
   *           wrapper
   */
  public HostGroupResponse updateMonitoredHostGroup(String zone, String serverType,
      String groupName, UpdateGroupName newGroupName) throws MonitoringException;

  /***********
   * IAAS.
   ***********/

  /**
   * Method for obtaining the Iaas machines status.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param group
   *          name
   * @param host
   *          name
   * @param serviceCategory
   *          name
   * @param tagService
   *          name
   * @param atomicServiceId
   *          id
   * @param metric
   *          name
   * @param triggersId
   *          name
   * @param history
   *          parametes
   * @param requestTime
   *          pojo
   * @return generic object
   * @throws MonitoringException
   *           wrapper
   * @throws NotFoundMonitoringException
   *           wrapper
   */
  public Object getInfoWrapperGeneric(String zone, String serverType, String group, String host,
      String serviceCategory, String tagService, List<String> atomicServiceId, String metric,
      List<String> triggersId, String history, FilterTimeRequest requestTime)
          throws MonitoringException, NotFoundMonitoringException;

  /**
   * GET Triggers from IAAS Platform.
   *
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostgroup
   *          name
   * @return WrappedIaasHealthByTrigger object
   * @throws MonitoringException
   *           wrapping the generic one
   */
  public WrappedIaasHealthByTrigger getTriggerByGroup(String zone, String serverType,
      String hostgroup) throws MonitoringException;

  /****************
   * PAAS.
   **************/
  /**
   * Create a host with certain characteristics.
   *
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostGroup
   *          name
   * @param hostName
   *          host
   * @param vmuuid
   *          uuid
   * @param vmip
   *          ip
   * @param serviceCategory
   *          name
   * @param serviceTag
   *          id
   * @param service
   *          name
   * @param activeMode
   *          condition
   * @param ports
   *          names
   * @param proxyapi
   *          naem
   * @throws MonitoringException
   *           wrapping the generic one
   */

  public void creationMonitoredHost(String zone, String serverType, String hostGroup,
      String hostName, String vmuuid, String vmip, String serviceCategory, String serviceTag,
      List<String> service, Boolean activeMode, List<Port> ports, String proxyapi)
          throws MonitoringException;

  /**
   * Create groups.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostGroupName
   *          name
   * @throws MonitoringException
   *           wrapping the generic one
   */
  public void creationMonitoredHostGroup(String zone, String serverType, String hostGroupName)
      throws MonitoringException;

  /**
   * Create proxy.
   * 
   * @param zone
   *          name
   * 
   * @param serverType
   *          type
   * @param tenantId
   *          id
   * @param proxyName
   *          name
   * @throws MonitoringException
   *           wrapping the generic one
   */
  public void creationMonitoredProxy(String zone, String serverType, String tenantId,
      String proxyName) throws MonitoringException;

  /**
   * Delete Proxy.
   * 
   * @param serverType
   *          type
   * @param proxyName
   *          name
   */
  public void deleteMonitoredProxy(String zone, String serverType, String proxyName);

  /**
   * Interface for deleting the host.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostvmuuids
   *          uuid
   * @param serviceId
   *          id
   * @throws MonitoringException
   *           wrapping the generic one
   */
  public void deleteMonitoredHost(String zone, String serverType, String hostvmuuids,
      String serviceId) throws MonitoringException;

  /********************
   * DELETE HOSTGROUP.
   *
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostGroupName
   *          name
   * @throws MonitoringException
   *           wrapping the generic one
   */
  public void deleteMonitoredHostGroup(String zone, String serverType, String hostGroupName)
      throws MonitoringException;

  /**
   * Interface useful for getting all events for Monitoring platform.
   * 
   * @param serverType
   *          type
   * @param host
   *          name
   * @param group
   *          name
   * @param tagService
   *          id
   * @param requestTime
   *          object @see FilterTimeRequest
   * @return MonitPillarEventResponse object @see MonitPillarEventResponse
   * @throws MonitoringException
   *           wrapping the generic one
   */
  public MonitPillarEventResponse getOverallServerEvents(String zone, String serverType,
      String host, String group, String tagService, FilterTimeRequest requestTime)
          throws MonitoringException;

  /**
   * Useful for disabling a specific host and a specific metric for specified host.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param group
   *          name
   * @param hostId
   *          name
   * @param metricId
   *          name
   * @param tagService
   *          id
   * @param update
   *          condition
   * @throws MonitoringException
   *           wrapper
   */
  public void getDisablingHost(String zone, String serverType, String group, String hostId,
      String metricId, String tagService, String update) throws MonitoringException;

  /*******************
   * DISABLE METRIC.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param group
   *          name
   * @param hostId
   *          name
   * @param metricId
   *          name
   * @param tagService
   *          id
   * @param update
   *          condition
   * @throws MonitoringException
   *           wrapper
   */
  public void getDisablingItem(String zone, String serverType, String group, String hostId,
      String metricId, String tagService, String update) throws MonitoringException;

  /**
   * GET TRIGGER EVENTs BY HOST.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param group
   *          name
   * @param host
   *          name
   * @return WrappedIaasHealthByTrigger pojo
   * @throws MonitoringException
   *           wrapper
   */
  WrappedIaasHealthByTrigger getTriggerByHost(String zone, String serverType, String group,
      String host) throws MonitoringException;

  // GET GROUPs List
  public MonitoringWrappedResponsePaasGroups getGroupsInfoWrapped(String zone, String serverType)
      throws MonitoringException;

  // GET HOSTs List
  public MonitoringWrappedResponsePaasGroups4HostList getHostsInfoWrapped(String zone,
      String serverType, String groupName) throws MonitoringException;

  /**
   * Interface useful for getting callbacks from Monitoring platform.
   * 
   * @param zone
   *          name
   * @param group
   *          name
   * @param hostName
   *          name
   * @param vmuuid
   *          uuid
   * @param hostServiceId
   *          id
   * @param hostIp
   *          ip
   * @param metric
   *          name
   * @param threshold
   *          name
   * @param status
   *          condition
   * @return MonitoringPillarEventCallbackResponse pojo
   * @throws MonitoringException
   *           wrapper
   */
  public MonitoringPillarEventCallbackResponse manageCallbackEvent(String zone, String group,
      String hostName, String vmuuid, String hostServiceId, String hostIp, String metric,
      String threshold, String status, String description) throws MonitoringException;

  /**
   * Get metrics list wrapped response.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param group
   *          name
   * @param host
   *          name
   * @return MonitoringWrappedResponsePaasGroups4MetricList pojo
   * @throws MonitoringException
   *           wrapper
   */
  public MonitoringWrappedResponsePaasGroups4MetricList getMetricsByHost(String zone,
      String serverType, String group, String host) throws MonitoringException;

  /**
   * SendMails.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param sendMailRequest
   *          pojo @see SendMailRequest
   */
  public void getSendMails(String zone, String serverType, SendMailRequest sendMailRequest)
      throws MonitoringException;

}