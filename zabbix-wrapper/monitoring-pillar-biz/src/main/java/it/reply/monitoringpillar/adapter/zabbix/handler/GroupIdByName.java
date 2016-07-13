package it.reply.monitoringpillar.adapter.zabbix.handler;

import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.NotFoundZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * 
 * @author m.grandolfo This class retrieves Host group IDs
 */
@Stateless
public class GroupIdByName implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject
  private ZabbixAdapterClientSetter<?> zabAdapSetter;

  /**
   * Get zabbix groups Ids.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostGroupName
   *          name
   * @param groups
   *          name
   * @return id
   * @throws ZabbixException
   *           wrapper
   */
  public String getGroupIDsintoZabbix(String zone, String serverType, String hostGroupName,
      List<ZabbixHostGroupResponse> groups) throws ZabbixException {

    if (groups == null || groups.isEmpty()) {
      List<ZabbixHostGroupResponse> hostGroups = zabAdapSetter.getHostGroupsService(zone,
          serverType, ZabbixMethods.HOSTGROUP.getzabbixMethod(), null, null, null);

      for (ZabbixHostGroupResponse zabbixHostGroupResponse : hostGroups) {

        if (zabbixHostGroupResponse.getName().equalsIgnoreCase(hostGroupName)) {
          return zabbixHostGroupResponse.getGroupid();
        }
      }
      throw new NotFoundZabbixException("Group [" + hostGroupName + "] not exists");

      // In case the List Has been retrieved from calling object (in order to
      // save number of calls)
    } else {
      for (ZabbixHostGroupResponse zabbixHostGroupResponse : groups) {
        if (zabbixHostGroupResponse.getName().equalsIgnoreCase(hostGroupName)) {
          return zabbixHostGroupResponse.getGroupid();
        }
      }
      throw new NotFoundZabbixException("Group [" + hostGroupName + "] not exists");
    }
  }
}