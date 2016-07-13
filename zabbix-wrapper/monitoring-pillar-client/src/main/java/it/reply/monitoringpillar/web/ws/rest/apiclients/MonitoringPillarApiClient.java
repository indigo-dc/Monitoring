package it.reply.monitoringpillar.web.ws.rest.apiclients;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.reply.domain.dsl.exceptions.accounting.UnauthorizedException;
import it.reply.domain.dsl.prisma.restprotocol.Error;
import it.reply.domain.dsl.prisma.restprotocol.PrismaResponseWrapper;
import it.reply.monitoringpillar.domain.dsl.monitoring.InfoType;
import it.reply.monitoringpillar.domain.dsl.monitoring.MonitoringConstant;
import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request.HostGroupMonitoringCreateRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringDeleteRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.ProxyCreationRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.CreatedHostInPaaS;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.FilterTimeRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.reply.monitoringpillar.domain.exception.NotFoundMonitoringException;
import it.reply.utils.web.ws.rest.apiclients.AbstractAPIClient;
import it.reply.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.reply.utils.web.ws.rest.apiclients.prisma.PrismaMetaData;
import it.reply.utils.web.ws.rest.apiclients.prisma.PrismaRestResponseDecoder;
import it.reply.utils.web.ws.rest.apiencoding.MappingException;
import it.reply.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.reply.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.reply.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.reply.utils.web.ws.rest.restclient.RestClient;
import it.reply.utils.web.ws.rest.restclient.RestClient.RestMethod;
import it.reply.utils.web.ws.rest.restclient.RestClientFactory;
import it.reply.utils.web.ws.rest.restclient.RestClientFactoryImpl;
import it.reply.utils.web.ws.rest.restclient.RestClientHelper;
import it.reply.utils.web.ws.rest.restclient.exceptions.RestClientException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import java.io.IOException;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

/**
 * Monitoring Pillar Client. Use PrismaMonitoringAPIClient methods to invoke pillar WS
 * 
 */
public class MonitoringPillarApiClient extends AbstractAPIClient {

  private Logger logging = LogManager.getLogger(MonitoringPillarApiClient.class);

  /**
   * Creates a {@link MonitoringPillarApiClient} using the default {@link RestClientFactoryImpl}.
   * The url of Prisma BizWS.
   */
  public MonitoringPillarApiClient(String baseUrl) {
    this(baseUrl, new RestClientFactoryImpl());
  }

  /**
   * Creates a {@link MonitoringPillarApiClient} with the given {@link RestClientFactory}.
   * 
   * @param baseWsUrl
   *          The url of Prisma BizWS.
   * @param restClientFactory
   *          The custom factory for the {@link RestClient}.
   */
  public MonitoringPillarApiClient(String baseWsUrl, RestClientFactory restClientFactory) {
    super(baseWsUrl, restClientFactory);
  }

  protected String addAdapterType(String url, String adapterType) {
    return url + adapterType;
  }

  protected String addZone(String url, String zone) {
    return url + "/zones/" + zone;
  }

  /**
   * Gets the trigger(only triggered) for the given hostgroup.
   */
  public WrappedIaasHealthByTrigger getShotTriggerByHostGroup(String adapterType, String zone,
      String hostGroup) throws RestClientException, NoMappingModelFoundException, MappingException,
          ServerErrorResponseException {

    // TODO: put it into DB or Enum the type of serverType
    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone) + "/types/"
        + InfoType.INFRASTRUCTURE.getInfoType() + "/groups/" + hostGroup + "/hosts?thresholds=true";

    logging.trace("Called: getShotTriggerByHostGroup({}, {})", adapterType, hostGroup);

    logging.trace(url);
    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    BaseRestResponseResult result = restClient.doRequest(RestMethod.GET, url, headers, null, null,
        null, null,
        new PrismaRestResponseDecoder<WrappedIaasHealthByTrigger>(WrappedIaasHealthByTrigger.class),
        null);
    return handleResult(result);
  }

  /**
   * Create the host to Zabbix metrics and watcher. HostMonitoringRequest -> atomicServices should
   * be null in case of VMaaS.
   * 
   */
  // NOW just formatted for zabbix Paas (service e watcher)
  // TODO: parametrizzare il tipo di server
  public void addHostToMonitoring(String adapterType, String zone, String group, String host,
      String uuid, HostMonitoringRequest request) throws MappingException, APIErrorException,
          JsonParseException, JsonMappingException, IOException, RestClientException,
          NoMappingModelFoundException, ServerErrorResponseException {
    logging.trace("Called: addHostToMonitoring({}, {})", adapterType, request);
    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone) + "/types/"
        + InfoType.PAAS.getInfoType() + "/groups/" + request.getHostGroup() + "/hosts/" + host;
    logging.trace(url);
    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

    BaseRestResponseResult result =
        restClient.postRequest(url, headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
            new PrismaRestResponseDecoder<String>(String.class), null);

    handleResult(result);
  }

  /**
   * Delete service from zabbix metrics and watcher.
   * 
   */
  // NOW just formatted for zabbix Paas (service e watcher)
  // TODO: parametrizzare il tipo di server
  public void deleteHostFromMonitoring(String adapterType, String zone, String group, String host,
      HostMonitoringDeleteRequest request) throws MappingException, APIErrorException,
          JsonParseException, JsonMappingException, IOException, RestClientException,
          NoMappingModelFoundException, ServerErrorResponseException, NotFoundMonitoringException {

    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone) + "/types/"
        + InfoType.PAAS.getInfoType() + "/groups/" + request.getGroup() + "/hosts/"
        + request.getUuid();

    logging.trace("Called: deleteHostFromMonitoring({}, {})", adapterType, request);

    logging.trace(url);

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

    BaseRestResponseResult result =
        restClient.deleteRequest(url, headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
            new PrismaRestResponseDecoder<CreatedHostInPaaS>(CreatedHostInPaaS.class), null);
    handleResult(result);
  }

  /**
   * Group Overall INFO.
   * 
   */
  public MonitoringWrappedResponseIaas getMachineByHostGroup(String adapterType, String zone,
      String hostGroup) throws RestClientException, NoMappingModelFoundException, MappingException,
          ServerErrorResponseException {

    // TODO: infrastructure is the name of the server type we are making
    // requests, to be implemented other yet
    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone) + "/types/"
        + InfoType.INFRASTRUCTURE.getInfoType() + "/groups/" + hostGroup;

    logging.trace("Called: getMachineByHostGroup({}, {})", adapterType, hostGroup);

    // TODO: infrastructure is the name of the server type we are making
    // requests
    logging.trace(url);

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    BaseRestResponseResult result = restClient.doRequest(RestMethod.GET, url, headers, null, null,
        null, null, new PrismaRestResponseDecoder<MonitoringWrappedResponseIaas>(
            MonitoringWrappedResponseIaas.class),
        null);

    return handleResult(result);
  }

  /**
   * NOW just formatted for zabbix Iaas (infrastructure).
   *
   * @param adapterType
   *          type
   * @param zone
   *          name
   * @param hostGroup
   *          name
   * @param hostName
   *          name
   * @return IaasWrapper
   * @throws RestClientException
   *           exe
   * @throws NoMappingModelFoundException
   *           exe
   * @throws MappingException
   *           exe
   * @throws ServerErrorResponseException
   *           exe
   */
  public MonitoringWrappedResponseIaas getItemsByHostGroupAndHost(String adapterType, String zone,
      String hostGroup, String hostName) throws RestClientException, NoMappingModelFoundException,
          MappingException, ServerErrorResponseException {

    logging.trace("Called: getItemsByHostGroupAndHost({}, {}, {})", adapterType, hostGroup,
        hostName);

    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone) + "/types/"
        + InfoType.INFRASTRUCTURE.getInfoType() + "/groups/" + hostGroup + "/hosts/" + hostName;

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    BaseRestResponseResult result = restClient.doRequest(RestMethod.GET, url, headers, null, null,
        null, null, new PrismaRestResponseDecoder<MonitoringWrappedResponseIaas>(
            MonitoringWrappedResponseIaas.class),
        null);

    return handleResult(result);
  }

  /**
   * Returns all the metrics and thresholds associated to all the monitored hosts with the given ID.
   * 
   * @param adapterType
   *          for example "zabbix". This value must be know to MonitoringPillar
   * @param group
   *          the group in which the service has been added (usually is the workgroupID)
   * @param serviceId
   *          the paasServiceID
   * @return wrapper Response Paas
   * 
   */

  public MonitoringWrappedResponsePaas getMonitoringServiceByTag(String adapterType, String zone,
      String group, String serviceId) throws RestClientException, NoMappingModelFoundException,
          MappingException, ServerErrorResponseException {

    logging.trace("Called: getMonitoringServiceByTag({}, {})", adapterType, serviceId);
    // TODO: parametrizzare serverType
    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone).toLowerCase() + "/types/"
        + InfoType.SERVICE.getInfoType() + "/groups/" + group + "/hosts?service-id=" + serviceId;
    logging.trace(url);

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    BaseRestResponseResult result = restClient.getRequest(url, headers,
        new PrismaRestResponseDecoder<MonitoringWrappedResponsePaas>(
            MonitoringWrappedResponsePaas.class),
        null);

    return handleResult(result);
  }

  /**
   * Returns all the metrics and thresholds associated to the hosts with the given ID.
   * 
   * @param adapterType
   *          for example "zabbix". This value must be know to MonitoringPillar
   * @param group
   *          the group in which the service has been added (usually is the workgroupID)
   * @param hostId
   *          the ID of the host
   * @return pojo
   * 
   * @throws ServerErrorResponseException
   *           wrapper
   */
  public MonitoringWrappedResponsePaas getMonitoringHostById(String adapterType, String zone,
      String group, String hostId) throws RestClientException, NoMappingModelFoundException,
          MappingException, ServerErrorResponseException {

    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone).toLowerCase() + "/types/"
        + InfoType.SERVICE.getInfoType() + "/groups/" + group + "/hosts/" + hostId;

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    BaseRestResponseResult result = restClient.getRequest(url, headers,
        new PrismaRestResponseDecoder<MonitoringWrappedResponsePaas>(
            MonitoringWrappedResponsePaas.class),
        null);

    return handleResult(result);
  }

  /**
   * Get Iaas status from external script.
   */
  public MonitoringWrappedResponseIaas getPrismaIaasHealth(String adapterType, String zone,
      String iaasType, String iaashost) throws MappingException, NoMappingModelFoundException,
          ServerErrorResponseException, APIErrorException, RestClientException, Exception {
    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone).toLowerCase() + "/types/"
        + InfoType.INFRASTRUCTURE.getInfoType() + "/groups/" + iaasType + "/hosts/" + iaashost;

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    BaseRestResponseResult result = restClient.getRequest(url, headers,
        new PrismaRestResponseDecoder<MonitoringWrappedResponseIaas>(
            MonitoringWrappedResponseIaas.class),
        null);

    return handleResult(result);
  }

  /**
   * Create Workgroup in monitoring platform.
   */
  public void createGroup(String adapterType, String zone, String groupName)
      throws MappingException, APIErrorException, JsonParseException, JsonMappingException,
      IOException, RestClientException, NoMappingModelFoundException, ServerErrorResponseException {

    logging.trace("Called: createGroup({}, {})", adapterType, groupName);
    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone) + "/types/"
        + InfoType.ALL.getInfoType() + "/groups/" + groupName;
    logging.trace(url);

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
    HostGroupMonitoringCreateRequest request = new HostGroupMonitoringCreateRequest();
    request.setHostGroupName(groupName);
    GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

    BaseRestResponseResult result =
        restClient.postRequest(url, headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
            new PrismaRestResponseDecoder<String>(String.class), null);
    handleResult(result);
  }

  /**
   * Create Proxy into Monitoring platform.
   */
  public void createProxy(String adapterType, String zone, Long wgId)
      throws MappingException, APIErrorException, JsonParseException, JsonMappingException,
      IOException, RestClientException, NoMappingModelFoundException, ServerErrorResponseException {

    logging.trace("Called: createProxy({}, {})", adapterType, MonitoringConstant.WG_PREFIX + wgId);
    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone) + "/types/"
        + InfoType.ALL.getInfoType() + "/groups/" + MonitoringConstant.WG_PREFIX + wgId
        + "/proxies";
    logging.trace(url);

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    ProxyCreationRequest request = new ProxyCreationRequest();
    request.setProxyName(MonitoringConstant.PROXY_PREFIX + wgId);
    GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

    BaseRestResponseResult result =
        restClient.postRequest(url, headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
            new PrismaRestResponseDecoder<String>(String.class), null);
    handleResult(result);
  }

  /**
   * Delete Workgroup from Monitoring Platform.
   * 
   */
  public void deleteGroup(String adapterType, String zone, String groupName)
      throws MappingException, APIErrorException, JsonParseException, JsonMappingException,
      IOException, RestClientException, NoMappingModelFoundException, ServerErrorResponseException {

    logging.trace("Called: deleteGroup({}, {})", adapterType, groupName);
    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone) + "/types/"
        + InfoType.ALL.getInfoType() + "/groups/" + groupName;
    logging.trace(url);

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    HostGroupMonitoringCreateRequest request = new HostGroupMonitoringCreateRequest();
    request.setHostGroupName(groupName);

    GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

    BaseRestResponseResult result =
        restClient.deleteRequest(url, headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
            new PrismaRestResponseDecoder<String>(String.class), null);
    handleResult(result);
  }

  /*********************************
   * GET HISTORY BY SERVICE ID.
   * 
   */
  public MonitoringWrappedResponsePaas getHistoryByServiceIdFilteredClient(String adapterType,
      String zone, String groupName, Long serviceId, String metricName,
      FilterTimeRequest filterTimeRequest)
          throws RestClientException, NoMappingModelFoundException, MappingException,
          ServerErrorResponseException, JsonParseException, JsonMappingException, IOException {

    logging.trace("Called: getHistory({}, {})", adapterType, groupName, serviceId);
    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone) + "/types/"
        + InfoType.SERVICE.getInfoType() + "/groups/" + "Workgroup-" + groupName + "/services/"
        + serviceId + "/metrics/" + metricName + "/history/";
    logging.trace(url);

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(filterTimeRequest);

    BaseRestResponseResult result =
        restClient.postRequest(url, headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
            new PrismaRestResponseDecoder<MonitoringWrappedResponsePaas>(
                MonitoringWrappedResponsePaas.class),
            null);

    return handleResult(result);
  }

  /****************************
   * GET HISTORY BY HOSTNAME.
   */
  public MonitoringWrappedResponsePaas getHistoryByHostNameFilteredClient(String adapterType,
      String zone, String groupName, String hostName, String metricName,
      FilterTimeRequest filterTimeRequest)
          throws RestClientException, NoMappingModelFoundException, MappingException,
          ServerErrorResponseException, JsonParseException, JsonMappingException, IOException {

    logging.trace("Called: getHistory({}, {})", adapterType, groupName, hostName);
    String url = addZone(addAdapterType(baseWSUrl, adapterType), zone) + "/types/"
        + InfoType.SERVICE.getInfoType() + "/groups/" + checkGroupName(groupName) + "/hosts/"
        + hostName + "/metrics/" + metricName + "/history/";
    logging.trace(url);

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(filterTimeRequest);

    BaseRestResponseResult result =
        restClient.postRequest(url, headers, ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
            new PrismaRestResponseDecoder<MonitoringWrappedResponsePaas>(
                MonitoringWrappedResponsePaas.class),
            null);

    return handleResult(result);
  }

  private String checkGroupName(String groupName) {

    if (groupName.toLowerCase().contains(MonitoringConstant.WG_PREFIX.toLowerCase())) {
      return groupName;
    } else {
      return MonitoringConstant.WG_PREFIX + groupName;
    }
  }

  /**
   * Manages the result by wrapping it.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected <RETURN_TYPET> RETURN_TYPET handleResult(BaseRestResponseResult result,
      PrismaMetaData meta) throws APIErrorException {
    if (result.getStatus().getFamily() == Status.Family.SUCCESSFUL) {
      try {
        if (meta != null) {
          meta.setMeta(((PrismaResponseWrapper<?>) result.getResult()).getMeta());
          meta.setBaseRestResponseResult(result);
          meta.setPrismaResponseWrapper(((PrismaResponseWrapper<?>) result.getResult()));
        }
        if (result.getResult() != null) {
          return ((PrismaResponseWrapper<RETURN_TYPET>) result.getResult()).getResult();
        } else {
          return null;
        }
      } catch (Exception ex) {
        throw new APIErrorException("Unexpected response type.", ex);
      }
    } else {
      Error error = ((PrismaResponseWrapper) result.getResult()).getError();
      // TODO: Is this right ?! The Monitoring Pillar currently doesn't
      // have auth !!
      if (result.getStatus().getStatusCode() == Status.UNAUTHORIZED.getStatusCode()) {
        throw new UnauthorizedException(error.getErrorMsg());
      } else {
        throw new APIErrorException("API_ERROR", null, result.getOriginalRestMessage(),
            ((PrismaResponseWrapper) result.getResult()).getError());
      }
    }

  }

  protected <REPRESENTATION_CLASST> REPRESENTATION_CLASST handleResult(
      BaseRestResponseResult result) throws APIErrorException {
    return handleResult(result, null);
  }
}