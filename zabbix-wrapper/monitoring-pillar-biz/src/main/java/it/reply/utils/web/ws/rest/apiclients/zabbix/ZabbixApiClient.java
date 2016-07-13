package it.reply.utils.web.ws.rest.apiclients.zabbix;

import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.response.MonitoringVmCredentialsResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.HostGroupParamRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.JsonRpcDeleteHostRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.JsonRpcRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.TriggerParamRequestByGroup;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixAuthenticationRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixCreateUserRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixExtendedParamHost;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixGetActionRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixGetUserRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamCreateHostRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamCreateProxyRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamHistoryRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamHostGroupRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamItemRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTemplateRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTrendRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamTriggerRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamUpdate;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamUpdateInventoryTag;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixProxyInfoRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixUpdateProxyRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.HostGroupResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.JsonRpcError;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.JsonRpcResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.UpdatedItemsResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixCreateUserResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixGetActionResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixGetUserResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV24;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixProxyInfoResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponseV24;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixUpdateProxyResponse;
import it.reply.utils.web.ws.rest.apiclients.AbstractAPIClient;
import it.reply.utils.web.ws.rest.apiclients.zabbix.exception.ClientResponseException;
import it.reply.utils.web.ws.rest.apiclients.zabbix.exception.ZabbixClientException;
import it.reply.utils.web.ws.rest.apiencoding.MappingException;
import it.reply.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.reply.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.reply.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.reply.utils.web.ws.rest.restclient.RestClientFactory;
import it.reply.utils.web.ws.rest.restclient.RestClientFactoryImpl;
import it.reply.utils.web.ws.rest.restclient.RestClientHelper;
import it.reply.utils.web.ws.rest.restclient.exceptions.RestClientException;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

/**
 * This class contains Zabbix API Client requests implementation.
 */
public class ZabbixApiClient extends AbstractAPIClient {

  public ZabbixApiClient(String baseWsUrl) {
    this(baseWsUrl, new RestClientFactoryImpl());
  }

  public ZabbixApiClient(String baseWsUrl, RestClientFactory restClientFactory) {
    super(baseWsUrl, restClientFactory);
  }

  /**
   * Get token for authenticating.
   * 
   * @param request
   *          pojo
   * @return token
   * @throws ZabbixClientException
   *           wrapper
   */
  public String authentication(JsonRpcRequest<ZabbixAuthenticationRequest> request)
      throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<String>(String.class), null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /**
   * Client for creating the host from Adapter.
   */
  public MonitoringVmCredentialsResponse createHostClient(
      JsonRpcRequest<ZabbixParamCreateHostRequest> request) throws ZabbixClientException {

    try {
      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<MonitoringVmCredentialsResponse>(
              MonitoringVmCredentialsResponse.class),
          null);
      return handleResult(result);
    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }

  }

  /*****************************
   * GET PROXY INFO.
   * 
   * @param request
   *          pojo
   * @return list of ZabbixProxyInfoResponse
   * @throws ZabbixClientException
   *           wrapper
   */
  public List<ZabbixProxyInfoResponse> getProxyInfoClient(
      JsonRpcRequest<ZabbixProxyInfoRequest> request) throws ZabbixClientException {

    try {
      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);
      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixProxyInfoResponse>>(ArrayList.class,
              ZabbixProxyInfoResponse.class),
          null);
      return handleResult(result);
    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /****************
   * UPDATE PROXY.
   ***************/
  public ZabbixUpdateProxyResponse updateProxyClient(
      JsonRpcRequest<ZabbixUpdateProxyRequest> request) throws ZabbixClientException {

    try {
      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);
      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ZabbixUpdateProxyResponse>(ZabbixUpdateProxyResponse.class),
          null);
      return handleResult(result);
    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /****************
   * CREATE PROXY.
   ***************/
  public ZabbixUpdateProxyResponse createProxyClient(
      JsonRpcRequest<ZabbixParamCreateProxyRequest> request) throws ZabbixClientException {

    try {
      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);
      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ZabbixUpdateProxyResponse>(ZabbixUpdateProxyResponse.class),
          null);
      return handleResult(result);
    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /*****************
   * DELETE HOSTS.
   *****************/
  public MonitoringVmCredentialsResponse deleteHostClient(JsonRpcDeleteHostRequest request)
      throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<MonitoringVmCredentialsResponse>(
              MonitoringVmCredentialsResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /**
   * GET GROUPS.
   */
  public List<ZabbixHostGroupResponse> getHostGroupClient(
      JsonRpcRequest<ZabbixParamHostGroupRequest> request) throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixHostGroupResponse>>(ArrayList.class,
              ZabbixHostGroupResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /*****************
   * GET TEMPLATES.
   * 
   * @param request
   *          pojo @see ZabbixParamHostGroupRequest
   * @return list of ZabbixTemplateResponse
   * @throws ZabbixClientException
   *           wrapper
   */
  public List<ZabbixTemplateResponse> getTemplate(
      JsonRpcRequest<ZabbixParamHostGroupRequest> request) throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixTemplateResponse>>(ArrayList.class,
              ZabbixTemplateResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /***************
   * GET HOSTS.
   ***************/
  public List<ZabbixMonitoredHostsResponse> getHostsClient(
      JsonRpcRequest<ZabbixParamRequest> request) throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixMonitoredHostsResponse>>(ArrayList.class,
              ZabbixMonitoredHostsResponse.class),
          null);

      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /********************
   * GET HOSTS 2_4.
   *******************/
  public List<ZabbixMonitoredHostsResponse> getHostsExtended(
      JsonRpcRequest<ZabbixExtendedParamHost> request) throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixMonitoredHostsResponse>>(ArrayList.class,
              ZabbixMonitoredHostsResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException ze) {
      throw new ZabbixClientException(ze);
    }
  }

  /**************************************************************************
   * GET HOSTS FILTERED BY NEW ZABBIX 2.4 (extended with items and templates).
   **************************************************************************/
  public List<ZabbixMonitoredHostResponseV24> getHostsExtended2_4(
      JsonRpcRequest<ZabbixExtendedParamHost> request) throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixMonitoredHostResponseV24>>(ArrayList.class,
              ZabbixMonitoredHostResponseV24.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /************
   * GET ITEMs.
   ***********/
  public List<ZabbixItemResponse> getItemsClient(JsonRpcRequest<ZabbixParamItemRequest> request)
      throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixItemResponse>>(ArrayList.class,
              ZabbixItemResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /************
   * GET ITEMs.
   ************/
  public List<ZabbixItemResponse> getFilterdItemNoZabbixWrappingResponse(
      JsonRpcRequest<ZabbixParamTemplateRequest> request)
          throws ZabbixClientException, RestClientException, ServerErrorResponseException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixItemResponse>>(ArrayList.class,
              ZabbixItemResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /******************************************
   * GET ITEMS USEFUL FOR HISTORY AND EVENTs.
   *****************************************/
  public List<ZabbixItemResponse> getHistoryClient(
      JsonRpcRequest<ZabbixParamHistoryRequest> request) throws ZabbixClientException {

    try {
      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixItemResponse>>(ArrayList.class,
              ZabbixItemResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /***********
   * GET TREND.
   ***********/
  public List<ZabbixItemResponse> getTrendClient(JsonRpcRequest<ZabbixParamTrendRequest> request)
      throws ZabbixClientException {

    try {
      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixItemResponse>>(ArrayList.class,
              ZabbixItemResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /*************
   * GET TRIGGER.
   *************/
  public List<ZabbixItemResponse> getTriggerClient(
      JsonRpcRequest<ZabbixParamTriggerRequest> request) throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixItemResponse>>(ArrayList.class,
              ZabbixItemResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /**************
   * GET TRIGGER.
   **************/
  public List<ZabbixItemResponse> getTriggerByGroupClient(
      JsonRpcRequest<TriggerParamRequestByGroup> request) throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixItemResponse>>(ArrayList.class,
              ZabbixItemResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /*****************************
   * GET HOSTGROUP CREATION.
   ****************************/
  public HostGroupResponse createHostGroupClient(JsonRpcRequest<HostGroupParamRequest> request)
      throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);
      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<HostGroupResponse>(HostGroupResponse.class), null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }

  }

  /****************
   * UPDATE GROUP.
   */
  public HostGroupResponse updateGroupClient(JsonRpcRequest<HostGroupParamRequest> request)
      throws ZabbixClientException {

    try {
      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<HostGroupResponse>(HostGroupResponse.class), null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }

  }

  /*******************
   * DELETE GROUP.
   *******************/
  public HostGroupResponse deleteHostGroupClient(JsonRpcDeleteHostRequest request)
      throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<HostGroupResponse>(HostGroupResponse.class), null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /**************
   * DELETE PROXY.
   **************/
  public ZabbixUpdateProxyResponse deleteProxyClient(JsonRpcDeleteHostRequest request)
      throws ZabbixClientException {

    try {
      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ZabbixUpdateProxyResponse>(ZabbixUpdateProxyResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /***********************************
   * GET UPDATE HOST (DISABLE/ENABLE).
   *********************************/
  public MonitoringVmCredentialsResponse updateHostClient(JsonRpcRequest<ZabbixParamUpdate> request)
      throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<MonitoringVmCredentialsResponse>(
              MonitoringVmCredentialsResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }

  }

  /***********************************
   * GET UPDATE HOST FORCE TO HAVE TAG.
   *********************************/
  public MonitoringVmCredentialsResponse updateHostTagClient(
      JsonRpcRequest<ZabbixParamUpdateInventoryTag> request) throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<MonitoringVmCredentialsResponse>(
              MonitoringVmCredentialsResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }

  }

  /********************************
   * GET UPDATE ITEM (DISABLE/ENABLE).
   *********************************/
  public UpdatedItemsResponse updateItemClient(JsonRpcRequest<ZabbixParamUpdate> request)
      throws ZabbixClientException {

    try {

      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(request);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<UpdatedItemsResponse>(UpdatedItemsResponse.class), null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }
  }

  /****************************************************
   * CLIENT FOR HANDLING TEMPLATES WITH ITEMS EXTENDED.
   */
  public List<ZabbixTemplateResponseV24> getTemplatesClient(
      JsonRpcRequest<ZabbixParamTemplateRequest> requestTemplate) throws ZabbixClientException {

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    BaseRestResponseResult result = null;
    try {
      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(requestTemplate);
      result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixTemplateResponseV24>>(ArrayList.class,
              ZabbixTemplateResponseV24.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }

  }

  /**
   * Create User.
   * 
   * @param requestUser
   *          pojo
   * @return pojo
   * @throws ZabbixClientException
   *           wrapper
   */
  public ZabbixCreateUserResponse createUserClient(
      JsonRpcRequest<ZabbixCreateUserRequest> requestUser) throws ZabbixClientException {

    try {
      MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(requestUser);

      BaseRestResponseResult result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ZabbixCreateUserResponse>(ZabbixCreateUserResponse.class),
          null);
      return handleResult(result);
    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }

  }

  /**
   * Client for getting the list of users and their features.
   * 
   * @param requestUser
   *          pojo
   * @return List of zabbix users
   * @throws ZabbixClientException
   *           wrapper
   */
  public List<ZabbixGetUserResponse> getUserClient(JsonRpcRequest<ZabbixGetUserRequest> requestUser)
      throws ZabbixClientException {

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    BaseRestResponseResult result = null;
    try {
      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(requestUser);
      result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixGetUserResponse>>(ArrayList.class,
              ZabbixGetUserResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }

  }

  /**
   * GetAction for alert script.
   * 
   * @param requestUser
   *          request
   * @return list of action response
   * @throws ZabbixClientException
   *           wrapper
   */
  public List<ZabbixGetActionResponse> getActionClient(
      JsonRpcRequest<ZabbixGetActionRequest> requestUser) throws ZabbixClientException {

    MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

    BaseRestResponseResult result = null;
    try {
      GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(requestUser);
      result = restClient.postRequest(baseWSUrl, headers, ge,
          RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
          new ZabbixResponseDecoder<ArrayList<ZabbixGetUserResponse>>(ArrayList.class,
              ZabbixGetUserResponse.class),
          null);
      return handleResult(result);

    } catch (IOException | RestClientException | NoMappingModelFoundException | MappingException
        | ServerErrorResponseException se) {
      throw new ZabbixClientException(se);
    }

  }

  @SuppressWarnings("unchecked")
  protected <RepresentationClassT> RepresentationClassT handleResult(BaseRestResponseResult result)
      throws ZabbixClientException {

    if (result.getStatus().getFamily() == Status.Family.SUCCESSFUL) {

      JsonRpcResponse<RepresentationClassT> rc =
          (JsonRpcResponse<RepresentationClassT>) result.getResult();
      if (rc.getResult() != null) {
        return ((JsonRpcResponse<RepresentationClassT>) result.getResult()).getResult();
      } else {
        throw parseZabbixError(rc.getError());
      }
    } else {
      throw new ZabbixClientException(result.getStatus().toString());
    }
  }

  private ZabbixClientException parseZabbixError(JsonRpcError error) {
    switch (ZabbixErrorCode.lookupFromName(error.getCode())) {
      case DUPLICATE_RESOURCE:
        return new ClientResponseException(error.getData(), Status.CONFLICT);
      case NOT_EXISTING_RESOURCE:
        return new ClientResponseException(error.getData(), Status.NOT_FOUND);
      default:
        return new ZabbixClientException("Unexpected status code");
    }

  }

  private enum ZabbixErrorCode {

    DUPLICATE_RESOURCE(-32602), NOT_EXISTING_RESOURCE(-32500);

    int code;

    private ZabbixErrorCode(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }

    public static ZabbixErrorCode lookupFromName(int code) throws IllegalArgumentException {
      for (ZabbixErrorCode c : values()) {
        if (code == c.getCode()) {
          return c;
        }
      }
      throw new IllegalArgumentException("Cannot find [" + code + "] into ZabbixErrorCode enum");
    }

  }
}