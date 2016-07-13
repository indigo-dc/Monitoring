package it.reply.monitoringpillar.adapter;

import it.reply.monitoringpillar.adapter.zabbix.ZabbixConstant;
import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.AuthenticationZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.DuplicateResourceZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.IllegalArgumentZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.GroupCheck;
import it.reply.monitoringpillar.adapter.zabbix.handler.GroupIdByName;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.FilterTimeRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV24;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponseV24;
import it.reply.monitoringpillar.domain.exception.DuplicateResourceMonitoringException;
import it.reply.monitoringpillar.domain.exception.IllegalArgumentMonitoringException;
import it.reply.monitoringpillar.domain.exception.MonitoringException;
import it.reply.monitoringpillar.domain.exception.NotFoundMonitoringException;
import it.reply.monitoringpillar.wrapper.zabbix.iaas.WrapperIaaSBean;
import it.reply.monitoringpillar.wrapper.zabbix.paas.WrapperPaasBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public abstract class MonitoringAdapteeZabbix {

  @Inject
  private WrapperIaaSBean wrapperIaaS;

  @Inject
  private WrapperPaasBean wrapperPaas;

  @Inject
  private GroupIdByName zabGroupId;

  @EJB
  private ZabbixAdapterClientSetter<?> zabAdapClientSetter;

  @EJB
  private GroupCheck checkGroup;

  /**
   * Convert a Zabbix Exception into a Monitoring Exception.
   * 
   * @param e
   *          ZabbixException
   * @return MonitorinException wrapping the generic one
   * @throws MonitoringException
   *           wrapping the generic one
   */
  protected MonitoringException handleException(ZabbixException me) {

    if (me instanceof IllegalArgumentZabbixException) {
      return new IllegalArgumentMonitoringException(me.getMessage(), me);
    }
    if (me instanceof AuthenticationZabbixException) {
      return new MonitoringException(me.getMessage(), me);
    }
    if (me instanceof DuplicateResourceZabbixException) {
      return new DuplicateResourceMonitoringException(me.getMessage(), me);
    }

    return new MonitoringException(me.getMessage(), me);
  }

  /*****************************************
   * WRAPPER GENERIC FOR IAAS ZABBIX SERVER.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param iaasgroupName
   *          name
   * @return Wrapper Response Iaas
   * @throws MonitoringException
   *           wrappeing the generic one
   */
  public MonitoringWrappedResponseIaas getOverallIaas(String zone, String serverType,
      String iaasgroupName, String iaashostName) throws MonitoringException {

    try {

      String groupId = null;
      // Get IaaS Server Credentials
      List<ZabbixHostGroupResponse> groupsInZabbixIaas = new ArrayList<>();

      List<ZabbixHostGroupResponse> groupsUseful = new ArrayList<>();
      List<ZabbixMonitoredHostsResponse> onlyUsefulHost = new ArrayList<>();

      if (iaasgroupName != null
          && checkGroup.isGroupPresent(zone, serverType, iaasgroupName, iaashostName, null)) {
        groupsInZabbixIaas = zabAdapClientSetter.getHostGroupsService(zone, serverType,
            ZabbixMethods.HOSTGROUP.getzabbixMethod(), null, null, null);
      }
      for (ZabbixHostGroupResponse zabbixHostGroupResponse : groupsInZabbixIaas) {

        if (zabbixHostGroupResponse.getName().equalsIgnoreCase(iaasgroupName)) {
          groupId = zabbixHostGroupResponse.getGroupid();
          groupsUseful.add(zabbixHostGroupResponse);
          break;
        }
      }
      if (groupId == null) {
        throw new NotFoundMonitoringException(
            "Error this hostgroup is not listed in zabbix server");
      }
      List<ZabbixMonitoredHostsResponse> hostsinZabbixIaas = new ArrayList<>();
      hostsinZabbixIaas = zabAdapClientSetter.getHostsService(zone, serverType,
          ZabbixMethods.HOST.getzabbixMethod(), null, groupId, null, null);

      if (iaashostName != null) {

        for (ZabbixMonitoredHostsResponse host : hostsinZabbixIaas) {
          if (iaashostName.equalsIgnoreCase(host.getName())) {

            onlyUsefulHost.add(host);
            hostsinZabbixIaas = onlyUsefulHost;
          }
        }
      }

      String hostId = null;
      MonitoringWrappedResponseIaas result = wrapperIaaS.getWrappedIaas(zone, serverType,
          iaasgroupName, iaashostName, groupsUseful, groupId, hostsinZabbixIaas, hostId);
      return result;
    } catch (ZabbixException ze) {
      throw handleException(ze);
    }
  }

  /****************************************************
   * WRAPPER GENERIC PAAS FOR ZABBIX METRICS AND WATCHER.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param group
   *          name
   * @param hostuuid
   *          uuid
   * @param serviceCategory
   *          name
   * @param tagService
   *          service Id
   * @param atomicServiceId
   *          name
   * @param metric
   *          name
   * @param triggersId
   *          name
   * @param history
   *          parameter
   * @param requestTime
   *          object @see FilterTimeRequest
   * @return MonitoringWrappedResponsePaas @see MonitoringWrappedResponsePaas
   * @throws MonitoringException
   *           wrapping the generic Exception
   */

  public MonitoringWrappedResponsePaas getOverallPaaS(String zone, String serverType, String group,
      String hostuuid, String serviceCategory, String tagService, List<String> atomicServiceId,
      String metric, List<String> triggersId, String history, FilterTimeRequest requestTime)
          throws MonitoringException {

    try {

      MonitoringWrappedResponsePaas wrappedResult = new MonitoringWrappedResponsePaas();

      String hostId = null;
      String groupId = null;
      String groupName = null;
      List<ZabbixMonitoredHostResponseV24> hostsinZabbixPaaS = new ArrayList<>();
      List<ZabbixHostGroupResponse> groupsInZabbixPaaS = new ArrayList<>();
      List<ZabbixItemResponse> items = new ArrayList<>();
      List<ZabbixTemplateResponseV24> templatesExtended = new ArrayList<>();
      Map<ZabbixMonitoredHostResponseV24, List<ZabbixTemplateResponseV24>> hostsByTagMap =
          new HashMap<>();
      Map<ZabbixTemplateResponseV24, List<ZabbixItemResponse>> itemsByTemplate = new HashMap<>();

      if (group != null
          && (checkGroup.isGroupPresent(zone, serverType, group, hostuuid, tagService))) {
        ;
      }

      /*****************************
       * ONLY TAG ID PASSED FROM API
       ****************************/
      if (tagService != null && hostuuid == null && group != null) {
        // Get the list of EXTENDED hosts (templates and metrics
        // associated to em)
        hostsinZabbixPaaS = zabAdapClientSetter.getMonitoredHostsZabbixV24(zone, serverType,
            ZabbixMethods.HOST.getzabbixMethod(), null, null, null, tagService);

        if (hostsinZabbixPaaS.isEmpty()) {
          throw new NotFoundMonitoringException(
              "Wrong Service ID Inserted: " + tagService + "Not Existing Into Platform");
        }
        for (ZabbixMonitoredHostResponseV24 host : hostsinZabbixPaaS) {
          hostId = host.getHostid();
          // GET the list of items
          items = host.getItems();
          // GET the list of templates
          templatesExtended = zabAdapClientSetter.getTemplatesExtendedService(zone, serverType,
              hostId, ZabbixMethods.TEMPLATE.getzabbixMethod());
          hostsByTagMap.put(host, templatesExtended);
          for (ZabbixTemplateResponseV24 template : templatesExtended) {

            // GET ONLY USEFUL ITEMS REALLY ASSOCIATED TO TEMPLATES
            List<ZabbixItemResponse> usefulItems = new ArrayList<>();
            for (ZabbixItemResponse itemFromExtHost : items) {
              for (ZabbixItemResponse item : template.getItems()) {
                if (item.getName().equals(itemFromExtHost.getName())) {
                  usefulItems.add(itemFromExtHost);
                }
              }
            }
            itemsByTemplate.put(template, usefulItems);
          }
        }
        groupsInZabbixPaaS = zabAdapClientSetter.getHostGroupsService(zone, serverType,
            ZabbixMethods.HOSTGROUP.getzabbixMethod(), groupId, null, hostId);

        if (groupsInZabbixPaaS.isEmpty()) {
          throw new NotFoundMonitoringException(ZabbixConstant.WRONGGROUPNAME + groupId);
        }
        groupId = groupsInZabbixPaaS.get(0).getGroupid();
        groupName = groupsInZabbixPaaS.get(0).getName();

        /**********************************
         * ONLY HOST UUID OR NAME SPECIFIED
         **********************************/
      } else if (hostuuid != null && group != null && tagService == null) {
        hostsinZabbixPaaS = zabAdapClientSetter.getMonitoredHostsZabbixV24(zone, serverType,
            ZabbixMethods.HOST.getzabbixMethod(), null, null, hostuuid, null);
        if (hostsinZabbixPaaS.isEmpty()) {
          throw new NotFoundMonitoringException(ZabbixConstant.WRONGHOSTNAME + hostuuid);
        }
        for (ZabbixMonitoredHostResponseV24 host : hostsinZabbixPaaS) {
          hostId = host.getHostid();
          // GET the list of items
          items = host.getItems();
          // GET the list of templates EXTENDED (useful for cycling on
          // metrics associated to em.. From extended host answer
          // couldn't
          // know it)
          templatesExtended = zabAdapClientSetter.getTemplatesExtendedService(zone, serverType,
              hostId, ZabbixMethods.TEMPLATE.getzabbixMethod());

          hostsByTagMap.put(host, templatesExtended);
          for (ZabbixTemplateResponseV24 template : templatesExtended) {

            // GET ONLY USEFUL ITEMS REALLY ASSOCIATED TO TEMPLATES
            List<ZabbixItemResponse> usefulItems = new ArrayList<>();
            for (ZabbixItemResponse itemFromExtHost : items) {
              for (ZabbixItemResponse item : template.getItems()) {
                if (item.getName().equals(itemFromExtHost.getName())) {
                  usefulItems.add(itemFromExtHost);
                }
              }
            }
            itemsByTemplate.put(template, usefulItems);
          }
        }
        groupsInZabbixPaaS = zabAdapClientSetter.getHostGroupsService(zone, serverType,
            ZabbixMethods.HOSTGROUP.getzabbixMethod(), groupId, null, hostId);
        if (groupsInZabbixPaaS.isEmpty()) {
          throw new NotFoundMonitoringException(ZabbixConstant.WRONGGROUPNAME + groupId);
        }
        groupId = groupsInZabbixPaaS.get(0).getGroupid();
        groupName = groupsInZabbixPaaS.get(0).getName();

        /**************************
         * ONLY GROUP SPECIFIED
         **************************/
      } else if (group != null && hostuuid == null && tagService == null) {
        List<ZabbixHostGroupResponse> groupsuseful = new ArrayList<>();
        groupName = group;
        List<ZabbixHostGroupResponse> groupsInZabbixPaasOverall =
            zabAdapClientSetter.getHostGroupsService(zone, serverType,
                ZabbixMethods.HOSTGROUP.getzabbixMethod(), null, null, null);
        if (groupsInZabbixPaasOverall.isEmpty()) {
          throw new NotFoundMonitoringException(ZabbixConstant.WRONGGROUPNAME + groupId);
        }
        groupId = zabGroupId.getGroupIDsintoZabbix(zone, serverType, groupName, groupsInZabbixPaaS);
        for (ZabbixHostGroupResponse groupInfo : groupsInZabbixPaasOverall) {
          if (groupInfo.getName().equalsIgnoreCase(groupName)) {
            groupsuseful.add(groupInfo);
            groupsInZabbixPaaS = groupsuseful;
            break;
          }
        }
        if (groupsuseful.isEmpty()) {
          throw new NotFoundMonitoringException(ZabbixConstant.WRONGGROUPNAME + groupId);
        }
        hostsinZabbixPaaS = zabAdapClientSetter.getMonitoredHostsZabbixV24(zone, serverType,
            ZabbixMethods.HOST.getzabbixMethod(), null, groupId, null, null);
      }

      wrappedResult = wrapperPaas.getWrappedPaas(zone, serverType, groupName, hostuuid,
          serviceCategory, tagService, atomicServiceId, metric, triggersId, history, requestTime,
          groupsInZabbixPaaS, groupId, hostsinZabbixPaaS, hostId, items, templatesExtended,
          hostsByTagMap, itemsByTemplate);

      return wrappedResult;
    } catch (ZabbixException ze) {
      throw handleException(ze);
    }
  }

}