package org.indigo.occiprobe.openstack.test;

import org.indigo.occiprobe.openstack.CloudProviderInfo;
import org.indigo.occiprobe.openstack.CmdbClient;
import org.indigo.occiprobe.openstack.CreateVmResult;
import org.indigo.occiprobe.openstack.DeleteVmResult;
import org.indigo.occiprobe.openstack.InspectVmResult;
import org.indigo.occiprobe.openstack.OcciProbeResult;
import org.indigo.occiprobe.openstack.OpenStackOcciClient;
import org.indigo.occiprobe.openstack.ProbeThread;
import org.indigo.occiprobe.openstack.ZabbixSender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class ThreadsManagementTest 
{
	private Client mockCmdb;
	private Client mockCmdbFail;
	private ScheduledExecutorService schedulerMock;
	private ZabbixSender mockSender;
	private CmdbClient cmdbClientMock;
	
	@Before
	public void prepareMockElements() 
	{
		System.out.println ("Setting up testing environment...");		
		
		// Define the main mock classes for complete result
		mockCmdb = Mockito.mock(Client.class);
		WebTarget targetList = Mockito.mock(WebTarget.class);
		WebTarget targetDetails = Mockito.mock(WebTarget.class);
		Invocation.Builder invocationBuilderList = Mockito.mock(Invocation.Builder.class);
		Invocation.Builder invocationBuilderDetails = Mockito.mock(Invocation.Builder.class);
		Response responseGetList = Mockito.mock(Response.class);
		Response responseGetDetails = Mockito.mock(Response.class);
		
		// Define main relationships for the Client class
		Mockito.when(mockCmdb.target(Mockito.endsWith("/provider/list"))).thenReturn(targetList);
		Mockito.when(mockCmdb.target(Mockito.endsWith("include_docs=true"))).thenReturn(targetDetails);
		Mockito.when(targetList.request()).thenReturn(invocationBuilderList);
		Mockito.when(targetDetails.request()).thenReturn(invocationBuilderDetails);
		
		// Define mock response for GET list (complete result)
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
				+ "{\"id\":\"4401ac5dc8cfbbb737b0a025758cfd60\",\"key\":[\"provider-RECAS-BARI\",\"services\"],\"value\":{\"sitename\":\"RECAS-BARI\",\"provider_id\":\"provider-RECAS-BARI\",\"hostname\":\"cloud.recas.ba.infn.it\",\"type\":\"compute\"},\"doc\":{\"_id\":\"4401ac5dc8cfbbb737b0a025758cfd60\",\"_rev\":\"2-6540bc334d76090c53399c7bd5bc0aae\",\"data\":{\"primary_key\":\"8015G0\",\"hostname\":\"cloud.recas.ba.infn.it\",\"gocdb_portal_url\":\"https://goc.egi.eu/portal/index.php?Page_Type=Service&id=8015\",\"hostdn\":\"/C=IT/O=INFN/OU=Host/L=Bari/CN=cloud.recas.ba.infn.it\",\"beta\":\"N\",\"service_type\":\"eu.egi.cloud.vm-management.occi\",\"core\":null,\"in_production\":\"Y\",\"node_monitored\":\"Y\",\"sitename\":\"RECAS-BARI\",\"country_name\":\"Italy\",\"country_code\":\"IT\",\"roc_name\":\"NGI_IT\",\"scopes\":{\"scope\":[\"EGI\",\"wlcg\",\"lhcb\"]},\"extensions\":null,\"type\":\"compute\",\"provider_id\":\"provider-RECAS-BARI\",\"endpoint\":\"http://cloud.recas.ba.infn.it:8787/occi\"},\"type\":\"service\"}},"
				+ "{\"id\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\",\"key\":[\"provider-RECAS-BARI\",\"services\"],\"value\":{\"provider_id\":\"provider-RECAS-BARI\",\"type\":\"compute\"},\"doc\":{\"_id\":\"4401ac5dc8cfbbb737b0a02575e6f4bc\",\"_rev\":\"1-256d36283315ea9bb045e6d5038657b6\",\"data\":{\"service_type\":\"eu.egi.cloud.vm-management.openstack\",\"endpoint\":\"http://cloud.recas.ba.infn.it:5000/v2.0\",\"provider_id\":\"provider-RECAS-BARI\",\"type\":\"compute\"},\"type\":\"service\"}},"
				+ "{\"id\":\"7efc59c5db69ea67c5100de0f73ab567\",\"key\":[\"provider-RECAS-BARI\",\"services\"],\"value\":{\"provider_id\":\"provider-RECAS-BARI\",\"type\":\"storage\"},\"doc\":{\"_id\":\"7efc59c5db69ea67c5100de0f73ab567\",\"_rev\":\"4-6e2921c359fb76118616e26c7de76397\",\"data\":{\"service_type\":\"eu.egi.cloud.storage-management.oneprovider\",\"endpoint\":\"E1u8A4FgR6C1UgbD2JOoP9OQIG43q-zDsXkx1PoaaI4\",\"provider_id\":\"provider-RECAS-BARI\",\"type\":\"storage\"},\"type\":\"service\"}}]}";
		Mockito.when(responseGetDetails.readEntity(String.class)).thenReturn(detailResponse);
		
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
		//String detailResponseFail = "{\"total_rows\":60,\"offset\":49,\"rows\":[" 
		//		+ "{\"id\":\"4401ac5dc8cfbbb737b0a025758cfd60\",\"key\":[\"provider-RECAS-BARI\",\"services\"],\"value\":{\"sitename\":\"RECAS-BARI\",\"provider_id\":\"provider-RECAS-BARI\",\"hostname\":\"cloud.recas.ba.infn.it\",\"type\":\"compute\"},\"doc\":{\"_id\":\"4401ac5dc8cfbbb737b0a025758cfd60\",\"_rev\":\"2-6540bc334d76090c53399c7bd5bc0aae\",\"data\":{\"primary_key\":\"8015G0\",\"hostname\":\"cloud.recas.ba.infn.it\",\"gocdb_portal_url\":\"https://goc.egi.eu/portal/index.php?Page_Type=Service&id=8015\",\"hostdn\":\"/C=IT/O=INFN/OU=Host/L=Bari/CN=cloud.recas.ba.infn.it\",\"beta\":\"N\",\"service_type\":\"eu.egi.cloud.vm-management.noocci\",\"core\":null,\"in_production\":\"Y\",\"node_monitored\":\"Y\",\"sitename\":\"RECAS-BARI\",\"country_name\":\"Italy\",\"country_code\":\"IT\",\"roc_name\":\"NGI_IT\",\"scopes\":{\"scope\":[\"EGI\",\"wlcg\",\"lhcb\"]},\"extensions\":null,\"type\":\"compute\",\"provider_id\":\"provider-RECAS-BARI\",\"endpoint\":\"http://cloud.recas.ba.infn.it:8787/occi\"},\"type\":\"service\"}}]}";
		String detailResponseFail = "{\"total_rows\":60,\"offset\":49,\"rows\":[]}";
		Mockito.when(responseGetDetailsFail.readEntity(String.class)).thenReturn(detailResponseFail);
		
		// Scheduler Mock delegating most of the calls to a real object
		// Mock Zabbix Sender		
		mockSender = Mockito.mock(ZabbixSender.class);		
		Mockito.when(mockSender.addMetricToQueue(Mockito.any(OcciProbeResult.class))).thenReturn(true);		
		Mockito.when(mockSender.sendMetrics()).thenReturn(true);
		
		// Mock CMDB client
		cmdbClientMock = Mockito.mock(CmdbClient.class);
		//Mockito.when(cmdbClientMock.getProvidersList()).thenReturn(new String[]{"provider-RECAS-BARI", "provider-UPV-GRyCAP"});
		CloudProviderInfo testProvider = new CloudProviderInfo("provider-RECAS-BARI", "http://cloud.recas.ba.infn.it:8787/occi", "http://cloud.recas.ba.infn.it:5000/v2.0", 0, true, false, true);
		Mockito.when(cmdbClientMock.getProviderData(Mockito.matches("provider-RECAS-BARI"))).thenReturn(testProvider);
		Mockito.when(cmdbClientMock.getProviderData(Mockito.matches("provider-UPV-GRyCAP"))).thenReturn(null);
		ArrayList<CloudProviderInfo> testList = new ArrayList<CloudProviderInfo>();
		testList.add(testProvider);
		Mockito.when(cmdbClientMock.getFeasibleProvidersInfo()).thenReturn(testList);
		
		// Mock OCCI Client
		OpenStackOcciClient mockOcci = Mockito.mock(OpenStackOcciClient.class);
		// Generate output object
		CreateVmResult create = new CreateVmResult (1, 200, 1429, "testVM");
		InspectVmResult inspect = new InspectVmResult (1, 200, 426);
		DeleteVmResult delete = new DeleteVmResult (1, 204, 612);
		OcciProbeResult global = new OcciProbeResult(1, 204, 2467, "TestProvider");
		global.addCreateVmInfo(create);
		global.addInspectVmInfo(inspect);
		global.addDeleteVmInfo(delete);
		Mockito.when(mockOcci.getOcciMonitoringInfo()).thenReturn(global);
		
		// Complete Mock
		/*MonitoringThread mockThread = new MonitoringThread(mockSender, mockOcci, "MockProvider");
		ScheduledExecutorService delegate = Executors.newScheduledThreadPool(2);
		schedulerMock = Mockito.mock(ScheduledExecutorService.class, AdditionalAnswers.delegatesTo(delegate));
		Mockito.doReturn(delegate.schedule(mockThread, 5, TimeUnit.SECONDS)).when(schedulerMock).schedule(Mockito.any(Runnable.class), Mockito.anyLong(), Mockito.eq(TimeUnit.SECONDS));
		*/
		System.out.println("Testing environment ready!");
	}
	
	@Test
	public void threadsManagementShouldWorkFine()
	{
		// Run the probe code
		ProbeThread probeManager = ProbeThread.instance(schedulerMock, mockSender, cmdbClientMock);
		//boolean result = probeManager.startMonitoringProcess();
		
		// Check Results
		//Assert.assertTrue("The result of the whole monitoring operation should be fine.", result);
	}
		
	@Test
	public void cmdbAccessShouldFail()
	{
		// Create the CMDB client with Mock
		CmdbClient myTestClient = new CmdbClient(mockCmdbFail);
		
		// Try to list providers and to get info from one of them
		//String[] testList = myTestClient.getProvidersList();
		CloudProviderInfo testInfo = myTestClient.getProviderData("TestProvider");
		
		//Assert.assertEquals("The number of providers in the list should be 0.", 0, testList.length);
		Assert.assertNull("The CloudProviderInfo object should be null.", testInfo);
	}
	
	@Test
	public void cmdbAccessShouldWorkFine()
	{
		// Create the CMDB client with Mock
		CmdbClient myTestClient = new CmdbClient(mockCmdb);
				
		// Try to list providers and to get info from one of them
		//String[] testList = myTestClient.getProvidersList();
		CloudProviderInfo testInfo = myTestClient.getProviderData("TestProvider");
		
		//Assert.assertEquals("The number of providers in the list should be 0.", 2, testList.length);
		Assert.assertNotNull("The CloudProviderInfo object should not be null.", testInfo);
		Assert.assertEquals("The OCCI endpoint should be the right one.", "http://cloud.recas.ba.infn.it:8787/occi", testInfo.getOcciEndpoint());
		Assert.assertTrue("The client should have parsed that production is Y.", testInfo.getIsProduction());
	}
	
}
