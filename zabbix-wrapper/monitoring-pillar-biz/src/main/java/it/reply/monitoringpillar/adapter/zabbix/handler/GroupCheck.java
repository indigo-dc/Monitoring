package it.reply.monitoringpillar.adapter.zabbix.handler;

import it.reply.monitoringpillar.adapter.MonitoringAdapteeZabbix;
import it.reply.monitoringpillar.adapter.zabbix.ZabbixConstant;
import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixGroup;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV24;
import it.reply.monitoringpillar.domain.exception.MonitoringException;
import it.reply.monitoringpillar.domain.exception.NotFoundMonitoringException;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class GroupCheck extends MonitoringAdapteeZabbix {

  @EJB
  private ZabbixAdapterClientSetter<?> zabAdapClientSetter;

  /**
   * Determines whether a group exists or not.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostGroup
   *          name
   * @param host
   *          name
   * @param tagService
   *          id
   * @return condition
   * @throws MonitoringException
   *           exe
   */
  public boolean isGroupPresent(String zone, String serverType, String hostGroup, String host,
      String tagService) throws MonitoringException {

    try {
      boolean groupFound = false;
      List<ZabbixHostGroupResponse> workgroups = zabAdapClientSetter.getHostGroupsService(zone,
          serverType, ZabbixMethods.HOSTGROUP.getzabbixMethod(), null, null, null);
      for (ZabbixHostGroupResponse group : workgroups) {
        if (group.getName().equalsIgnoreCase(hostGroup)) {
          groupFound = true;
          break;
        }
      }

      if (host != null && tagService == null) {
        List<ZabbixMonitoredHostResponseV24> hosts = zabAdapClientSetter.getMonitoredHostsZabbixV24(
            zone, serverType, ZabbixMethods.HOST.getzabbixMethod(), null, null, host, null);
        if (hosts.isEmpty()) {
          throw new NotFoundMonitoringException(
              "Wrong Parameters: " + host + "inserted or NO host avalaible for such parameters");
        }
        for (ZabbixGroup group : hosts.get(0).getGroups()) {
          if (group.getName().equalsIgnoreCase(hostGroup)) {
            groupFound = true;
            break;
          }
        }
        if (!groupFound) {
          throw new NotFoundMonitoringException("Host Selected does not belong to selected group");
        }
      } else if (tagService != null) {
        List<ZabbixMonitoredHostResponseV24> hosts = zabAdapClientSetter.getMonitoredHostsZabbixV24(
            zone, serverType, ZabbixMethods.HOST.getzabbixMethod(), null, null, null, tagService);
        if (hosts.isEmpty()) {
          throw new NotFoundMonitoringException("Wrong Parameters inserted or any host like: "
              + host + " avalaible for such parameters");
        }

        if (!hosts.get(0).getGroups().get(0).getName().equalsIgnoreCase(hostGroup)) {
          throw new NotFoundMonitoringException("Host Selected: "
              + hosts.get(0).getGroups().get(0).getName() + " does not belong to selected group");
        }
      }

      if (groupFound) {
        return true;
      } else {
        throw new NotFoundMonitoringException(
            ZabbixConstant.WRONGGROUPNAME + serverType + ": " + hostGroup);
      }
    } catch (ZabbixException ze) {
      throw handleException(ze);
    }
  }

}