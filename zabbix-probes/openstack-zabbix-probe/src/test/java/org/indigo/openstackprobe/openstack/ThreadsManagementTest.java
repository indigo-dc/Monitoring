package org.indigo.openstackprobe.openstack;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.client.IOSClientBuilder.V2;
import org.openstack4j.api.compute.ComputeService;
import org.openstack4j.api.image.ImageService;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.identity.Token;
import org.openstack4j.model.image.Image;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.beans.AppOperation;

public class ThreadsManagementTest {
	private Client mockCmdb;
	private Client mockCmdbFail;
	private CmdbClient cmdbClientMock;

	private OpenstackThread hostThreadMocked;
	private OpenstackCollector collectorMocked;
	private ProviderSearch providerSearch;
	private CloudProviderInfo providermocked;
	private Token tokenmocked;
	List<CloudProviderInfo> providersMocked = new ArrayList<>();
	private OSClient mockOSClient;
	private OpenStackClient osclient;
	private Token mockToken;
	private ComputeService computeMocked;
	private ImageService imageServiceMocked;
	private ActionResponse actionMocked;

	private String imageId;
	private String flavorId;
	private String virtualMachineTest;
	private String flavorMockId = "";
	private String imageMockId = "";
	private ArrayList<Flavor> flavorsSimulated;
	private ArrayList<Image> imagesSimulated;

	private static final String token = UUID.randomUUID().toString();
	private static final String tenant = "tenantTest";
	private static final String password = "passwordTest";
	private static final String user = "userTest";
	private static final String endpoint = "endpoint";
	private V2 keystoneMock;
	private static final String cmdUrlMoked = "http://indigo.cloud.plgrid.pl/cmdb";

	private OSClient osClientmocked;

	List<OpenstackCollector> collectors = new ArrayList<>();

	// private OpenStackClient openstackclientMocked;

	@Before
	public void prepareMockElements() throws Exception {
		System.out.println("Setting up testing global environment...");

		// Define KeystoneMock
		keystoneMock = Mockito.mock(V2.class);
		mockOSClient = Mockito.mock(OSClient.class);
		computeMocked = Mockito.mock(ComputeService.class);
		mockToken = Mockito.mock(Token.class);
		Mockito.when(keystoneMock.endpoint(Mockito.anyString())).thenReturn(keystoneMock);
		Mockito.when(keystoneMock.credentials(Mockito.anyString(), Mockito.anyString())).thenReturn(keystoneMock);
		Mockito.when(keystoneMock.tenantName(Mockito.anyString())).thenReturn(keystoneMock);
		Mockito.when(keystoneMock.authenticate()).thenReturn(mockOSClient);
		Mockito.when(mockOSClient.getToken()).thenReturn(mockToken);
		Mockito.when(mockOSClient.compute()).thenReturn(computeMocked);
		Mockito.when(mockToken.getId()).thenReturn(token);

		// openstackclientMocked = Mockito.mock(OpenStackClient.class);
		// PropertiesManager propmanagerMocked =
		// Mockito.mock(PropertiesManager.class);
		// Mockito.doThrow(IOException.class).when(propmanagerMocked);
		// PropertiesManager.loadProperties("openstackprobe.properties");
		osClientmocked = Mockito.mock(OSClient.class);
		tokenmocked = Mockito.mock(Token.class);

		Mockito.when(osClientmocked.getToken()).thenReturn(tokenmocked);
		Mockito.when(tokenmocked.getId()).thenReturn("tokenIdtest");

		System.setProperty("jsse.enableSNIExtension", "false");
		System.setProperty("javax.net.ssl.trustStore", "C:/Program Files/Java/jdk1.8.0_121/jre/lib/security/cacerts");

		// Define the main mock classes for complete result
		mockCmdb = Mockito.mock(Client.class);
		WebTarget targetList = Mockito.mock(WebTarget.class);
		WebTarget targetDetails = Mockito.mock(WebTarget.class);
		Invocation.Builder invocationBuilderList = Mockito.mock(Invocation.Builder.class);
		Invocation.Builder invocationBuilderDetails = Mockito.mock(Invocation.Builder.class);
		Response responseGetList = Mockito.mock(Response.class);
		Response responseGetDetails = Mockito.mock(Response.class);
		// PropertiesManager propertiesManager =
		// Mockito.mock(PropertiesManager.class);

		// Define main relationships for the Client class for getting providers
		Mockito.when(mockCmdb.target(Mockito.endsWith("/provider/list"))).thenReturn(targetList);
		Mockito.when(mockCmdb.target(Mockito.endsWith("include_docs=true"))).thenReturn(targetDetails);
		Mockito.when(targetList.request()).thenReturn(invocationBuilderList);
		Mockito.when(targetDetails.request()).thenReturn(invocationBuilderDetails);

		// Define mock response for GET list of providers(complete result)
		Mockito.when(invocationBuilderList.get()).thenReturn(responseGetList);
		Mockito.when(responseGetList.getStatus()).thenReturn(200);
		String listResponse = "{\"total_rows\":738,\"offset\":20,\"rows\":["
				+ "{\"id\":\"provider-100IT\",\"key\":[\"provider\"],\"value\":{\"name\":\"100IT\"}},"
				+ "{\"id\":\"provider-RECAS-BARI\",\"key\":[\"provider\"],\"value\":{\"name\":\"RECAS-BARI\"}}]}";
		Mockito.when(responseGetList.readEntity(String.class)).thenReturn(listResponse);

		// Define mock response for GET details (complete result)
		Mockito.when(invocationBuilderDetails.get()).thenReturn(responseGetDetails);
		Mockito.when(responseGetDetails.getStatus()).thenReturn(200);
		String detailResponse = "{\"total_rows\":60,\"offset\":49,\"rows\":["
				+ "{\"id\":\"4401ac5dc8cfbbb737b0a025758cfd60\",\"key\":[\"provider-RECAS-BARI\",\"services\"],\"value\":"
				+ "{\"sitename\":\"RECAS-BARI\",\"provider_id\":\"provider-RECAS-BARI\",\"hostname\":\"cloud.recas.ba.infn.it\","
				+ "\"type\":\"compute\"},\"doc\":{\"_id\":\"4401ac5dc8cfbbb737b0a025758cfd60\",\"_rev\":\"2-6540bc334d76090c53399c7bd5bc0aae\","
				+ "\"data\":{\"primary_key\":\"8015G0\",\"hostname\":\"cloud.recas.ba.infn.it\","
				+ "\"gocdb_portal_url\":\"https://goc.egi.eu/portal/index.php?Page_Type=Service&id=8015\","
				+ "\"hostdn\":\"/C=IT/O=INFN/OU=Host/L=Bari/CN=cloud.recas.ba.infn.it\",\"beta\":\"N\",\"service_type\":\"eu.egi.cloud.vm-management.openstack\","
				+ "\"core\":null,\"in_production\":\"Y\",\"node_monitored\":\"Y\",\"sitename\":\"RECAS-BARI\",\"country_name\":\"Italy\",\"country_code\":\"IT\",\"roc_name\":\"NGI_IT\",\"scopes\""
				+ ":{\"scope\":[\"EGI\",\"wlcg\",\"lhcb\"]},\"extensions\":null,\"type\":\"compute\",\"provider_id\":\"provider-RECAS-BARI\","
				+ "\"endpoint\":\"http://cloud.recas.ba.infn.it:8774/\"},\"type\":\"service\"}},"
				+ "{\"id\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\",\"key\":[\"provider-RECAS-BARI\",\"services\"],\"value\":{\"provider_id\":\"provider-RECAS-BARI\",\"type\":\"compute\"},\"doc\":{\"_id\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\",\"_rev\":\"1-256d36283315ea9bb045e6d5038657b6\",\"data\":{\"service_type\":\"eu.egi.cloud.vm-management.openstack\",\"endpoint\":\"http://cloud.recas.ba.infn.it:5000/v2.0\",\"provider_id\":\"provider-RECAS-BARI\",\"type\":\"compute\"},\"type\":\"service\"}},"
				+ "{\"id\":\"7efc59c5db69ea67c5100de0f73ab567\",\"key\":[\"provider-RECAS-BARI\",\"services\"],\"value\":{\"provider_id\":\"provider-RECAS-BARI\",\"type\":\"storage\"},\"doc\":{\"_id\":\"7efc59c5db69ea67c5100de0f73ab567\",\"_rev\":\"4-6e2921c359fb76118616e26c7de76397\",\"data\":{\"service_type\":\"eu.egi.cloud.storage-management.oneprovider\",\"endpoint\":\"E1u8A4FgR6C1UgbD2JOoP9OQIG43q-zDsXkx1PoaaI4\",\"provider_id\":\"provider-RECAS-BARI\",\"type\":\"storage\"},\"type\":\"service\"}}]}";
		Mockito.when(responseGetDetails.readEntity(String.class)).thenReturn(detailResponse);

		// Define main relationships for the Client class Images
		Mockito.when(mockCmdb.target(Mockito.endsWith("/image/list"))).thenReturn(targetList);
		Mockito.when(mockCmdb.target(Mockito.endsWith("include_docs=true"))).thenReturn(targetDetails);
		Mockito.when(targetList.request()).thenReturn(invocationBuilderList);
		Mockito.when(targetDetails.request()).thenReturn(invocationBuilderDetails);

		// Define mock response for GET list of images(complete result)
		Mockito.when(invocationBuilderList.get()).thenReturn(responseGetList);
		Mockito.when(responseGetList.getStatus()).thenReturn(200);
		String listImagesResponse = "{\"total_rows\":72,\"offset\":6,\"rows\":[{\"id\":\"7efc59c5db69ea67c5100de0f726d41e\",\"key\":"
				+ "[\"4401ac5dc8cfbbb737b0a02575e6f4bc\",\"images\"],\"value\":{\"image_id\":\"303d8324-69a7-4372-be24-1d68703affd7\","
				+ "\"image_name\":\"indigodatacloud/ubuntu-sshd:14.04-devel\",\"service\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\"},\"doc\":{\"_id\":\"7efc59c5db69ea67c5100de0f726d41e\","
				+ "\"_rev\":\"1-583b38e80f989b7f39b8ddd5d28c4c76\",\"type\":\"image\",\"data\":{\"image_id\":\"303d8324-69a7-4372-be24-1d68703affd7\","
				+ "\"image_name\":\"indigodatacloud/ubuntu-sshd:14.04-devel\",\"architecture\":\"x86_64\",\"type\":\"linux\","
				+ "\"distribution\":\"ubuntu\",\"version\":\"14.04\",\"service\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\"}}},"
				+ "{\"id\":\"7efc59c5db69ea67c5100de0f726e0a0\",\"key\":[\"4401ac5dc8cfbbb737b0a02575e6f4bc\",\"images\"],\"value\":{\"image_id\":\"0de96743-4a12-4470-b8b2-6dc260977a40\",\"image_name\":\"indigodatacloud/centos-sshd:7-devel\",\"service\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\"},"
				+ "\"doc\":{\"_id\":\"7efc59c5db69ea67c5100de0f726e0a0\",\"_rev\":\"1-948dae5f4b7e1096036af3f0cca37f89\",\"type\":\"image\",\"data\":"
				+ "{\"image_id\":\"0de96743-4a12-4470-b8b2-6dc260977a40\",\"image_name\":\"indigodatacloud/centos-sshd:7-devel\",\"architecture\":\"x86_64\","
				+ "\"type\":\"linux\",\"distribution\":\"centos\",\"version\":\"7\",\"service\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\"}}},{\"id\":\"7efc59c5db69ea67c5100de0f72ca01a\","
				+ "\"key\":[\"4401ac5dc8cfbbb737b0a02575e6f4bc\",\"images\"],\"value\":{\"image_id\":\"303d8324-69a7-4372-be24-1d68703affd7\","
				+ "\"image_name\":\"linux-ubuntu-14.04-vmi\",\"service\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\"},\"doc\":{\"_id\":\"7efc59c5db69ea67c5100de0f72ca01a\","
				+ "\"_rev\":\"1-458cb41b7747761b8bb8f27ad561958a\",\"type\":\"image\",\"data\":{\"image_id\":\"303d8324-69a7-4372-be24-1d68703affd7\","
				+ "\"image_name\":\"linux-ubuntu-14.04-vmi\",\"architecture\":\"x86_64\",\"type\":\"linux\",\"distribution\":\"ubuntu\",\"version\":\"14.04\","
				+ "\"service\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\"}}},{\"id\":\"7efc59c5db69ea67c5100de0f751dfa2\",\"key\":[\"4401ac5dc8cfbbb737b0a02575e6f4bc\","
				+ "\"images\"],\"value\":{\"image_id\":\"0de96743-4a12-4470-b8b2-6dc260977a40\",\"image_name\":\"linux-centos-7-vmi\","
				+ "\"service\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\"},\"doc\":{\"_id\":\"7efc59c5db69ea67c5100de0f751dfa2\","
				+ "\"_rev\":\"2-945794a2ee09317dc89b52f507bbe11f\",\"type\":\"image\",\"data\":{\"image_id\":\"0de96743-4a12-4470-b8b2-6dc260977a40\","
				+ "\"image_name\":\"linux-centos-7-vmi\",\"architecture\":\"x86_64\",\"type\":\"linux\",\"distribution\":\"centos\","
				+ "\"version\":\"7\",\"service\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\"}}},{\"id\":\"7efc59c5db69ea67c5100de0f7706af4\","
				+ "\"key\":[\"4401ac5dc8cfbbb737b0a02575e6f4bc\",\"images\"],\"value\":{\"image_id\":\"867bdfd7-7a97-4ef5-bfa8-9f7d05958239\","
				+ "\"image_name\":\"linux-ubuntu-14.04-mesos-vmi\",\"service\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\"},"
				+ "\"doc\":{\"_id\":\"7efc59c5db69ea67c5100de0f7706af4\",\"_rev\":\"1-cc4a0c7af11bde8237663181f5800f53\","
				+ "\"type\":\"image\",\"data\":{\"image_id\":\"867bdfd7-7a97-4ef5-bfa8-9f7d05958239\","
				+ "\"image_name\":\"linux-ubuntu-14.04-mesos-vmi\",\"architecture\":\"x86_64\",\"type\":\"linux\","
				+ "\"distribution\":\"ubuntu\",\"version\":\"14.04\",\"service\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\"}}}]}";
		Mockito.when(responseGetList.readEntity(String.class)).thenReturn(listImagesResponse);

		// Define mocks for CMDB failure
		mockCmdbFail = Mockito.mock(Client.class);
		WebTarget targetListFail = Mockito.mock(WebTarget.class);
		WebTarget targetDetailsFail = Mockito.mock(WebTarget.class);
		Invocation.Builder invocationBuilderListFail = Mockito.mock(Invocation.Builder.class);
		Invocation.Builder invocationBuilderDetailsFail = Mockito.mock(Invocation.Builder.class);
		Response responseGetListFail = Mockito.mock(Response.class);
		Response responseGetDetailsFail = Mockito.mock(Response.class);

		// Define main relationships for the Client Failure class
		Mockito.when(mockCmdbFail.target(Mockito.endsWith("/provider/list"))).thenReturn(targetListFail);
		Mockito.when(mockCmdbFail.target(Mockito.endsWith("include_docs=true"))).thenReturn(targetDetailsFail);
		Mockito.when(targetListFail.request()).thenReturn(invocationBuilderListFail);
		Mockito.when(targetDetailsFail.request()).thenReturn(invocationBuilderDetailsFail);

		// Define mock response for GET list Fail (complete result)
		Mockito.when(invocationBuilderListFail.get()).thenReturn(responseGetListFail);
		Mockito.when(responseGetListFail.getStatus()).thenReturn(200);
		String listResponseFail = "{\"total_rows\":0,\"offset\":20,\"rows\":[]}";
		Mockito.when(responseGetListFail.readEntity(String.class)).thenReturn(listResponseFail);

		// Define mock response for GET details (complete result)
		Mockito.when(invocationBuilderDetailsFail.get()).thenReturn(responseGetDetailsFail);
		Mockito.when(responseGetDetailsFail.getStatus()).thenReturn(200);
		// String detailResponseFail =
		// "{\"total_rows\":60,\"offset\":49,\"rows\":["
		// +
		// "{\"id\":\"4401ac5dc8cfbbb737b0a025758cfd60\",\"key\":[\"provider-RECAS-BARI\",\"services\"],\"value\":{\"sitename\":\"RECAS-BARI\",\"provider_id\":\"provider-RECAS-BARI\",\"hostname\":\"cloud.recas.ba.infn.it\",\"type\":\"compute\"},\"doc\":{\"_id\":\"4401ac5dc8cfbbb737b0a025758cfd60\",\"_rev\":\"2-6540bc334d76090c53399c7bd5bc0aae\",\"data\":{\"primary_key\":\"8015G0\",\"hostname\":\"cloud.recas.ba.infn.it\",\"gocdb_portal_url\":\"https://goc.egi.eu/portal/index.php?Page_Type=Service&id=8015\",\"hostdn\":\"/C=IT/O=INFN/OU=Host/L=Bari/CN=cloud.recas.ba.infn.it\",\"beta\":\"N\",\"service_type\":\"eu.egi.cloud.vm-management.noocci\",\"core\":null,\"in_production\":\"Y\",\"node_monitored\":\"Y\",\"sitename\":\"RECAS-BARI\",\"country_name\":\"Italy\",\"country_code\":\"IT\",\"roc_name\":\"NGI_IT\",\"scopes\":{\"scope\":[\"EGI\",\"wlcg\",\"lhcb\"]},\"extensions\":null,\"type\":\"compute\",\"provider_id\":\"provider-RECAS-BARI\",\"endpoint\":\"http://cloud.recas.ba.infn.it:8774/\"},\"type\":\"service\"}}]}";
		String detailResponseFail = "{\"total_rows\":60,\"offset\":49,\"rows\":[]}";
		Mockito.when(responseGetDetailsFail.readEntity(String.class)).thenReturn(detailResponseFail);

		// Mock CMDB client
		cmdbClientMock = Mockito.mock(CmdbClient.class);
		Mockito.when(cmdbClientMock.getProvidersList())
				.thenReturn(new String[] { "provider-RECAS-BARI", "provider-UPV-GRyCAP" });
		CloudProviderInfo testProvider = new CloudProviderInfo(
				"provider-RECAS-BARI", /* "http://cloud.recas.ba.infn.it:8774/", */
				"http://cloud.recas.ba.infn.it:5000/v2.0", 0, true, false, true);
		Mockito.when(cmdbClientMock.getProviderData(Mockito.matches("provider-RECAS-BARI"))).thenReturn(testProvider);
		// Mockito.when(cmdbClientMock.getProviderData(Mockito.matches("provider-UPV-GRyCAP")))
		// .thenReturn(null);
		ArrayList<CloudProviderInfo> testList = new ArrayList<CloudProviderInfo>();
		testList.add(testProvider);
		Mockito.when(cmdbClientMock.getFeasibleProvidersInfo()).thenReturn(testList);
		String[] testListImages = new String[2];
		Mockito.when(cmdbClientMock.getImageList())
				.thenReturn(new String[] { "linux-ubuntu-14.04-vmi", "linux-ubuntu-14.04-mesos-vmi" });
		
		// Mock Openstack Client
		OpenStackClient mockOpenstack = Mockito.mock(OpenStackClient.class);
		// Generate output object
		CreateVmResult create = new CreateVmResult(1, 200, 1429, "testVM");
		InspectVmResult inspect = new InspectVmResult(1, 200, 426);
		DeleteVmResult delete = new DeleteVmResult(1, 204, 612);
		OpenstackProbeResult global = new OpenstackProbeResult(1, 204, 2467, "provider-RECAS-BARI");
		global.addCreateVmInfo(create);
		global.addInspectVmInfo(inspect);
		global.addDeleteVmInfo(delete);
		Mockito.when(mockOpenstack.getOpenstackMonitoringInfo()).thenReturn(global);

		// Mock the list of providers
		providermocked = Mockito.mock(CloudProviderInfo.class);
		Mockito.when(providermocked.getCloudType()).thenReturn(0);
		Mockito.when(providermocked.getIsBeta()).thenReturn(true);
		Mockito.when(providermocked.getProviderId()).thenReturn("provider-RECAS-BARI");
		Mockito.when(providermocked.getKeystoneEndpoint()).thenReturn("keystoneEndpoint");
		providersMocked.add(providermocked);
		Mockito.when(cmdbClientMock.getFeasibleProvidersInfo()).thenReturn(providersMocked);

		// Mock Openstack
		OpenStackClient osclientMocked = Mockito.mock(OpenStackClient.class);
		CreateVmResult createVmResultmocked = Mockito.mock(CreateVmResult.class);
		Mockito.when(createVmResultmocked.getVmId()).thenReturn("vmid");
		Mockito.when(osclientMocked.createVm()).thenReturn(createVmResultmocked);

		// Mock the final results
		hostThreadMocked = Mockito.mock(OpenstackThread.class);
		collectorMocked = Mockito.mock(OpenstackCollector.class);
		AppOperation appoperationMocked = Mockito.mock(AppOperation.class);
		Map<String, String> metricMapMocked = new HashMap<>();
		metricMapMocked.put("MetricNameTest", "MetricValueTest");
		Mockito.when(appoperationMocked.getMetrics()).thenReturn(metricMapMocked);
		Mockito.when(appoperationMocked.getStatus()).thenReturn(200);
		Mockito.when(collectorMocked.clear()).thenReturn(appoperationMocked);
		Mockito.when(hostThreadMocked.createCollector()).thenReturn(collectorMocked);
		providerSearch = Mockito.mock(ProviderSearch.class);
		collectors.add(collectorMocked);

		System.out.println("Testing environment ready, finished mocking components!");
		
		PropertiesManager.loadProperties(new InputStreamReader(
		        this.getClass().getResourceAsStream("/testprobe.properties")));
	}

	@Test
	public void threadsManagementShouldWorkFine() {

		OpenstackThread mockThread = new OpenstackThread(collectors);
		OpenstackCollector oscollectorMocked = collectors.get(0);
		assertSame(collectorMocked, mockThread.createCollector());
	}

	@Test
	public void threadsManagementShouldReturnProvidersFine() throws IOException {

		// Run the probe code
//		CmdbClient cmdbClient = new CmdbClient(mockCmdb, cmdUrlMoked);
		
		providerSearch = new ProviderSearch(providersMocked, collectorMocked, cmdbClientMock, providermocked,
				osClientmocked);
		List<OpenstackCollector> collectorlist = providerSearch.getCollectorResults();
		collectorlist.add(collectorMocked);
		OpenstackThread mockThread = new OpenstackThread(collectorlist);
		Assert.assertEquals(1, collectorlist.size());
		assertNotNull(mockThread.createCollector());
	}

	@Test
	public void threadsManagementShouldFail() {

		// Run the probe code
		// OpenstackThread mockThread = new OpenstackThread();
		collectors = new ArrayList<>();
		OpenstackThread mockThread = new OpenstackThread(collectors);
		assertNull(mockThread.createCollector());
	}

	@Test(expected = IOException.class)
	public void CmdbConstructorTestFail() throws IOException {
		PropertiesManager propertiesManager = Mockito.mock(PropertiesManager.class);
		String testConfigFile = "configFile";
		String cmdburl = "dburl";
		PropertiesManager.loadProperties(testConfigFile);
		String prop = propertiesManager.getProperty(testConfigFile);
	}

	@Test()
	public void CmdbConstructorTestSuccess() {
		PropertiesManager propertiesManager = Mockito.mock(PropertiesManager.class);

		String testConfigFile = OpenstackProbeTags.CONFIG_FILE;
		String cmdburl = OpenstackProbeTags.CMDB_URL;

		String prop = PropertiesManager.getProperty(testConfigFile);

		Assert.assertEquals("the url of cmdb should be", prop, PropertiesManager.getProperty(testConfigFile));
	}

	@Test
	public void cmdbAccessShouldFail() {
		// Create the CMDB client with Mock
		CmdbClient myTestClient = new CmdbClient(mockCmdbFail, cmdUrlMoked);

		// Try to list providers and to get info from one of them
		String[] testList = myTestClient.getProvidersList();
		CloudProviderInfo testInfo = myTestClient.getProviderData("provider-RECAS-BARI");

		Assert.assertEquals("The number of providers in the list should be 0.", 0, testList.length);
		Assert.assertNull("The CloudProviderInfo object should be null.", testInfo);
	}

	@Test
	public void cmdbAccessShouldWorkFine() {
		// Create the CMDB client with Mock
		OpenStackClient mockOpenstack = Mockito.mock(OpenStackClient.class);
		CmdbClient myTestClient = new CmdbClient(mockCmdb, cmdUrlMoked);

		// Mockito.when(myTestClient.getImageList()).thenReturn(new
		// String[]{"linux-ubuntu-14.04-vmi", "linux-ubuntu-14.04-mesos-vmi"});
		String[] testListImages = myTestClient.getImageList();

		// Try to list providers and to get info from one of them
		String[] testList = cmdbClientMock.getProvidersList();
		CloudProviderInfo testInfo = myTestClient.getProviderData("provider-RECAS-BARI");

		List<CloudProviderInfo> providersInfos = new ArrayList<>();
		providersInfos.add(testInfo);
		providersInfos = myTestClient.getFeasibleProvidersInfo();

		List<OpenstackCollector> collectors = new ArrayList<>();
		OpenstackCollector collectorMocked = Mockito.mock(OpenstackCollector.class);

		collectors.add(collectorMocked);

		Assert.assertEquals("The number of provider Info should be:  ", 5, providersInfos.size());

		Assert.assertEquals("The number of images in the list should be: ", 5, testListImages.length);

		Assert.assertEquals("The number of providers in the list should be 0.", 2, testList.length);
		Assert.assertNotNull("The CloudProviderInfo object should not be null.", testInfo);
		System.out.println(testInfo.getKeystoneEndpoint());
		Assert.assertEquals("The keystone endpoint should be the right one.", "http://cloud.recas.ba.infn.it:5000/v2.0",
				testInfo.getKeystoneEndpoint());
		Assert.assertTrue("The client should have parsed that production is Y.", testInfo.getIsProduction());
		Assert.assertTrue("The client should have parsed that production is Y.", testInfo.getIsMonitored());
		Assert.assertNotNull("The client should have parsed that production is Y.", testInfo.getProviderId());
		Assert.assertNotNull("The client should have parsed that production is Y.", testInfo.getIsBeta());
		Assert.assertEquals("The client should have parsed that production is Y.", 0, testInfo.getCloudType());
	}

}