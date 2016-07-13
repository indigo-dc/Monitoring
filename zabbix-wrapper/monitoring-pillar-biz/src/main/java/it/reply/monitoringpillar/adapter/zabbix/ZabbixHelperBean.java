package it.reply.monitoringpillar.adapter.zabbix;

import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.config.Configuration;
import it.reply.monitoringpillar.config.dsl.Server;
import it.reply.monitoringpillar.config.dsl.Zone;
import it.reply.monitoringpillar.domain.dsl.monitoring.InfoType;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.JsonRpcRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixAuthenticationRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixProxyInfoRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixProxyInfoResponse;
import it.reply.utils.web.ws.rest.apiclients.zabbix.ZabbixApiClient;
import it.reply.utils.web.ws.rest.apiclients.zabbix.exception.ZabbixClientException;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.naming.NameNotFoundException;

/**
 * This bean MUST be the only point of access to zabbix server Endpoint and Token.
 */
@Startup
@Singleton
public class ZabbixHelperBean extends ZabbixBase implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LogManager.getLogger(ZabbixHelperBean.class);

  @Inject
  private Configuration config;

  // FIXME: Check if this is safe for concurrency !!
  private MultiKeyMap tokenMap = new MultiKeyMap();

  /**
   * Contains proxy's IDs indexed by &lt;zone, server, proxyName&gt;.
   */
  private MultiKeyMap proxyMap = new MultiKeyMap();

  @PostConstruct
  public void init() throws ZabbixException, NameNotFoundException {
    getAllTokens();
    LOG.info("Caching all tokens");
  }

  @Schedule(hour = "*", persistent = false)
  private void refreshTokens() {
    try {
      LOG.info("refreshing Tokens");
      getAllTokens();
    } catch (Exception exe) {
      LOG.error("Error during token refresh", exe);
    }
    // try {
    // LOG.info("refreshing Proxies");
    // getAllProxies();
    // } catch (Exception e) {
    // LOG.error("Error during proxy refresh", e);
    // }
  }

  private void getAllTokens() {
    for (Zone zone : config.getMonitoringZones().getZones()) {
      for (Server server : zone.getServers()) {
        try {
          getZabbixToken(zone.getName(), InfoType.lookupFromName(server.getType()).getInfoType());
        } catch (ZabbixException ze) {
          throw new IllegalArgumentException("Unable to authenticate on server " + server.getType()
              + " in zone " + zone.getName() + ". Maybe wrong endpoint/credentials ?", ze);
        }
      }
    }
  }

  /**
   * Get token.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @return token
   * @throws ZabbixException
   *           exe
   */
  public String getZabbixToken(String zone, String serverType) throws ZabbixException {
    // Token is already in cache ?
    if (tokenMap.containsKey(zone, serverType)) {
      return (String) tokenMap.get(zone, serverType);
    }

    // Request token
    ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
    ZabbixAuthenticationRequest auth = new ZabbixAuthenticationRequest();

    Server server = config.getMonitoringZones().getZone(zone).getServer(serverType.toString());
    auth.setUser(server.getUsername());
    auth.setPassword(server.getPassword());

    JsonRpcRequest<ZabbixAuthenticationRequest> authRequest =
        new JsonRpcRequest<ZabbixAuthenticationRequest>();

    authRequest.setJsonrpc(getZabbixRpcVersion());
    authRequest.setMethod(ZabbixConstant.METHOD_USER_LOGIN);

    authRequest.setParams(auth);
    authRequest.setId(ZabbixConstant.ID);
    try {
      String token = zabClient.authentication(authRequest);
      // Update the token cache
      tokenMap.put(zone, serverType, token);
      return token;
    } catch (ZabbixClientException ze) {
      throw new ZabbixException(ze);
    }
  }

  public String getZabbixRpcVersion() {
    return config.getMonitoringConfigurations().getAdapter().getRpcVersion();
  }

  /**
   * Return the proxy's id for the proxy with the given name in the given server type in the given
   * zone. <br/>
   * <br/>
   * <b>NOTE</b> that the response is cached (only if the proxy is not found in the cache, the cache
   * for the server's proxies will be refreshed).
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param proxyName
   *          name
   * @return id of proxy
   * @throws NameNotFoundException
   *           in case the proxy is not found
   * @throws ZabbixException
   *           exe
   */
  public String getProxyId(String zone, String serverType, String proxyName)
      throws NameNotFoundException, ZabbixException {
    String proxyId = (String) proxyMap.get(zone, serverType, proxyName);

    if (proxyId == null) {
      // Refresh proxies (per SERVER !!)
      refreshProxiesPerServer(zone, serverType);

      proxyId = (String) proxyMap.get(zone, serverType, proxyName);
    }

    if (proxyId == null) {
      throw new NameNotFoundException(
          String.format("Proxy not found in zone %s, server type %s, proxy name %s", zone,
              serverType, proxyName));
    }
    return proxyId;
  }

  /**
   * Refreshes all the proxies for a given server type in the given zone.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @throws ZabbixException
   *           wrapper
   */
  public synchronized void refreshProxiesPerServer(String zone, String serverType)
      throws ZabbixException {

    try {

      JsonRpcRequest<ZabbixProxyInfoRequest> request = new JsonRpcRequest<>();
      ZabbixProxyInfoRequest paramRequest = new ZabbixProxyInfoRequest();
      request.setJsonrpc(getZabbixRpcVersion());
      request.setMethod(ZabbixMethods.GETPROXY.getzabbixMethod());

      paramRequest.setOutput(ZabbixConstant.EXTEND);
      paramRequest.setSelectInterface(ZabbixConstant.EXTEND);
      request.setParams(paramRequest);
      request.setAuth(getZabbixToken(zone, serverType));
      request.setId(ZabbixConstant.ID);

      ZabbixApiClient zabClient = new ZabbixApiClient(config.getZabbixServerUrl(zone, serverType));
      List<ZabbixProxyInfoResponse> proxyResponses = zabClient.getProxyInfoClient(request);
      for (ZabbixProxyInfoResponse proxyResponse : proxyResponses) {
        proxyMap.put(zone, serverType, proxyResponse.getHost(), proxyResponse.getProxyid());
      }
    } catch (ZabbixClientException ze) {
      throw handleException(ze);
    }
  }

}