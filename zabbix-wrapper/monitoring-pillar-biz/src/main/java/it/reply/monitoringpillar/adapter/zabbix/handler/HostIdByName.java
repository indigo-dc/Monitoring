package it.reply.monitoringpillar.adapter.zabbix.handler;

import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.NotFoundZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV24;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class HostIdByName implements Serializable {

  private static final long serialVersionUId = 1L;
  private static final String ERROR_Msg =
      "Wrong host, group, tag inserted or not existing in monitoring platform";

  @Inject
  private ZabbixAdapterClientSetter<?> zabAdapSetter;

  /**
   * Get host Id.
   */
  public String getHostId(String zone, String serverType, String hostgroupName, String vmuuid,
      ArrayList<ZabbixMonitoredHostsResponse> hosts) throws ZabbixException {

    if (hosts == null || vmuuid == null) {
      ArrayList<ZabbixMonitoredHostsResponse> hostinfo =
          new ArrayList<ZabbixMonitoredHostsResponse>();

      hostinfo = (ArrayList<ZabbixMonitoredHostsResponse>) zabAdapSetter.getHostsService(zone,
          serverType, ZabbixMethods.HOST.getzabbixMethod(), null, null, null, null);
      for (int i = 0; i < hostinfo.size(); i++) {
        String hostIdfromPillar = hostinfo.get(i).getHostid();
        String hostname = hostinfo.get(i).getName();
        if (vmuuid.equalsIgnoreCase(hostname)) {
          return hostIdfromPillar;
        }
      }
      throw new NotFoundZabbixException(ERROR_Msg);
    } else {
      for (int i = 0; i < hosts.size(); i++) {
        String hostIdfromPillar = hosts.get(i).getHostid();
        String hostname = hosts.get(i).getName();
        if (vmuuid.equalsIgnoreCase(hostname)) {
          return hostIdfromPillar;
        }
      }
      throw new NotFoundZabbixException(ERROR_Msg);
    }
  }

  /**
   * Get host list.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostgroupName
   *          name
   * @param vmuuid
   *          uuid
   * @param tagService
   *          is
   * @param hosts
   *          list
   * @return list of hosts
   * @throws ZabbixException
   *           wrapper
   */
  public String getHostIdV24(String zone, String serverType, String hostgroupName, String vmuuid,
      String tagService, List<ZabbixMonitoredHostResponseV24> hosts) throws ZabbixException {

    if (hosts == null || vmuuid == null) {
      ArrayList<ZabbixMonitoredHostsResponse> hostinfo =
          new ArrayList<ZabbixMonitoredHostsResponse>();

      hostinfo = (ArrayList<ZabbixMonitoredHostsResponse>) zabAdapSetter.getHostsService(zone,
          serverType, ZabbixMethods.HOST.getzabbixMethod(), null, null, null, null);

      for (int i = 0; i < hostinfo.size(); i++) {
        String hostIdfromPillar = hostinfo.get(i).getHostid();
        String hostname = hostinfo.get(i).getName();
        if (vmuuid.equalsIgnoreCase(hostname)) {
          return hostIdfromPillar;
        }
      }
      throw new NotFoundZabbixException(ERROR_Msg);
    } else {
      for (int i = 0; i < hosts.size(); i++) {
        String hostIdfromPillar = hosts.get(i).getHostid();
        String hostname = hosts.get(i).getName();
        if (vmuuid.equalsIgnoreCase(hostname)) {
          return hostIdfromPillar;
        }
      }
      throw new NotFoundZabbixException(ERROR_Msg);
    }
  }

}