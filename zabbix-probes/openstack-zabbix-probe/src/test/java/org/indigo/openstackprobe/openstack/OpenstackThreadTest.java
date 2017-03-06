package org.indigo.openstackprobe.openstack;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProviderSearch.class)
public class OpenstackThreadTest {
	
	private Client client;
	  private Client clientFail;
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	CmdbClient cmdbClient;
	


	@Test
	public void createCollectorTest() {
//		
//		// Define mocks for CMDB failure
//	    clientFail = Mockito.mock(Client.class);
//	    WebTarget targetListFail = Mockito.mock(WebTarget.class);
//	    WebTarget targetDetailsFail = Mockito.mock(WebTarget.class);
//	    Invocation.Builder invocationBuilderListFail = Mockito.mock(Invocation.Builder.class);
//	    Invocation.Builder invocationBuilderDetailsFail = Mockito.mock(Invocation.Builder.class);
//	    Response responseGetListFail = Mockito.mock(Response.class);
//	    Response responseGetDetailsFail = Mockito.mock(Response.class);
//
//	    // Define main relationships for the Client Failure class
//	    Mockito.when(clientFail.target(Mockito.endsWith("/provider/list")))
//	        .thenReturn(targetListFail);
//	    Mockito.when(clientFail.target(Mockito.endsWith("include_docs=true")))
//	        .thenReturn(targetDetailsFail);
//	    Mockito.when(targetListFail.request()).thenReturn(invocationBuilderListFail);
//	    Mockito.when(targetDetailsFail.request()).thenReturn(invocationBuilderDetailsFail);
//		
//		// Define the main mock classes for complete result
//	    clientFail = Mockito.mock(Client.class);
//	    WebTarget targetList = Mockito.mock(WebTarget.class);
//	    WebTarget targetDetails = Mockito.mock(WebTarget.class);
//	    Invocation.Builder invocationBuilderList = Mockito.mock(Invocation.Builder.class);
//	    Invocation.Builder invocationBuilderDetails = Mockito.mock(Invocation.Builder.class);
//	    Response responseGetList = Mockito.mock(Response.class);
//	    Response responseGetDetails = Mockito.mock(Response.class);
////	    PropertiesManager propertiesManager = Mockito.mock(PropertiesManager.class);
//
//	    // Define main relationships for the Client class for getting providers
//	    Mockito.when(client.target(Mockito.endsWith("/provider/list"))).thenReturn(targetList);
//	    Mockito.when(client.target(Mockito.endsWith("include_docs=true"))).thenReturn(targetDetails);
//	    Mockito.when(targetList.request()).thenReturn(invocationBuilderList);
//	    Mockito.when(targetDetails.request()).thenReturn(invocationBuilderDetails);
//	    
//	 // Define mock response for GET list of providers(complete result)
//	    Mockito.when(invocationBuilderList.get()).thenReturn(responseGetList);
//	    Mockito.when(responseGetList.getStatus()).thenReturn(200);
//	    String listResponse = "{\"total_rows\":738,\"offset\":20,\"rows\":["
//	        + "{\"id\":\"provider-100IT\",\"key\":[\"provider\"],\"value\":{\"name\":\"100IT\"}},"
//	        + "{\"id\":\"provider-RECAS-BARI\",\"key\":[\"provider\"],\"value\":{\"name\":\"RECAS-BARI\"}}]}";
//	    Mockito.when(responseGetList.readEntity(String.class)).thenReturn(listResponse);
//	    
//	    
//	    List<OpenstackCollector> collectors = new ArrayList<>();
//	    OpenstackCollector collector = new OpenstackCollector("providerId", "providerurl", "keystoneUrl");
//	    collectors.add(collector);
//	    
//	    //given
//        PowerMockito.mockStatic(DriverManager.class);
//        BDDMockito.given(ProviderSearch.getCollectorResults()).willReturn(collectors);
//
//        //when
////        sut.execute();
//
//        //then
//        PowerMockito.verifyStatic();
////        ProviderSearch.getCollectorResults();
//
//		List<CloudProviderInfo> providersInfos = new ArrayList<>();
//		CloudProviderInfo provider = new CloudProviderInfo("recas", "novaendpoint", "keystoneendpoint", 0, true, true,
//				true);
//		providersInfos.add(provider);
//		Mockito.when(cmdbClient.getFeasibleProvidersInfo()).thenReturn(providersInfos);
//		openstackThread = new OpenstackThread();
//		Assert.assertEquals(openstackThread.createCollector(), null);
	}
	
	@Test
	public void mainTest(){
		List<OpenstackCollector> collectors = new ArrayList<>();
		OpenstackCollector collector = new OpenstackCollector("mockProviderId", "mockKeystoneUrl");
		collectors.add(collector);
		PowerMockito.mockStatic(ProviderSearch.class);
		
		BDDMockito.given(ProviderSearch.getCollectorResults()).willReturn(collectors);
	}

}
