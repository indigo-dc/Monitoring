package it.reply.monitoringpillar.wrapper;

import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.NotFoundZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.GroupWrapped4MetricList;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedMetric;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaasGroups4MetricList;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.PaasMachineWrapped4MetricList;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV24;
import it.reply.monitoringpillar.domain.exception.MonitoringException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class MetricsWrapperBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject
  private ZabbixAdapterClientSetter<?> zabAdapSetter;

  /**
   * GEt group for metrics.
   */
  public MonitoringWrappedResponsePaasGroups4MetricList getMetricsWrapped(String zone,
      String serverType, String groupName, List<ZabbixHostGroupResponse> groupList,
      ZabbixHostGroupResponse group, List<ZabbixMonitoredHostResponseV24> hosts)
          throws ZabbixException, MonitoringException {

    MonitoringWrappedResponsePaasGroups4MetricList wrappedPaas =
        new MonitoringWrappedResponsePaasGroups4MetricList();

    wrappedPaas.setGroups(getGroupInfo(zone, serverType, groupName, groupList, group, hosts));
    return wrappedPaas;
  }

  /**
   * Get group info.
   */
  @SuppressWarnings({ "unchecked", "unused" })
  public List<GroupWrapped4MetricList> getGroupInfo(String zone, String serverType,
      String groupName, List<ZabbixHostGroupResponse> groupList, ZabbixHostGroupResponse hostGroup,
      List<ZabbixMonitoredHostResponseV24> hosts) throws ZabbixException, MonitoringException {

    String hostGroupIdtoAdapter = null;
    List<ZabbixItemResponse> zabbixTemplateResponse = new ArrayList<>();
    List<ZabbixItemResponse> metrics4Host = new ArrayList<>();
    List<ZabbixItemResponse> trigger4Host = new ArrayList<>();
    // Prepare the Array for groups
    List<GroupWrapped4MetricList> groupsresult = new ArrayList<>();
    boolean workgroupFound = false;

    // For each group into array set the name and ask collect the machines
    GroupWrapped4MetricList group = new GroupWrapped4MetricList();
    for (ZabbixHostGroupResponse hostgroup : groupList) {
      group.setGroupName(hostGroup.getName());
      for (ZabbixMonitoredHostResponseV24 host : hosts) {
        group.getPaasMachines().addAll(
            getMachinesList(zone, serverType, hostGroupIdtoAdapter, groupName, hosts, host));
      }
    }
    groupsresult.add(group);

    if (groupsresult.isEmpty()) {
      throw new NotFoundZabbixException("Wrong resource Group Name: " + groupName
          + " inserted or not existing into monitoring platform");
    }
    return groupsresult;
  }

  /**
   * Retrieves Machines depending on passed parameters.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostGroupIdtoAdapter
   *          id
   * @param hostgroupName
   *          name
   * @param hostInfo
   *          pojo
   * @param host
   *          host pojo
   * @return list of machines pojo
   * @throws ZabbixException
   *           excep
   * @throws MonitoringException
   *           excep
   */
  public List<PaasMachineWrapped4MetricList> getMachinesList(String zone, String serverType,
      String hostGroupIdtoAdapter, String hostgroupName,
      List<ZabbixMonitoredHostResponseV24> hostInfo, ZabbixMonitoredHostResponseV24 host)
          throws ZabbixException, MonitoringException {

    PaasMachineWrapped4MetricList paasMachine = new PaasMachineWrapped4MetricList();
    paasMachine.setMetrics(new ArrayList<MonitoringWrappedMetric>());

    String vmip = null;
    List<ZabbixItemResponse> zabbixinterfaceResponse = new ArrayList<>();

    String hostName = host.getName();
    String hostId = host.getHostid();

    // API useful for getting IP and Host's networkInfo
    zabbixinterfaceResponse = zabAdapSetter.getZabbixFeatureMultiService(zone, serverType, hostId,
        null, ZabbixMethods.INTERFACE.getzabbixMethod());

    for (ZabbixItemResponse zabbixresult : zabbixinterfaceResponse) {
      vmip = zabbixresult.getIp();
    }
    paasMachine.setMachineName(hostName);
    paasMachine.setIp(vmip);

    if (host.getAvailable().equalsIgnoreCase("1") || host.getAvailable().equalsIgnoreCase("0")) {
      paasMachine.setEnabled(true);
    } else {
      paasMachine.setEnabled(false);
    }

    List<PaasMachineWrapped4MetricList> machines = new ArrayList<>();
    for (ZabbixItemResponse item : host.getItems()) {
      paasMachine.getMetrics().addAll(getMetricList(serverType, hostGroupIdtoAdapter, hostgroupName,
          hostInfo, host, paasMachine, item));
    }
    machines.add(paasMachine);

    if (machines.isEmpty()) {
      // if(paasMachine==null){
      throw new NotFoundZabbixException("Host ID is not present in zabbix server");
    }
    // return paasMachine;
    return machines;
  }

  /**
   * It sets the metrics.
   *
   * @param serverType
   *          type
   * @param hostGroupIdtoAdapter
   *          id
   * @param hostgroupName
   *          name
   * @param hosts
   *          list
   * @param host
   *          pojo
   * @param paasMachine
   *          pojo
   * @param item
   *          name
   * @return list of metric pojos wrapped
   * @throws ZabbixException
   *           wrapper
   * @throws MonitoringException
   *           wrapper
   */
  public List<MonitoringWrappedMetric> getMetricList(String serverType, String hostGroupIdtoAdapter,
      String hostgroupName, List<ZabbixMonitoredHostResponseV24> hosts,
      ZabbixMonitoredHostResponseV24 host, PaasMachineWrapped4MetricList paasMachine,
      ZabbixItemResponse item) throws ZabbixException, MonitoringException {

    MonitoringWrappedMetric metricWrapped = new MonitoringWrappedMetric();
    metricWrapped.setMetricValue(item.getLastvalue());
    metricWrapped.setMetricName(item.getName());
    metricWrapped.setMetricTime(item.getLastclock());
    List<MonitoringWrappedMetric> metrics = new ArrayList<>();
    metrics.add(metricWrapped);
    return metrics;
  }
}