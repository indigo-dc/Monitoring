package it.reply.monitoringpillar.adapter.zabbix.clientbuilder;

import it.reply.monitoringpillar.adapter.zabbix.ZabbixBase;
import it.reply.monitoringpillar.adapter.zabbix.ZabbixConstant;
import it.reply.monitoringpillar.adapter.zabbix.ZabbixConstant.MetricAction;
import it.reply.monitoringpillar.adapter.zabbix.ZabbixHelperBean;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.GroupIdByName;
import it.reply.monitoringpillar.adapter.zabbix.handler.TemplateIdByName;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.config.Configuration;
import it.reply.monitoringpillar.domain.dsl.monitoring.MonitoringConstant;
import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request.Port;
import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.response.MonitoringVmCredentialsResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.FilterTimeRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.HostGroupParamRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.HostIdMassUpdate;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.JsonRpcDeleteHostRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.JsonRpcRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.Macros;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.SearchInventory;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.TriggerParamRequestByGroup;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.UserMediaRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixCreateUserRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixExtendedParamHost;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixFilterRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixGetActionRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixGetUserRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamCreateHostRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamCreateProxyRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamGroupIntoHostCreateRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamHistoryRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamHostGroupRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamInterfaceIntoHostCreateRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamItemRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTemplateRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTrendRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTriggerRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamUpdate;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixSearchKeyRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixUpdateProxyRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.HostGroupResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.Inventory;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.UpdatedItemsResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.Usrgrp;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixCreateUserResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixGetActionResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixGetUserResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV24;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponseV24;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixUpdateProxyResponse;
import it.reply.monitoringpillar.utility.TimestampMonitoring;
import it.reply.monitoringpillar.utils.datetime.FilterTimeRequestHandlerMonitoring;
import it.reply.utils.web.ws.rest.apiclients.zabbix.ZabbixApiClient;
import it.reply.utils.web.ws.rest.apiclients.zabbix.exception.ZabbixClientException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.naming.NameNotFoundException;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ZabbixAdapterClientSetter<T> extends ZabbixBase implements Serializable {

  private static final long serialVersionUID = 1L;

  @EJB
  private GroupIdByName groupid;

  @EJB
  private TemplateIdByName idv2;

  @Inject
  private ZabbixHelperBean zabbixHelper;

  @Inject
  private Configuration config;

  /************************
   * CREATE HOST IN ZABBIX.
   ************************/

  /**
   * Gets host's created ID in PrismaMetrics.
   */
  public MonitoringVmCredentialsResponse createHostService(String zone, String serverType,
      String hostGroup, String hostName, String vmuuid, String vmIp, String serviceCategory,
      String serviceTag, List<String> services, Boolean activeMode, String zabbixMethod,
      List<Port> ports, String proxyapi) throws ZabbixException, NameNotFoundException {
    try {

      JsonRpcRequest<ZabbixParamCreateHostRequest> requestCreate =
          new JsonRpcRequest<ZabbixParamCreateHostRequest>();
      requestCreate.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      requestCreate.setMethod(zabbixMethod);

      requestCreate.setAuth(zabbixHelper.getZabbixToken(zone, serverType.toString()));

      Macros macro = new Macros();
      List<Macros> macros = new ArrayList<>();
      if (hostName.toLowerCase()
          .contains(MonitoringConstant.ATOMIC_SERVICE_AGGREGATOR.toLowerCase())) {
        macro.setMacro("{$GROUPID}");
        macro.setValue(hostGroup);
        macros.add(macro);
      } else {
        macro = new Macros();
        macro.setMacro("{$UUID}");
        macro.setValue(vmuuid);
        macros.add(macro);
        if (ports != null && !ports.isEmpty()) {
          for (Port port : ports) {
            macro = new Macros();
            macro.setValue(port.getValue());
            macro.setMacro("{$" + getPortName(zone, serverType, port) + "}");
            macros.add(macro);
          }
          if (ports.isEmpty()) {
            throw new ZabbixException("MACRO Name not Listed into Property");
          }
        }
      }
      ZabbixParamCreateHostRequest hostCreationParameters = new ZabbixParamCreateHostRequest();
      hostCreationParameters.setMacros(macros);

      hostCreationParameters.setHost(hostName);
      String proxyName = null;
      if (config.getMonitoringConfigurations().getOptions().isMultipleServers()
          && config.getMonitoringConfigurations().getOptions().isProxyArchitecture()) {
        // Proxy architecture
        // In case distributed Architecture has been applied for the
        // particular zone, then, make sure to get proxy-<workgroupIg>
        proxyName = config.getProxyNames(zone, serverType, Configuration.getWorkgroupId(hostGroup));
        String proxyId = zabbixHelper.getProxyId(zone, serverType, proxyName);
        hostCreationParameters.setProxy_hostid(proxyId);
      } else if (proxyapi != null
          && config.getMonitoringConfigurations().getOptions().isProxyArchitecture()) {
        proxyName = config.getMonitoringZones().getZone(zone).getServer(serverType)
            .getProxyName(proxyapi).getNameTemplate();
        String proxyId = zabbixHelper.getProxyId(zone, serverType, proxyName);
        hostCreationParameters.setProxy_hostid(proxyId);
      }

      ZabbixParamInterfaceIntoHostCreateRequest interfaceParameter =
          new ZabbixParamInterfaceIntoHostCreateRequest();
      interfaceParameter.setMain(1);
      interfaceParameter.setIp(vmIp);
      interfaceParameter.setDns("");
      interfaceParameter.setType(1);
      interfaceParameter.setPort(ZabbixConstant.ZABBIX_PORT_AGENT);
      interfaceParameter.setUseip(1);

      ArrayList<ZabbixParamInterfaceIntoHostCreateRequest> interfaceArray =
          new ArrayList<ZabbixParamInterfaceIntoHostCreateRequest>();
      interfaceArray.add(interfaceParameter);
      String groupId = null;

      if (services == null && config.getMonitoringConfigurations().getOptions().isMultipleServers()
          && config.getMonitoringConfigurations().getOptions().isIaasMonitoring()) {
        String ceilometer = "";
        services = new ArrayList<>();
        services.add(ceilometer);
      }

      for (int i = 0; i < services.size(); i++) {
        groupId = groupid.getGroupIDsintoZabbix(zone, serverType.toString(), hostGroup, null);
      }
      ZabbixParamGroupIntoHostCreateRequest grouprequest =
          new ZabbixParamGroupIntoHostCreateRequest();
      grouprequest.setGroupid(groupId);
      ArrayList<ZabbixParamGroupIntoHostCreateRequest> groupParamArray =
          new ArrayList<ZabbixParamGroupIntoHostCreateRequest>();
      groupParamArray.add(grouprequest);

      hostCreationParameters.setGroups(groupParamArray);
      hostCreationParameters.setInterfaces(interfaceArray);

      hostCreationParameters
          .setTemplates(idv2.getTemplateId(zone, serverType.toString(), services, activeMode));

      Inventory inventory = new Inventory();
      inventory.setTag(serviceTag);
      inventory.setType(serviceCategory);
      hostCreationParameters.setInventory(inventory);

      requestCreate.setParams(hostCreationParameters);
      requestCreate.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.createHostClient(requestCreate);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }

  }

  private String getPortName(String zone, String serverType, Port port)
      throws ZabbixClientException {

    return config.getExistingPaasPort(zone, serverType, MonitoringConstant.ATOMIC_SERVICE_PAAS,
        port.getPortName());
  }

  /***********************
   * DELETE HOST IN ZABBIX.
   ***********************/

  /**
   * It removes the monitored host.
   * 
   * @return the host's ID
   */
  public MonitoringVmCredentialsResponse deleteHostService(String url, String token, String hostid,
      String zabbixMethod) throws ZabbixException {
    try {

      JsonRpcDeleteHostRequest requestDelete = new JsonRpcDeleteHostRequest();
      requestDelete.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      requestDelete.setMethod(zabbixMethod);
      ArrayList<String> host2delete = new ArrayList<>();
      host2delete.add(hostid);
      requestDelete.setParams(host2delete);

      requestDelete.setAuth(token);

      requestDelete.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(url);
      return zabClient.deleteHostClient(requestDelete);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /*******************
   * DELETE HOSTGROUP.
   */
  public HostGroupResponse deleteHostGroupService(String zone, String serverType,
      String hostGroupName, String zabbixMethod) throws ZabbixException {
    try {

      JsonRpcDeleteHostRequest requestDelete = new JsonRpcDeleteHostRequest();

      requestDelete.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      requestDelete.setMethod(zabbixMethod);
      ArrayList<String> host2delete = new ArrayList<>();
      host2delete.add(hostGroupName);
      requestDelete.setParams(host2delete);

      requestDelete.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      requestDelete.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.deleteHostGroupClient(requestDelete);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * DELETE PROXY.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param proxyId
   *          id
   * @param zabbixMethod
   *          method
   * @return id of proxy
   * @throws ZabbixException
   *           wrapper
   */
  public ZabbixUpdateProxyResponse deleteProxyService(String zone, String serverType,
      String proxyId, String zabbixMethod) throws ZabbixException {
    try {
      JsonRpcDeleteHostRequest requestDelete = new JsonRpcDeleteHostRequest();

      requestDelete.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      requestDelete.setMethod(zabbixMethod);
      ArrayList<String> proxy2delete = new ArrayList<>();
      proxy2delete.add(proxyId);
      requestDelete.setParams(proxy2delete);

      requestDelete.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      requestDelete.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.deleteProxyClient(requestDelete);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /********************
   * CREATE HOSTGROUP.
   ********************/
  public HostGroupResponse createHostGroupService(String url, String token, String hostGroupName,
      String zabbixMethod) throws ZabbixException {

    try {
      JsonRpcRequest<HostGroupParamRequest> request = new JsonRpcRequest<>();
      HostGroupParamRequest hostGroupParamRequest = new HostGroupParamRequest();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);

      hostGroupParamRequest.setName(hostGroupName);
      request.setParams(hostGroupParamRequest);
      request.setAuth(token);
      request.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(url);
      return zabClient.createHostGroupClient(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /********************
   * UPDATE HOSTGROUP.
   ********************/
  public HostGroupResponse updateHostGroupService(String zone, String serverType,
      String hostGroupName, String groupId, String zabbixMethod) throws ZabbixException {
    try {

      JsonRpcRequest<HostGroupParamRequest> request = new JsonRpcRequest<>();
      HostGroupParamRequest hostGroupParamRequest = new HostGroupParamRequest();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);

      hostGroupParamRequest.setName(hostGroupName);
      hostGroupParamRequest.setGroupid(groupId);
      request.setParams(hostGroupParamRequest);
      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));
      request.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.updateGroupClient(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**************
   * UPDATE PROXY.
   **************/
  @Deprecated
  public ZabbixUpdateProxyResponse updateProxyService(String zone, String serverType,
      String proxyId, String hostId, String zabbixMethod) throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixUpdateProxyRequest> request = new JsonRpcRequest<>();

      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);

      List<String> hostids = new ArrayList<>();
      hostids.add(hostId);
      ZabbixUpdateProxyRequest paramRequest = new ZabbixUpdateProxyRequest();
      paramRequest.setHosts(hostids);
      paramRequest.setProxyid(proxyId);
      request.setParams(paramRequest);
      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));
      request.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.updateProxyClient(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /********************
   * CREATE PROXY.
   */
  public ZabbixUpdateProxyResponse createProxyService(String zone, String url, String token,
      String serverType, String hostGroupNameId, String proxyName, String zabbixMethod)
          throws ZabbixException {
    try {
      String checkedUrl = null;
      String checkedToken = null;
      if (url != null && token != null) {
        checkedUrl = url;
        checkedToken = token;
      } else {
        checkedUrl = config.getZabbixServerUrl(zone, serverType);
        checkedToken = zabbixHelper.getZabbixToken(zone, serverType);
      }

      JsonRpcRequest<ZabbixParamCreateProxyRequest> request = new JsonRpcRequest<>();
      ZabbixParamCreateProxyRequest paramRequest = new ZabbixParamCreateProxyRequest();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);

      if (url == null && token == null) {
        paramRequest.setHost(proxyName);
      } else {
        paramRequest.setHost(hostGroupNameId.toLowerCase()
            .replace(MonitoringConstant.WG_PREFIX.toLowerCase(), MonitoringConstant.PROXY_PREFIX));
      }
      paramRequest.setStatus(ZabbixConstant.PROXY_ACTIVE_PROP);
      request.setParams(paramRequest);
      request.setAuth(checkedToken);
      request.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(checkedUrl);
      return zabClient.createProxyClient(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /*******************************
   * UPDATE HOST/METRIC IN ZABBIX.
   *******************************/

  /**
   * Method useful for enabling/disabling the host.
   */
  public MonitoringVmCredentialsResponse updateHostService(String zone, String serverType,
      String hostid, String itemid, String zabbixMethod, MetricAction update)
          throws ZabbixException {
    try {

      HostIdMassUpdate host2update = new HostIdMassUpdate();
      JsonRpcRequest<ZabbixParamUpdate> requestUpdate = new JsonRpcRequest<ZabbixParamUpdate>();
      requestUpdate.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      requestUpdate.setMethod(zabbixMethod);
      ZabbixParamUpdate paramUpdate = new ZabbixParamUpdate();

      ArrayList<HostIdMassUpdate> hosts2update = new ArrayList<>();

      host2update.setHostid(hostid);
      hosts2update.add(host2update);
      paramUpdate.setHosts(hosts2update);
      paramUpdate.setStatus(update.getStatus());

      requestUpdate.setAuth(zabbixHelper.getZabbixToken(zone, serverType));
      requestUpdate.setId(ZabbixConstant.ID);
      requestUpdate.setParams(paramUpdate);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.updateHostClient(requestUpdate);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * Updates a specific metric.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostid
   *          id
   * @param itemid
   *          id
   * @param zabbixMethod
   *          method
   * @param update
   *          condition
   * @return pojo
   * @throws ZabbixException
   *           wrapper
   */
  public UpdatedItemsResponse updateItemService(String zone, String serverType, String hostid,
      String itemid, String zabbixMethod, MetricAction update) throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixParamUpdate> requestUpdate = new JsonRpcRequest<ZabbixParamUpdate>();
      requestUpdate.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      requestUpdate.setMethod(zabbixMethod);
      ZabbixParamUpdate paramUpdate = new ZabbixParamUpdate();

      paramUpdate.setItemid(itemid);
      paramUpdate.setStatus(update.getStatus());

      requestUpdate.setAuth(zabbixHelper.getZabbixToken(zone, serverType));
      requestUpdate.setId(ZabbixConstant.ID);
      requestUpdate.setParams(paramUpdate);

      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.updateItemClient(requestUpdate);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * Get Monitored HOSTS LIST in Zabbix Platform useful for returning the host.
   */
  public List<ZabbixMonitoredHostsResponse> getHostsService(String zone, String serverType,
      String zabbixMethod, String hostid, String groupids, String filterByHostName,
      String triggerId) throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixParamRequest> request = new JsonRpcRequest<ZabbixParamRequest>();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);

      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixParamRequest parametersHosts = new ZabbixParamRequest();
      parametersHosts.setOutput(ZabbixConstant.EXTEND);

      if (!(groupids == null)) {
        parametersHosts.setGroupids(groupids);
      }

      if (!(hostid == null)) {
        parametersHosts.setHostids(hostid);
      }
      ZabbixFilterRequest filter = new ZabbixFilterRequest();
      if (filterByHostName != null) {
        filter.setHost(filterByHostName);
      }
      if (triggerId != null) {
        hostid = null;
        parametersHosts.setTriggerids(triggerId);
      }
      parametersHosts.setFilter(filter);
      parametersHosts.setSelectInventory("ZabbixConstant.EXTEND");
      request.setParams(parametersHosts);
      request.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.getHostsClient(request);
    } catch (ZabbixClientException ze) {

      throw handleException(ze);
    }
  }

  /**
   * Get hosts service.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param zabbixMethod
   *          method
   * @param hostids
   *          id
   * @param groupids
   *          id
   * @param filterByHostName
   *          name
   * @param tagId
   *          id
   * @return list host responses
   * @throws ZabbixException
   *           wrapper
   */
  public List<ZabbixMonitoredHostsResponse> getMonitoredHostsExtendedService(String zone,
      String serverType, String zabbixMethod, String hostids, String groupids,
      String filterByHostName, String tagId) throws ZabbixException {
    try {
      JsonRpcRequest<ZabbixExtendedParamHost> request =
          new JsonRpcRequest<ZabbixExtendedParamHost>();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);

      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixExtendedParamHost parametersHosts = new ZabbixExtendedParamHost();
      parametersHosts.setOutput(ZabbixConstant.EXTEND);

      if (!(groupids == null)) {
        parametersHosts.setGroupids(groupids);
      }

      if (!(hostids == null)) {
        parametersHosts.setHostids(hostids);
      }
      ZabbixFilterRequest filter = new ZabbixFilterRequest();
      if (filterByHostName != null) {
        filter.setHost(filterByHostName);
      }
      SearchInventory searchInventory = new SearchInventory();
      searchInventory.setTag(tagId);
      parametersHosts.setSearchInventory(searchInventory);
      parametersHosts.setSearchWildcardsEnabled(true);
      parametersHosts.setFilter(filter);
      parametersHosts.setSelectInventory(ZabbixConstant.EXTEND);
      parametersHosts.setSelectItems(null);
      parametersHosts.setSelectParentTemplates(null);
      request.setParams(parametersHosts);
      request.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.getHostsExtended(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * Get Monitored HOSTS LIST in Zabbix 2.4 Metrics/Watcher.
   * 
   */
  public List<ZabbixMonitoredHostResponseV24> getMonitoredHostsZabbixV24(String zone,
      String serverType, String zabbixMethod, String hostids, String groupids,
      String filterByHostName, String tagId) throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixExtendedParamHost> request =
          new JsonRpcRequest<ZabbixExtendedParamHost>();

      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);

      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixExtendedParamHost parametersHosts = new ZabbixExtendedParamHost();
      parametersHosts.setOutput(ZabbixConstant.EXTEND);
      parametersHosts.setSelectGroups(ZabbixConstant.EXTEND);

      if (!(groupids == null)) {
        parametersHosts.setGroupids(groupids);
      }

      if (!(hostids == null)) {
        parametersHosts.setHostids(hostids);
      }
      ZabbixFilterRequest filter = new ZabbixFilterRequest();
      if (filterByHostName != null) {
        filter.setHost(filterByHostName);
      }
      SearchInventory searchInventory = new SearchInventory();
      searchInventory.setTag(tagId);
      parametersHosts.setSearchInventory(searchInventory);
      parametersHosts.setSearchWildcardsEnabled(true);
      parametersHosts.setFilter(filter);
      parametersHosts.setSelectInventory(ZabbixConstant.EXTEND);
      parametersHosts.setSelectItems(ZabbixConstant.EXTEND);
      parametersHosts.setSelectParentTemplates(ZabbixConstant.EXTEND);
      request.setParams(parametersHosts);
      request.setId(ZabbixConstant.ID);

      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.getHostsExtended2_4(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * Get TEMPLATE LIST into PrismaMetrics Server.
   */
  public List<ZabbixTemplateResponse> getTemplateService(String zone, String serverType,
      String zabbixMethod) throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixParamHostGroupRequest> request =
          new JsonRpcRequest<ZabbixParamHostGroupRequest>();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);

      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixParamHostGroupRequest parametersHosts = new ZabbixParamHostGroupRequest();
      parametersHosts.setOutput(ZabbixConstant.EXTEND);

      ZabbixFilterRequest filter = new ZabbixFilterRequest();
      parametersHosts.setFilter(filter);
      request.setParams(parametersHosts);
      request.setId(ZabbixConstant.ID);

      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return (zabClient).getTemplate(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * Get Zabbix Features USEFUL FOR MULTIPLE PURPOSES depending on method passed.
   */
  public List<ZabbixItemResponse> getZabbixFeatureMultiService(String zone, String serverType,
      String hostId, String templateIdtoAdapter, String zabbixMethod) throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixParamItemRequest> requestItem =
          new JsonRpcRequest<ZabbixParamItemRequest>();
      requestItem.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      requestItem.setMethod(zabbixMethod);

      requestItem.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixParamItemRequest params = new ZabbixParamItemRequest();
      params.setOutput(ZabbixConstant.EXTEND);
      params.setHostids(hostId);

      if (!(templateIdtoAdapter == null)) {
        params.setTemplateids(templateIdtoAdapter);
        params.setHostids(null);
      }
      ZabbixSearchKeyRequest key = new ZabbixSearchKeyRequest();
      key.setKey("");
      params.setSearch(key);
      requestItem.setParams(params);
      requestItem.setId(ZabbixConstant.ID);

      requestItem.setParams(params);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.getItemsClient(requestItem);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * GET TEMPLATES AND ASSOCIATED ITEMS.
   */
  public List<ZabbixTemplateResponseV24> getTemplatesExtendedService(String zone, String serverType,
      String hostId, String zabbixMethod) throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixParamTemplateRequest> requestTemplate = new JsonRpcRequest<>();

      requestTemplate.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      requestTemplate.setMethod(zabbixMethod);

      requestTemplate.setAuth(zabbixHelper.getZabbixToken(zone, serverType));
      ZabbixParamTemplateRequest paramTemplate = new ZabbixParamTemplateRequest();
      paramTemplate.setOutput(ZabbixConstant.EXTEND);
      paramTemplate.setHostids(hostId);

      paramTemplate.setSelectItems(ZabbixConstant.EXTEND);
      requestTemplate.setParams(paramTemplate);
      requestTemplate.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.getTemplatesClient(requestTemplate);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }

  }

  /**
   * Get LIST of METRICS for Specific host.
   */
  public List<ZabbixItemResponse> getItemsService(String zone, String serverType, String hostId,
      String templateIds, String metricIds, String triggerIds, String zabbixMethod,
      FilterTimeRequest requestTime) throws ZabbixException {

    return getItemHistoryService(zone, serverType, hostId, templateIds, metricIds, triggerIds,
        zabbixMethod, requestTime);
  }

  /**
   * GET HISTORY / EVENTS.
   */
  public List<ZabbixItemResponse> getItemHistoryService(String zone, String serverType,
      String hostId, String templateIds, String metricIds, String triggerIds, String zabbixMethod,
      FilterTimeRequest requestTime) throws ZabbixException {
    try {

      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      JsonRpcRequest<ZabbixParamItemRequest> requestItem =
          new JsonRpcRequest<ZabbixParamItemRequest>();

      requestItem.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      requestItem.setMethod(zabbixMethod);
      String token = zabbixHelper.getZabbixToken(zone, serverType);
      requestItem.setAuth(token);
      ZabbixParamItemRequest itemParameters = new ZabbixParamItemRequest();
      itemParameters = new ZabbixParamItemRequest();
      itemParameters.setOutput(ZabbixConstant.EXTEND);

      /***************************************
       * SETTING HISTORY or EVENTS in ZABBIX
       **************************************/
      if (zabbixMethod.equals(ZabbixMethods.HISTORY.getzabbixMethod())
          || zabbixMethod.equals(ZabbixMethods.EVENT.getzabbixMethod())) {
        int limit = 10;

        /*****************
         * EVENT CASE
         *****************/
        if (zabbixMethod.equals(ZabbixMethods.EVENT.getzabbixMethod())) {
          limit = 30;
          // TODO: understand which limit to set for history considering
          // that
          // the sample are taken every 30s
          // (quite a lot for returning history) if every 10mins, then 144
          // samples cover the entire day; 3 days-->432; 7days-->1008;
          // 30days-->4320; 90day-->12960 WITH ZABBIX 3.0 --> use Trend API
        } else if (requestTime != null) {
          limit = FilterTimeRequestHandlerMonitoring.determineHowManySamples(requestTime);
        }

        JsonRpcRequest<ZabbixParamHistoryRequest> request =
            new JsonRpcRequest<ZabbixParamHistoryRequest>();

        request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
        request.setMethod(zabbixMethod);
        request.setAuth(token);
        ZabbixParamHistoryRequest parameters4history = new ZabbixParamHistoryRequest();
        parameters4history.setOutput(ZabbixConstant.EXTEND);
        int history = ZabbixConstant.TEXT_HISTORY_TYPE;
        parameters4history.setHistory(history);
        parameters4history.setLimit(limit);
        /************************
         * TIME FILTER FOR EVENTS
         ************************/
        if (requestTime != null
            && (requestTime.getDateFrom() != null && requestTime.getDateTo() != null)) {
          parameters4history.setTimeFrom(
              TimestampMonitoring.getDateFromFormatter(requestTime.getDateFrom()).toString());
          parameters4history.setTimeTill(
              TimestampMonitoring.getDateToFormatter(requestTime.getDateTo()).toString());
        } else if (requestTime != null
            && (requestTime.getFrom() != null && requestTime.getTo() != null)) {
          parameters4history.setTimeFrom(requestTime.getFrom().toString());
          parameters4history.setTimeTill(requestTime.getTo().toString());
        }
        String sortfield = "clock";
        parameters4history.setSortfield(sortfield);
        String sortorder = "ASC"; // "DESC"
        parameters4history.setSortorder(sortorder);

        if (!(metricIds == null)) {
          parameters4history.setItemids(metricIds);
          itemParameters.setTemplateids(null);
          itemParameters.setHostids(null);
        } else {
          parameters4history.setHostids(hostId);
        }

        request.setParams(parameters4history);
        request.setId(ZabbixConstant.ID);
        List<ZabbixItemResponse> result = new ArrayList<>();

        result = zabClient.getHistoryClient(request);

        /*************
         * HISTORY CASE
         **************/
        if (result.size() == 0 && (zabbixMethod.equals(ZabbixMethods.HISTORY.getzabbixMethod()))) {
          history = ZabbixConstant.INT_HISTORY_TYPE;
          parameters4history.setHistory(history);

          /**************
           * FILTER TIME
           *************/
          if (requestTime != null) {
            if (requestTime != null
                && (requestTime.getFrom() == null || requestTime.getTo() == null)) {
              parameters4history.setTimeFrom(
                  TimestampMonitoring.getDateFromFormatter(requestTime.getDateFrom()).toString());
              parameters4history.setTimeTill(
                  TimestampMonitoring.getDateToFormatter(requestTime.getDateTo()).toString());
            } else if ((requestTime.getDateFrom() == null || requestTime.getDateTo() == null)) {
              parameters4history.setTimeFrom(requestTime.getFrom().toString());
              parameters4history.setTimeTill(requestTime.getTo().toString());
            }
          }
          result = zabClient.getHistoryClient(request);
          if (result.size() == 0) {
            history = ZabbixConstant.FLOAT_HISTORY_TYPE;
            parameters4history.setHistory(history);

            result = zabClient.getHistoryClient(request);
          }
        }
        return result;

        /***************************
         * SETTING ITEMS in ZABBIX
         ***************************/
      } else {

        itemParameters.setHostids(hostId);

        if (!(templateIds == null) && !(hostId == null)) {
          itemParameters.setTemplateids(null);
        } else if (!(templateIds == null)) {
          itemParameters.setTemplateids(templateIds);
          itemParameters.setHostids(null);
          // For getting specific metrics and history
        } else if (!(metricIds == null)) {
          itemParameters.setItemids(metricIds);
          itemParameters.setTemplateids(null);
          itemParameters.setHostids(null);
        }
        // This parameter has been exploited when requesting metrics
        // into
        // Event wrapper
        if (triggerIds != null) {
          itemParameters.setTriggerids(triggerIds);
        }
        // itemParameters.setTemplateids(templateIds);
        ZabbixSearchKeyRequest key = new ZabbixSearchKeyRequest();
        key = new ZabbixSearchKeyRequest();
        key.setKey("");

        itemParameters.setSearch(key);

        requestItem.setParams(itemParameters);
        requestItem.setId(ZabbixConstant.ID);

        return zabClient.getItemsClient(requestItem);
      }
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /*************************
   * GET TRENDS.
   *********************/
  public List<ZabbixItemResponse> getTrendService(String zone, String serverType, String hostId,
      String templateIds, String metricIds, String triggerIds, String zabbixMethod,
      FilterTimeRequest requestTime) throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixParamItemRequest> requestItem =
          new JsonRpcRequest<ZabbixParamItemRequest>();

      Long dateFrom = null;
      Long dateTill = null;

      if (requestTime != null) {
        dateFrom = TimestampMonitoring.getDateFromFormatter(requestTime.getDateFrom());
        dateTill = TimestampMonitoring.getDateToFormatter(requestTime.getDateTo());
      }

      requestItem.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      requestItem.setMethod(zabbixMethod);
      String token = zabbixHelper.getZabbixToken(zone, serverType);
      requestItem.setAuth(token);
      ZabbixParamItemRequest itemParameters = new ZabbixParamItemRequest();
      itemParameters = new ZabbixParamItemRequest();
      itemParameters.setOutput(ZabbixConstant.EXTEND);

      JsonRpcRequest<ZabbixParamTrendRequest> request =
          new JsonRpcRequest<ZabbixParamTrendRequest>();

      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);
      request.setAuth(token);
      ZabbixParamTrendRequest parameters4trend = new ZabbixParamTrendRequest();
      parameters4trend.setOutput(ZabbixConstant.EXTEND);
      int history = ZabbixConstant.TEXT_HISTORY_TYPE;
      parameters4trend.setHistory(history);
      parameters4trend.setLimit(400);
      parameters4trend.setTimeFrom(dateFrom.toString());
      parameters4trend.setTimeTill(dateTill.toString());
      String sortorder = "ASC"; // "DESC"
      parameters4trend.setSortorder(sortorder);

      if (!(metricIds == null)) {
        parameters4trend.setItemids(metricIds);
        itemParameters.setTemplateids(null);
        itemParameters.setHostids(null);
      } else {
        parameters4trend.setHostids(hostId);
      }

      request.setParams(parameters4trend);
      request.setId(ZabbixConstant.ID);

      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      List<ZabbixItemResponse> resultTrend = zabClient.getTrendClient(request);
      if (resultTrend.isEmpty()) {
        parameters4trend.setHistory(ZabbixConstant.FLOAT_HISTORY_TYPE);
        return zabClient.getTrendClient(request);
      } else {
        return resultTrend;
      }

    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * TriggerList for wrapper.
   */
  public List<ZabbixItemResponse> getTriggerService(String zone, String serverType,
      String createdHostId, String itemId, String zabbixMethod) throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixParamTriggerRequest> request =
          new JsonRpcRequest<ZabbixParamTriggerRequest>();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);

      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixParamTriggerRequest triggerParameters = new ZabbixParamTriggerRequest();
      triggerParameters.setOutput(ZabbixConstant.EXTEND);
      if (!(itemId == null)) {
        ArrayList<String> itemIdsarray = new ArrayList<>();
        itemIdsarray.add(itemId);
        triggerParameters.setItemids(itemIdsarray);
        createdHostId = null;
      }
      triggerParameters.setOnly_true(null);
      triggerParameters.setExpandDescription(true);
      triggerParameters.setHostids(createdHostId);
      request.setParams(triggerParameters);
      request.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.getTriggerClient(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * Get list of metrics.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostId
   *          id
   * @param itemId
   *          id
   * @param methodFromManager
   *          method
   * @param group
   *          name
   * @return list of item responses
   * @throws ZabbixException
   *           wrapper
   */
  public List<ZabbixItemResponse> getTriggerWithProblemsService(String zone, String serverType,
      String hostId, String itemId, String methodFromManager, String group) throws ZabbixException {

    boolean problems = true;
    return setTriggersService(zone, serverType, hostId, itemId, methodFromManager, problems, group);
  }

  private List<ZabbixItemResponse> setTriggersService(String zone, String serverType, String hostId,
      String itemId, String methodFromManager, boolean problems, String group)
          throws ZabbixException {
    try {
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));

      JsonRpcRequest<ZabbixParamTriggerRequest> request =
          new JsonRpcRequest<ZabbixParamTriggerRequest>();
      JsonRpcRequest<TriggerParamRequestByGroup> requestByGroup = new JsonRpcRequest<>();

      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(methodFromManager);

      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixParamTriggerRequest itemParameters = new ZabbixParamTriggerRequest();
      itemParameters.setOutput(ZabbixConstant.EXTEND);
      itemParameters.setExpandDescription(true);

      itemParameters.setHostids(hostId);

      ArrayList<String> itemIdsarray = new ArrayList<String>();
      itemIdsarray.add(itemId);
      itemParameters.setItemids(itemIdsarray);
      itemParameters.setExpandExpression(true);

      if (!problems == false) {
        TriggerParamRequestByGroup paramsByGroup = new TriggerParamRequestByGroup();
        paramsByGroup.setOnlyTrue(problems);
        paramsByGroup.setGroup(group);
        paramsByGroup.setOutput(ZabbixConstant.EXTEND);
        paramsByGroup.setExpandExpression(true);
        // paramsByGroup.setLastChangeTill("");
        if (hostId != null) {
          paramsByGroup.setHostids(hostId);
        }
        requestByGroup.setId(ZabbixConstant.ID);
        requestByGroup.setMethod(methodFromManager);
        requestByGroup.setAuth(zabbixHelper.getZabbixToken(zone, serverType));
        requestByGroup.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
        requestByGroup.setParams(paramsByGroup);
        return zabClient.getTriggerByGroupClient(requestByGroup);
      }
      request.setParams(itemParameters);
      request.setId(ZabbixConstant.ID);

      return zabClient.getTriggerClient(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * Get GROUPS LIST.
   */
  public List<ZabbixHostGroupResponse> getHostGroupsService(String zone, String serverType,
      String zabbixMethod, String hostGroupIdtoAdapter, String tagService, String hostid)
          throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixParamHostGroupRequest> request =
          new JsonRpcRequest<ZabbixParamHostGroupRequest>();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(zabbixMethod);

      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixParamHostGroupRequest parametersHosts = new ZabbixParamHostGroupRequest();
      parametersHosts.setOutput(ZabbixConstant.EXTEND);

      if (hostid != null) {
        parametersHosts.setHostids(hostid);
      }
      ZabbixFilterRequest filter = new ZabbixFilterRequest();

      parametersHosts.setFilter(filter);
      request.setParams(parametersHosts);
      request.setId(ZabbixConstant.ID);
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.getHostGroupClient(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * Get users list.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @return list of users
   * @throws ZabbixException
   *           wrapper
   */
  public List<ZabbixGetUserResponse> getUsersService(String zone, String serverType)
      throws ZabbixException {
    try {

      JsonRpcRequest<ZabbixGetUserRequest> request = new JsonRpcRequest<ZabbixGetUserRequest>();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(ZabbixMethods.GETUSERS.getzabbixMethod());

      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixGetUserRequest parametersHosts = new ZabbixGetUserRequest();
      parametersHosts.setSelectMediatypes(ZabbixConstant.EXTEND);
      parametersHosts.setSelectUsrgrps(ZabbixConstant.EXTEND);
      parametersHosts.setOutput(ZabbixConstant.EXTEND);
      request.setParams(parametersHosts);

      request.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.getUserClient(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * Get actions.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @return list of zabbix actions
   * @throws ZabbixException
   *           wrapper
   */
  public List<ZabbixGetActionResponse> getActionsService(String zone, String serverType)
      throws ZabbixException {

    try {

      JsonRpcRequest<ZabbixGetActionRequest> request = new JsonRpcRequest<ZabbixGetActionRequest>();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(ZabbixMethods.GETUSERS.getzabbixMethod());

      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixGetActionRequest parametersHosts = new ZabbixGetActionRequest();
      parametersHosts.setOutput(ZabbixConstant.EXTEND);
      request.setParams(parametersHosts);

      request.setId(ZabbixConstant.ID);
      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.getActionClient(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

  /**
   * It creates a user: useful for mail.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param username
   *          user
   * @param password
   *          pswd
   * @param sendmailTo
   *          user
   * @param userGroupId
   *          id
   * @param mediaTypeId
   *          id
   * @return ZabbixCreateUserResponse pojo
   * @throws ZabbixException
   *           wrapper
   */
  public ZabbixCreateUserResponse createUserService(String zone, String serverType, String username,
      String password, String sendmailTo, String userGroupId, String mediaTypeId)
          throws ZabbixException {

    try {

      JsonRpcRequest<ZabbixCreateUserRequest> request =
          new JsonRpcRequest<ZabbixCreateUserRequest>();
      request.setJsonrpc(zabbixHelper.getZabbixRpcVersion());
      request.setMethod(ZabbixMethods.CREATEUSER.getzabbixMethod());

      request.setAuth(zabbixHelper.getZabbixToken(zone, serverType));

      ZabbixCreateUserRequest parametersHosts = new ZabbixCreateUserRequest();
      Usrgrp usrgrp = new Usrgrp();

      parametersHosts.setAlias(username);
      parametersHosts.setPasswd(password);
      List<Usrgrp> usersgrs = new ArrayList<>();
      usrgrp.setUsrgrpid(userGroupId);
      usersgrs.add(usrgrp);
      List<UserMediaRequest> mediatypes = new ArrayList<>();
      UserMediaRequest media = new UserMediaRequest();
      media.setMediatypeid(mediaTypeId);
      mediatypes.add(media);

      parametersHosts.setUsrgrps(usersgrs);
      parametersHosts.setUserMedias(mediatypes);
      request.setParams(parametersHosts);

      request.setId(ZabbixConstant.ID);

      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      return zabClient.createUserClient(request);
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

}