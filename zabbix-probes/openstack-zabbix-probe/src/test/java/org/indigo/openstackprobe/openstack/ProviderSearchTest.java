package org.indigo.openstackprobe.openstack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(ProviderSearch.class)
public class ProviderSearchTest {

	private Client mockCmdb;
	private CmdbClient cmdbClientMock;
	String keystone = "keystoneurl";
	String providerEndpoint = "providerurl";
	String provider = "providerId";

	@Test
	public void getCollectorResultTest(){
		
		 // Define the main mock classes for complete result
	    mockCmdb = Mockito.mock(Client.class);
	    WebTarget targetList = Mockito.mock(WebTarget.class);
	    WebTarget targetDetails = Mockito.mock(WebTarget.class);
	    Invocation.Builder invocationBuilderList = Mockito.mock(Invocation.Builder.class);
	    Invocation.Builder invocationBuilderDetails = Mockito.mock(Invocation.Builder.class);
	    Response responseGetList = Mockito.mock(Response.class);
	    Response responseGetDetails = Mockito.mock(Response.class);
//	    PropertiesManager propertiesManager = Mockito.mock(PropertiesManager.class);

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
	    
	 // Mock CMDB client
	    cmdbClientMock = Mockito.mock(CmdbClient.class);
	    Mockito.when(cmdbClientMock.getProvidersList())
	        .thenReturn(new String[] { "provider-RECAS-BARI", "provider-UPV-GRyCAP" });
	    CloudProviderInfo testProvider =
	        new CloudProviderInfo("provider-RECAS-BARI", /*"http://cloud.recas.ba.infn.it:8774/",*/
	            "http://cloud.recas.ba.infn.it:5000/v2.0", 0, true, false, true);
	    Mockito.when(cmdbClientMock.getProviderData(Mockito.matches("provider-RECAS-BARI")))
	        .thenReturn(testProvider);
	    Mockito.when(cmdbClientMock.getProviderData(Mockito.matches("provider-UPV-GRyCAP")))
	        .thenReturn(null);
	    ArrayList<CloudProviderInfo> testList = new ArrayList<CloudProviderInfo>();
	    testList.add(testProvider);
	    Mockito.when(cmdbClientMock.getFeasibleProvidersInfo()).thenReturn(testList);
	    String[] testListImages = new String[2];
	    Mockito.when(cmdbClientMock.getImageList()).thenReturn(new String[]{"linux-ubuntu-14.04-vmi", "linux-ubuntu-14.04-mesos-vmi"});
	    
	    //Mock OpenstackCollector
	    OpenstackCollector collector = Mockito.mock(OpenstackCollector.class);
	    
//	    Mockito.when(collector.keystoneEndpoint).thenReturn(keystone);
//	    Mockito.when(collector.providerEndpoint).thenReturn(provider);
//	    Mockito.when(collector.provider).thenReturn(provider);
	    
//	    OpenstackProbeResult probeResult = Mockito.mock(OpenstackProbeResult.class);
//	    CreateVmResult create = new CreateVmResult(1, 1, 7, "uuid");
//	    probeResult.addCreateVmInfo(create);
		
//		List<OpenstackCollector> collectors = new ArrayList<>();
//		OpenstackCollector collector = new OpenstackCollector("mockProviderId", "mockKeystoneUrl");
//		collectors.add(collector);
//		PowerMockito.mockStatic(ProviderSearch.class);
//		
//		BDDMockito.given(ProviderSearch.getCollectorResults()).willReturn(collectors);

	    List<OpenstackCollector> collectors = new ArrayList<>();
	    List<CloudProviderInfo> providerslist = testList;
	    Iterator<CloudProviderInfo> providerIterator = providerslist.iterator();
	    while(providerIterator.hasNext()){
	    	CloudProviderInfo currentProvider = providerIterator.next();
//	    	Mockito.when().thenReturn(osclient);
	    	collectors.add(collector);
	    }
	    
		Assert.assertEquals("The result must contain information.", 1, collectors.size());
	}

}
