package it.reply.monitoringpillar.wrapper.zabbix.iaas;

import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.MetricsParserHelper;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.MonitoringFeatureUtility.MonitoringMetricsIaasNames;
import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.iaas.hypervisor.HypervisorGroup;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.IaaSMetric;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.IaasGroupOfMachine;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.IaasMachine;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.IaasThresholdsList;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.PrismaIaasScript;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.reply.monitoringpillar.utility.TimestampMonitoring;
import it.reply.utils.json.JsonUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class WrapperIaaSBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private String hostName;
  private String groupName;
  private String hostGroupIdtoAdapter;
  private String vmip;
  private String templateId;
  private String hostIdtoAdapter = null;
  private String metricName;
  private String metricId;
  private String triggerExpression;
  private String triggerValue;
  private String connectedHost;

  @Inject
  private ZabbixAdapterClientSetter<?> zabAdapClientSetter;

  /**
   * Get the iaas response wrapped.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param iaasTypes
   *          type
   * @param iaashosts
   *          hosts
   * @param groups
   *          groups
   * @param groupId
   *          id
   * @param hosts
   *          name
   * @param hostId
   *          id
   * @return pojo of wrapped response
   * @throws ZabbixException
   *           wrapper
   */
  public MonitoringWrappedResponseIaas getWrappedIaas(String zone, String serverType,
      String iaasTypes, String iaashosts, List<ZabbixHostGroupResponse> groups, String groupId,
      List<ZabbixMonitoredHostsResponse> hosts, String hostId) throws ZabbixException {
    MonitoringWrappedResponseIaas wrappedIaas = new MonitoringWrappedResponseIaas();
    // wrappedIaas.setTestbed(testbedType);
    wrappedIaas.setIaasMachineGroups(
        getGroupInfo(zone, serverType, iaasTypes, iaashosts, groups, groupId, hosts, hostId));
    return wrappedIaas;
  }

  /**
   * It gives info about groups in the platform.
   * 
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param iaasGroupNames
   *          list of names
   * @param iaashostNames
   *          list of host names
   * @param groups
   *          groups
   * @param groupId
   *          id
   * @param hosts
   *          hosts
   * @param hostId
   *          id
   * @return list of iaas macchines
   * @throws ZabbixException
   *           wrapper
   */
  private List<IaasGroupOfMachine> getGroupInfo(String zone, String serverType,
      String iaasGroupNames, String iaashostNames, List<ZabbixHostGroupResponse> groups,
      String groupId, List<ZabbixMonitoredHostsResponse> hosts, String hostId)
          throws ZabbixException {

    ArrayList<IaasGroupOfMachine> groupsList = new ArrayList<>();

    IaasGroupOfMachine hostgroupList = new IaasGroupOfMachine();

    for (ZabbixHostGroupResponse groupInfo : groups) {

      hostgroupList.setIaasMachinesList(new ArrayList<IaasMachine>());

      groupName = groupInfo.getName();

      /*******************************
       * HOST GROUP SPECIFIED
       ******************************/

      if (iaasGroupNames != null && iaashostNames == null) {
        String hostIdtoAdapter = null;

        hostGroupIdtoAdapter = groupInfo.getGroupid();
        hostgroupList.setIaasGroupName(groupName);
        hostgroupList.setIaasMachinesList(
            getMachinesList(zone, serverType, hostGroupIdtoAdapter, hostIdtoAdapter, hosts));
        groupsList.add(hostgroupList);

        /*************************************************
         * ASK FOR SPECIFIC HOST INTO A GROUP
         ************************************************/

      } else if (iaashostNames != null) {
        hostGroupIdtoAdapter = groupInfo.getGroupid();
        hostgroupList.setIaasGroupName(groupInfo.getName());

        hostIdtoAdapter = hosts.get(0).getHostid();

        hostgroupList.getIaasMachines().addAll(
            (getMachinesList(zone, serverType, hostGroupIdtoAdapter, hostIdtoAdapter, hosts)));

        groupsList.add(hostgroupList);
      } else if (!(groupName.equals("Hypervisors") || groupName.equals("Linux servers")
          || groupName.equals("Virtual machines") || groupName.equals("Discovered hosts")
          || groupName.equals("Zabbix servers") || groupName.equals("Templates"))
          && iaasGroupNames == null) {
        hostGroupIdtoAdapter = groupInfo.getGroupid();

        hostgroupList.setIaasGroupName(groupName);
        hostgroupList.setIaasMachinesList(
            getMachinesList(zone, serverType, hostGroupIdtoAdapter, hostIdtoAdapter, hosts));
        groupsList.add(hostgroupList);
      }
    }
    return groupsList;
  }

  /**
   * Get the list of Machines.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostGroupIdtoAdapter
   *          name
   * @param hostIdtoAdapter
   *          id
   * @param hostInfo
   *          parameter
   * @return list of machines in Iaas
   * @throws ZabbixException
   *           wrapper
   */
  private ArrayList<IaasMachine> getMachinesList(String zone, String serverType,
      String hostGroupIdtoAdapter, String hostIdtoAdapter,
      List<ZabbixMonitoredHostsResponse> hostInfo) throws ZabbixException {

    ArrayList<IaasMachine> machinesList = new ArrayList<>();

    if (!(hostIdtoAdapter == null)) {
      machinesList = new ArrayList<IaasMachine>();

      for (ZabbixMonitoredHostsResponse hostResult : hostInfo) {
        IaasMachine machines = new IaasMachine();
        String hostIdtoAdapterCurrent = hostResult.getHostid();
        if (hostIdtoAdapterCurrent.equals(hostIdtoAdapter)) {
          String monitoredHost = hostResult.getAvailable();
          if (monitoredHost.equals("1")) {
            connectedHost = "MONITORED";
          } else {
            connectedHost = "No communication between the agent and the Server";
          }

          ArrayList<ZabbixItemResponse> zabbixinterfaceResponse =
              (ArrayList<ZabbixItemResponse>) zabAdapClientSetter.getZabbixFeatureMultiService(zone,
                  serverType, hostIdtoAdapter, null, ZabbixMethods.INTERFACE.getzabbixMethod());

          vmip = zabbixinterfaceResponse.get(0).getIp();

          // Set Hosts' carachteristics
          hostName = hostResult.getName();
          machines.setMachineName(hostName);
          machines.setIp(vmip);
          machines.setConnection(connectedHost);

          machines.setMetrics(getIaasMetrics(zone, serverType, hostIdtoAdapter));

          machinesList.add(machines);
        }
      }

      // no HostID
    } else if (hostIdtoAdapter == null) {
      machinesList = new ArrayList<IaasMachine>();

      for (ZabbixMonitoredHostsResponse hostResult : hostInfo) {
        IaasMachine machines = new IaasMachine();
        hostName = hostResult.getName();
        if (hostName != "Zabbix server") {
          hostIdtoAdapter = hostResult.getHostid();
          String monitoredHost = hostResult.getAvailable();
          if (monitoredHost.equals("1")) {
            connectedHost = "MONITORED";
          } else {
            connectedHost = "No communication between the agent and the Server";
          }

          // if (IaaS==true)
          ArrayList<ZabbixItemResponse> zabbixinterfaceResponse =
              (ArrayList<ZabbixItemResponse>) zabAdapClientSetter.getZabbixFeatureMultiService(zone,
                  serverType, hostIdtoAdapter, null, ZabbixMethods.INTERFACE.getzabbixMethod());

          vmip = zabbixinterfaceResponse.get(0).getIp();

        }
        // Set Hosts' carachteristics
        machines.setMachineName(hostName);
        machines.setIp(vmip);
        machines.setConnection(connectedHost);
        machines.setMetrics(getIaasMetrics(zone, serverType, hostIdtoAdapter));
        machinesList.add(machines);
      }
    }
    return machinesList;
  }

  /**
   * For a certain host and a certain service category gets metrics associated to it.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostidtoAdapter
   *          id
   * @return List of metrics
   * @throws ZabbixException
   *           wrapper
   */
  private ArrayList<IaaSMetric> getIaasMetrics(String zone, String serverType,
      String hostidtoAdapter) throws ZabbixException {
    ArrayList<IaaSMetric> metrics = new ArrayList<>();

    ArrayList<ZabbixItemResponse> metrics4Host =
        (ArrayList<ZabbixItemResponse>) zabAdapClientSetter.getItemsService(zone, serverType,
            hostidtoAdapter, templateId, null, null, ZabbixMethods.METRIC.getzabbixMethod(), null);

    // Loop into all Metrics
    for (ZabbixItemResponse metrictype : metrics4Host) {
      IaaSMetric iaasMetrics = new IaaSMetric();
      metricName = metrictype.getName();
      metricId = metrictype.getItemid();
      // metricKey = metrictype.getKey();
      String metricKeyDebug = groupName + "." + hostName + "." + metricName;

      String metricValueType = metrictype.getValueType();

      Object metricValue =
          MetricsParserHelper.getMetricIaaSParsedValue(metricValueType, metrictype.getLastvalue());

      // if Metrics are values coming from External Scritps manage this
      if (metricName
          .equals(MonitoringMetricsIaasNames.PRISMA_IAAS_SCRIPT.getMonitoringMetricsIaasNames())) {
        try {
          PrismaIaasScript scriptIaas = (PrismaIaasScript) JsonUtility
              .deserializeJson(metricValue.toString(), PrismaIaasScript.class);
          metricValue = scriptIaas;

          if (scriptIaas.getNetwork().equals("OK") && scriptIaas.getStorage().equals("OK")
              && scriptIaas.getAvailableNodes() > 0) {
            iaasMetrics.setMetricStatus("OK");
          } else {
            iaasMetrics.setMetricStatus("PROBLEM");
          }
        } catch (Exception ex) {
          throw new ZabbixException(ex);
        }

      } else if (metricName.equals(
          MonitoringMetricsIaasNames.HYPERVISOR_IAAS_SCRIPT.getMonitoringMetricsIaasNames())) {
        try {
          HypervisorGroup hypervisorScript = (HypervisorGroup) JsonUtility
              .deserializeJson(metricValue.toString(), HypervisorGroup.class);
          metricValue = hypervisorScript;
        } catch (Exception ex) {
          throw new ZabbixException(ex);
        }
      }
      iaasMetrics.setMetricName(metricName);
      iaasMetrics.setMetricValue(metricValue);
      String metricTime =
          TimestampMonitoring.decodUnixTime2Date(Long.parseLong(metrictype.getLastclock()));
      iaasMetrics.setMetricTime(metricTime);

      if (!(metricName == MonitoringMetricsIaasNames.PRISMA_IAAS_SCRIPT
          .getMonitoringMetricsIaasNames())) {

        if (!(metricValue.equals("0") || metricValue == null)) {
          iaasMetrics.setMetricStatus("OK");
        } else {
          iaasMetrics.setMetricStatus("PROBLEM at: " + metricKeyDebug);
        }
      }
      iaasMetrics.setIaasThresholds(getThreshold(zone, serverType, metricId));
      iaasMetrics.setMetricKey(metricKeyDebug);
      metrics.add(iaasMetrics);
    }
    return metrics;
  }

  /**
   * Get Triggers for each metric.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param metricId
   *          id
   * @return list of triggers
   * @throws ZabbixException
   *           wrapper
   */
  private List<IaasThresholdsList> getThreshold(String zone, String serverType, String metricId)
      throws ZabbixException {

    ArrayList<ZabbixItemResponse> trigger4Host =
        (ArrayList<ZabbixItemResponse>) zabAdapClientSetter.getTriggerService(zone, serverType,
            hostIdtoAdapter, metricId, ZabbixMethods.TRIGGER.getzabbixMethod());
    ArrayList<IaasThresholdsList> thresholds = new ArrayList<IaasThresholdsList>();
    for (ZabbixItemResponse triggerList : trigger4Host) {
      IaasThresholdsList iaasThresholds = new IaasThresholdsList();

      triggerExpression = triggerList.getExpression();
      triggerValue = triggerList.getValue();
      String triggerKeyDebug =
          groupName + "." + hostName + "." + metricName + "." + triggerExpression;

      iaasThresholds.setTriggerKey(triggerKeyDebug);

      iaasThresholds.setTriggerExpression(triggerExpression);
      iaasThresholds.setTriggerValue(triggerValue);
      if (triggerValue.equals("0")) {
        iaasThresholds.setTriggerStatus("OK");
      } else {
        iaasThresholds.setTriggerStatus("PROBLEM at: " + triggerKeyDebug);
      }
      thresholds.add(iaasThresholds);
    }
    return thresholds;
  }
}
