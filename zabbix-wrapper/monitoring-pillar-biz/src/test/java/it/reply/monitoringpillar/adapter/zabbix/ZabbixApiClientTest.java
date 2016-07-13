package it.reply.monitoringpillar.adapter.zabbix;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.JsonRpcRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.ZabbixExtendedParamHost;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.Inventory;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.JsonRpcResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixGroup;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV24;
import it.reply.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.reply.utils.web.ws.rest.apiclients.zabbix.ZabbixApiClient;
import it.reply.utils.web.ws.rest.apiclients.zabbix.exception.ZabbixClientException;
import it.reply.utils.web.ws.rest.apiencoding.MappingException;
import it.reply.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.reply.utils.web.ws.rest.apiencoding.RestMessage;
import it.reply.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.reply.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.reply.utils.web.ws.rest.apiencoding.decode.RestResponseDecodeStrategy;
import it.reply.utils.web.ws.rest.apiencoding.decode.RestResponseDecoder;
import it.reply.utils.web.ws.rest.restclient.RestClientFactory;
import it.reply.utils.web.ws.rest.restclient.RestClientHelper;
import it.reply.utils.web.ws.rest.restclient.RestClientImpl;
import it.reply.utils.web.ws.rest.restclient.exceptions.RestClientException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

@RunWith(MockitoJUnitRunner.class)
public class ZabbixApiClientTest {

  private static final String token = "90e28ba39e52a22247051f1452f2d15b";
  private static final String apdapterTestDev = "zabbix";
  private static final String zone = "paas-dev-core";

  @Mock
  private RestClientFactory restClientFactory;

  @Mock
  private RestClientImpl<BaseRestResponseResult> restClient =
      new RestClientImpl<BaseRestResponseResult>();

  @InjectMocks
  private ZabbixApiClient apiclientUnderTest = new ZabbixApiClient(token, restClientFactory);

  @Test()
  public void getHostList() throws JsonParseException, JsonMappingException, APIErrorException,
      MappingException, IOException, RestClientException, NoMappingModelFoundException,
      ServerErrorResponseException, ZabbixClientException {

    // Mock zabbix Expcted response
    JsonRpcResponse<List<ZabbixMonitoredHostResponseV24>> resultRpczabbix = new JsonRpcResponse<>();
    resultRpczabbix.setError(null);
    resultRpczabbix.setId(1);
    resultRpczabbix.setJsonrpc("2.0");

    ZabbixMonitoredHostResponseV24 hostzabbixActual = new ZabbixMonitoredHostResponseV24();
    hostzabbixActual.setHost("testMachine");
    hostzabbixActual.setName("testMachine");
    List<ZabbixGroup> groupszabbixActuals = new ArrayList<>();
    ZabbixGroup groupzabbixActual = new ZabbixGroup();
    groupzabbixActual.setName("groupTest");
    groupszabbixActuals.add(groupzabbixActual);
    hostzabbixActual.setGroups(groupszabbixActuals);

    Inventory inventory = new Inventory();
    inventory.setTag("id");
    hostzabbixActual.setInventory(inventory);
    List<ZabbixMonitoredHostResponseV24> hostszabbixActuals = new ArrayList<>();
    hostszabbixActuals.add(hostzabbixActual);
    resultRpczabbix.setResult(hostszabbixActuals);

    // Mock zabbix request
    JsonRpcRequest<ZabbixExtendedParamHost> jsonRequest = new JsonRpcRequest<>();
    jsonRequest.setAuth(token);
    jsonRequest.setJsonrpc("2.0");
    jsonRequest.setMethod(ZabbixMethods.HOST.getzabbixMethod());
    ZabbixExtendedParamHost param = new ZabbixExtendedParamHost();
    param.setHostids("hostid");
    param.setGroupids("groupid");
    param.setOutput("output");
    jsonRequest.setParams(param);

    GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder().build(jsonRequest);
    String body =
        "{ \"jsonrpc\":\"2.0\",\"result\":[{\"hostid\":\"testMachine\",\"host\":\"testMachine\","
            + "\"groups\":[{\"groupid\":\"15\",\"name\":\"Workgroup-1\"}],\"parentTemplates\":"
            + "[{\"host\":\"TemplateActiveApache\",\"status\":\"3\"}},\"id\":1} ";
    RestMessage originalRestMessage = new RestMessage(null, body);
    originalRestMessage.setBody(body);

    BaseRestResponseResult zabbixMockedResult =
        new BaseRestResponseResult(Status.OK, resultRpczabbix, null, originalRestMessage);

    Mockito.when(restClientFactory.getRestClient()).thenReturn(restClient);

    Mockito
        .when(restClient.postRequest(Mockito.any(String.class), Mockito.any(MultivaluedMap.class),
            Mockito.any(GenericEntity.class),
            Mockito.eq(RestClientHelper.JsonEntityBuilder.MEDIA_TYPE),
            Mockito.any(RestResponseDecoder.class), Mockito.any(RestResponseDecodeStrategy.class)))
        .thenReturn(zabbixMockedResult);

    // Mock Actual response method under test

    ZabbixMonitoredHostResponseV24 host = new ZabbixMonitoredHostResponseV24();

    // Set actual zabbix response
    List<ZabbixGroup> groups = new ArrayList<>();
    ZabbixGroup group = new ZabbixGroup();
    group.setName("groupTest");
    groups.add(group);
    host.setGroups(groups);
    host.setHost("testMachine");
    host.setName("testMachine");
    Inventory inventoryResponse = new Inventory();
    inventoryResponse.setTag("id");
    host.setInventory(inventoryResponse);
    List<ZabbixMonitoredHostResponseV24> classUnderTestresponseExpected = new ArrayList<>();
    classUnderTestresponseExpected.add(host);

    List<ZabbixMonitoredHostResponseV24> actualValue =
        apiclientUnderTest.getHostsExtended2_4(jsonRequest);
    Assert.assertTrue(EqualsBuilder.reflectionEquals(classUnderTestresponseExpected, actualValue));

  }
}
