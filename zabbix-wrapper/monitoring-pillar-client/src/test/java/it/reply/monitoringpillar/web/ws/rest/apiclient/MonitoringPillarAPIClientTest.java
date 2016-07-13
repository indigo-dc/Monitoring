// package it.reply.monitoringpillar.web.ws.rest.apiclient;
//
// import com.fasterxml.jackson.core.JsonParseException;
// import com.fasterxml.jackson.databind.JsonMappingException;
//
// import
// it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
// import
// it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.PaaSMetric;
// import
// it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.PaasMachine;
// import
// it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.PaasThreshold;
// import
// it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.Inventory;
// import
// it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.JSONRPCResponse;
// import
// it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixGroup;
// import
// it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV2_4;
// import
// it.reply.monitoringpillar.web.ws.rest.apiclients.MonitoringPillarAPIClient;
// import it.reply.utils.web.ws.rest.apiclients.prisma.APIErrorException;
// import it.reply.utils.web.ws.rest.apiencoding.MappingException;
// import it.reply.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
// import it.reply.utils.web.ws.rest.apiencoding.RestMessage;
// import it.reply.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
// import it.reply.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
// import
// it.reply.utils.web.ws.rest.apiencoding.decode.RestResponseDecodeStrategy;
// import it.reply.utils.web.ws.rest.apiencoding.decode.RestResponseDecoder;
// import it.reply.utils.web.ws.rest.restclient.RestClientFactory;
// import it.reply.utils.web.ws.rest.restclient.RestClientImpl;
// import it.reply.utils.web.ws.rest.restclient.exceptions.RestClientException;
//
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
//
// import javax.ws.rs.core.MultivaluedMap;
//
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.runners.MockitoJUnitRunner;
//
// @RunWith(MockitoJUnitRunner.class)
// public class MonitoringPillarAPIClientTest {
//
// // class ResultClass extends JavaType {
// //
// // protected ResultClass(Class<?> raw, int additionalHash, Object
// // valueHandler, Object typeHandler,
// // boolean asStatic) {
// // super(raw, additionalHash, valueHandler, typeHandler, asStatic);
// // // TODO Auto-generated constructor stub
// // }
// // }
//
// private static final String urlTestDev = "90e28ba39e52a22247051f1452f2d15b";
// private static final String apdapterTestDev = "zabbix";
// private static final String zone = "paas-dev-core";
//
// @Mock
// private RestClientFactory restClientFactory;
//
// @Mock
// private RestClientImpl<BaseRestResponseResult> restClient = new
// RestClientImpl<BaseRestResponseResult>();
//
// // @InjectMocks
// // private JavaType resultClass;
//
// @InjectMocks
// private MonitoringPillarAPIClient apiclientMocked = new
// MonitoringPillarAPIClient(urlTestDev, restClientFactory);
//
// @Test()
// public void getHostList() throws JsonParseException, JsonMappingException,
// APIErrorException, MappingException,
// IOException, RestClientException, NoMappingModelFoundException,
// ServerErrorResponseException {
//
// // Mock zabbix response
// JSONRPCResponse<List<ZabbixMonitoredHostResponseV2_4>> resultRPCzabbix = new
// JSONRPCResponse<>();
// ZabbixMonitoredHostResponseV2_4 hostzabbixActual = new
// ZabbixMonitoredHostResponseV2_4();
// resultRPCzabbix.setError(null);
// resultRPCzabbix.setId(1);
// resultRPCzabbix.setJsonrpc("2.0");
// List<ZabbixMonitoredHostResponseV2_4> hostszabbixActuals = new ArrayList<>();
// hostzabbixActual.setHost("testMachine");
// List<ZabbixGroup> groupszabbixActuals = new ArrayList<>();
// ZabbixGroup groupzabbixActual = new ZabbixGroup();
// groupzabbixActual.setName("groupTest");
// groupszabbixActuals.add(groupzabbixActual);
// hostzabbixActual.setGroups(groupszabbixActuals);
// hostzabbixActual.setName("testMachine");
// Inventory inventory = new Inventory();
// inventory.setTag("id");
// hostzabbixActual.setInventory(inventory);
// hostszabbixActuals.add(hostzabbixActual);
//
// resultRPCzabbix.setResult(hostszabbixActuals);
//
// // setBaseRestResponse
// // String body = "
// //
// //
// //
// {\"jsonrpc\":\"2.0\",\"result\":[{\"groupid\":\"1\",\"name\":\"Templates\",\"internal\":\"0\",\"flags\":\"0\"},\"id\":1}
// // ";
// String body =
// "{\"jsonrpc\":\"2.0\",\"result\":[{\"hostid\":\"10118\",\"proxy_hostid\":\"10115\",\"host\":\"vm-stack-dbaas-1-31-dbtest31\",\"status\":\"0\",\"name\":\"vm-stack-dbaas-1-31-dbtest31\",\"groups\":[{\"groupid\":\"15\",\"name\":\"Workgroup-1\"}],\"parentTemplates\":[{\"host\":\"TemplateActiveApache\",\"status\":\"3\"}},\"id\":1}";
// RestMessage originalRestMessage = new RestMessage(null, body);
// originalRestMessage.setBody(body);
// // JavaType resultClass = new JavaType(new
// // ArrayList<ZabbixMonitoredHostResponseV2_4>().getClass(),
// // ZabbixMonitoredHostResponseV2_4.class);
//
// BaseRestResponseResult baseResponseExpected = new
// BaseRestResponseResult(javax.ws.rs.core.Response.Status.OK,
// resultRPCzabbix, null, originalRestMessage);
//
// // Mock Actual response method
// MonitoringWrappedResponsePaas responseExpected = new
// MonitoringWrappedResponsePaas();
// List<Group> groups = new ArrayList<>();
// Group group = new Group();
// group.setGroupName("Workgroup-1");
// List<PaasMachine> paasMachines = new ArrayList<>();
// PaasMachine paasMachine = new PaasMachine();
// paasMachine.setMachineName("testMachine");
// paasMachine.setIp("127.0.0.1");
// paasMachine.setServiceCategory("TestCategory");
// paasMachine.setServiceId("Id");
// Service service = new Service();
// service.setServiceName("dbaas");
// PaaSMetric paasMetric = new PaaSMetric();
// List<String> historyclocks = new ArrayList<>();
// historyclocks.add("00000");
// paasMetric.setHistoryClock(historyclocks);
// List<Float> historyValues = new ArrayList<>();
// historyValues.add(Float.valueOf("5.5"));
// paasMetric.setHistoryValues(historyValues);
// paasMetric.setMetricKey("testkey");
// paasMetric.setMetricName("testMetric");
// paasMetric.setMetricTime(null);
// paasMetric.setMetricValue(Float.valueOf("5.5"));
// paasMetric.setMetricUnit("testUnit");
// PaasThreshold paasThreshold = new PaasThreshold();
// paasThreshold.setThresholdExpression("testExpression");
// paasThreshold.setThresholdName("testThreshold");
// paasThreshold.setThresholdStatus(null);
// List<PaasThreshold> paasThresholds = new ArrayList<>();
// paasThresholds.add(paasThreshold);
// List<PaaSMetric> paasMetrics = new ArrayList<>();
// paasMetrics.add(paasMetric);
// paasMetric.setPaasThresholds(paasThresholds);
// service.setPaasMetrics(paasMetrics);
// List<Service> services = new ArrayList<>();
// paasMachine.setServices(services);
// group.setPaasMachines(paasMachines);
// groups.add(group);
// responseExpected.setGroups(groups);
//
// Mockito.when(restClientFactory.getRestClient()).thenReturn(restClient);
//
// Mockito.when(restClient.getRequest(Mockito.any(String.class),
// Mockito.any(MultivaluedMap.class),
// Mockito.any(RestResponseDecoder.class),
// Mockito.any(RestResponseDecodeStrategy.class)))
// .thenReturn(baseResponseExpected);
//
// MonitoringWrappedResponsePaas actualValue =
// apiclientMocked.getHostsExtended2_4();
//
// assertEquals(responseExpected, actualValue);
//
// }
//
// // @Test(expected = RuntimeException.class)
// // public void GetCreationGroups()
// // throws JsonParseException, JsonMappingException, APIErrorException,
// // MappingException, IOException,
// // RestClientException, NoMappingModelFoundException,
// // ServerErrorResponseException {
// //
// // MonitoringPillarAPIClient apiclientMocked =
// // mock(MonitoringPillarAPIClient.class);
// // apiclientMocked = new MonitoringPillarAPIClient(urlTestDev);
// //
// // Mockito.doThrow(new
// // RuntimeException()).when(apiclientMocked).createGroup(apdapterTestDev,
// // zone,
// // "group-created-test");
// // }
// }
