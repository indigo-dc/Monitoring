package it.reply.monitoringpillar.adapter.nagios;

import it.reply.monitoringpillar.adapter.MonitoringTarget;
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

import java.util.List;

import javax.ejb.Stateless;

//@Named
@Stateless
// @Local(MonitoringAdapter.class)
@IMonitAdaptNagios
public class MonitoringAdapterNagios implements MonitoringTarget {

  @Override
  public HostGroupResponse updateMonitoredHostGroup(String zone, String serverType,
      String groupName, UpdateGroupName newGroupName) throws MonitoringException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object getInfoWrapperGeneric(String zone, String serverType, String group, String host,
      String serviceCategory, String tagService, List<String> atomicServiceId, String metric,
      List<String> triggersId, String history, FilterTimeRequest requestTime)
          throws MonitoringException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public WrappedIaasHealthByTrigger getTriggerByGroup(String zone, String serverType,
      String hostgroup) throws MonitoringException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void creationMonitoredHostGroup(String zone, String serverType, String hostGroupName)
      throws MonitoringException {
    // TODO Auto-generated method stub

  }

  @Override
  public void creationMonitoredProxy(String zone, String serverType, String tenantId,
      String proxyName) throws MonitoringException {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteMonitoredProxy(String zone, String serverType, String proxyName) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteMonitoredHost(String zone, String serverType, String hostvmuuids,
      String serviceId) throws MonitoringException {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteMonitoredHostGroup(String zone, String serverType, String hostGroupName)
      throws MonitoringException {
    // TODO Auto-generated method stub

  }

  @Override
  public MonitPillarEventResponse getOverallServerEvents(String zone, String serverType,
      String host, String group, String tagService, FilterTimeRequest requestTime)
          throws MonitoringException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void getDisablingHost(String zone, String serverType, String group, String hostId,
      String metricId, String tagService, String update) throws MonitoringException {
    // TODO Auto-generated method stub

  }

  @Override
  public void getDisablingItem(String zone, String serverType, String group, String hostId,
      String metricId, String tagService, String update) throws MonitoringException {
    // TODO Auto-generated method stub

  }

  @Override
  public WrappedIaasHealthByTrigger getTriggerByHost(String zone, String serverType, String group,
      String host) throws MonitoringException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonitoringWrappedResponsePaasGroups getGroupsInfoWrapped(String zone, String serverType)
      throws MonitoringException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonitoringWrappedResponsePaasGroups4HostList getHostsInfoWrapped(String zone,
      String serverType, String groupName) throws MonitoringException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonitoringPillarEventCallbackResponse manageCallbackEvent(String zone, String group,
      String hostName, String vmuuid, String hostServiceId, String hostIp, String metric,
      String threshold, String status, String description) throws MonitoringException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonitoringWrappedResponsePaasGroups4MetricList getMetricsByHost(String zone,
      String serverType, String group, String host) throws MonitoringException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void getSendMails(String zone, String serverType, SendMailRequest sendMailRequest)
      throws MonitoringException {
    // TODO Auto-generated method stub

  }

  @Override
  public void creationMonitoredHost(String zone, String serverType, String hostGroup,
      String hostName, String vmuuid, String vmip, String serviceCategory, String serviceTag,
      List<String> service, Boolean activeMode, List<Port> ports, String proxyapi)
          throws MonitoringException {
    // TODO Auto-generated method stub

  }

}