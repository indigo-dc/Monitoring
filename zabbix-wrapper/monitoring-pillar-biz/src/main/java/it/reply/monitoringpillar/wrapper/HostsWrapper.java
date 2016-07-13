package it.reply.monitoringpillar.wrapper;

import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.NotFoundZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.GroupWrapped4HostList;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaasGroups4HostList;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.PaasMachineListWrapped;
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
public class HostsWrapper implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject
  private ZabbixAdapterClientSetter<?> zabAdapSetter;

  /**
   * Get host wrapped.
   */
  public MonitoringWrappedResponsePaasGroups4HostList getHostsWrapped(String zone,
      String serverType, String groupName, List<ZabbixHostGroupResponse> groups,
      List<ZabbixMonitoredHostResponseV24> hosts) throws ZabbixException, MonitoringException {

    MonitoringWrappedResponsePaasGroups4HostList wrappedPaas =
        new MonitoringWrappedResponsePaasGroups4HostList();

    // wrappedPaas.setEnvironment(testbedType);

    wrappedPaas.setGroups(getGroupInfo(zone, serverType, groupName, groups, hosts));
    return wrappedPaas;
  }

  /**
   * GET group info.
   */
  @SuppressWarnings({ "unchecked", "unused" })
  public List<GroupWrapped4HostList> getGroupInfo(String zone, String serverType, String groupName,
      List<ZabbixHostGroupResponse> groups, List<ZabbixMonitoredHostResponseV24> hosts)
          throws ZabbixException, MonitoringException {

    // if (IaaS==true)
    String hostGroupIdtoAdapter = null;
    ArrayList<ZabbixItemResponse> zabbixTemplateResponse = new ArrayList<>();
    ArrayList<ZabbixItemResponse> metrics4Host = new ArrayList<>();
    ArrayList<ZabbixItemResponse> trigger4Host = new ArrayList<>();

    // Prepare the Array for groups
    List<GroupWrapped4HostList> groupsresult = new ArrayList<>();
    boolean workgroupFound = false;
    String hostName = null;
    String serviceName = null;

    // For each group into array set the name and ask collect the machines
    for (ZabbixHostGroupResponse workGroup : groups) {
      GroupWrapped4HostList group = new GroupWrapped4HostList();
      group.setPaasMachines(new ArrayList<PaasMachineListWrapped>());

      group.setGroupName(group.getGroupName());

      /**********************
       * Only group specified.
       **********************/

      String hostIdtoAdapter = null;
      String metricIdtoAdapter = null;
      String templateIdtoAdapter = null;
      String triggerIdtoAdapter = null;

      hostGroupIdtoAdapter = workGroup.getGroupid();
      groupName = workGroup.getName();
      group.setGroupName(groupName);

      for (ZabbixMonitoredHostResponseV24 host : hosts) {
        PaasMachineListWrapped paasMachine = new PaasMachineListWrapped();

        String hostId = host.getHostid();
        // GET the list of items
        List<ZabbixItemResponse> items = host.getItems();

        group.getPaasMachines().addAll(getMachinesList(zone, serverType, hostGroupIdtoAdapter,
            groupName, hosts, host, paasMachine));
      }
      groupsresult.add(group);
    }
    if (groupsresult.isEmpty()) {
      throw new NotFoundZabbixException(
          "Wrong resource Group Name inserted or not existing into monitoring platform");
    }
    return groupsresult;
  }

  /**
   * Retrieves Machines depending on passed parameters.
   */
  @SuppressWarnings({ "unchecked", "unused" })
  public List<PaasMachineListWrapped> getMachinesList(String zone, String serverType,
      String hostGroupIdtoAdapter, String hostgroupName,
      List<ZabbixMonitoredHostResponseV24> hostInfo, ZabbixMonitoredHostResponseV24 host,
      PaasMachineListWrapped paasMachine) throws ZabbixException, MonitoringException {

    String vmip = null;
    ArrayList<ZabbixItemResponse> zabbixinterfaceResponse = new ArrayList<>();

    String hostId = host.getHostid();
    String monitoredHost = host.getAvailable();

    if (monitoredHost.equals("1")) {
      String connectedHost = "MONITORED";
    } else {
      String connectedHost =
          "External Script Communication, Failed Comunication between agent and server or Disabled";
    }
    // API useful for getting IP and Host's networkInfo
    zabbixinterfaceResponse =
        (ArrayList<ZabbixItemResponse>) zabAdapSetter.getZabbixFeatureMultiService(zone, serverType,
            hostId, null, ZabbixMethods.INTERFACE.getzabbixMethod());

    for (ZabbixItemResponse zabbixresult : zabbixinterfaceResponse) {
      vmip = zabbixresult.getIp();
      String vmdns = zabbixresult.getDns();
    }
    String hostName = host.getName();
    paasMachine.setMachineName(hostName);
    paasMachine.setIp(vmip);
    paasMachine.setServiceCategory(host.getInventory().getType());
    paasMachine.setServiceId(host.getInventory().getTag());
    if (host.getAvailable().equalsIgnoreCase("1") || host.getAvailable().equalsIgnoreCase("0")) {
      paasMachine.setEnabled(true);
    } else {
      paasMachine.setEnabled(false);
    }
    List<PaasMachineListWrapped> machines = new ArrayList<>();
    machines.add(paasMachine);

    if (machines.isEmpty()) {
      // if(paasMachine==null){
      throw new NotFoundZabbixException("Host ID is not present in zabbix server");
    }
    // return paasMachine;
    return machines;
  }

}