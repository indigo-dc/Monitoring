/**
Copyright 2016 ATOS SPAIN S.A.

Licensed under the Apache License, Version 2.0 (the License);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Authors Contact:
Francisco Javier Nieto. Atos Research and Innovation, Atos SPAIN SA
@email francisco.nieto@atos.net
**/

package org.indigo.openstackprobe.openstack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.indigo.openstackprobe.openstack.OpenStackClient;
import org.indigo.openstackprobe.openstack.OpenstackProbeResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.client.IOSClientBuilder.V2;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.identity.Token;
import org.openstack4j.model.image.Image;
import org.openstack4j.openstack.compute.domain.NovaImage;

import jersey.repackaged.com.google.common.collect.Lists;

public class MonitoringTest {
  
  @Mock
	private Client mockClient;
	private Client mockClientFailure;
	private V2 keystoneMock;
	private ServerCreate serverCreate;
	
	private String imageId;
	private String flavorId;
	private String virtualMachineTest;
  private String imageMock = "";
	private String flavorMock = "";
	@InjectMocks Builders builders;
	
	@Before
	public void prepareMockOpenstackServer()
	{
		System.out.println ("Setting up testing environment...");
		
		// Define the main mock classes for complete result
		mockClient = Mockito.mock(Client.class);
		WebTarget target = Mockito.mock(WebTarget.class);
		Invocation.Builder invocationBuilder = Mockito.mock(Invocation.Builder.class);
		Response responsePost = Mockito.mock(Response.class);
		Response responseGet = Mockito.mock(Response.class);
		Response responseDelete = Mockito.mock(Response.class);
				
		// Define main relationships for the Client class
		Mockito.when(mockClient.target(Mockito.anyString())).thenReturn(target);
		Mockito.when(target.request()).thenReturn(invocationBuilder);
		
		// Define mock response for POST (complete result)
		Mockito.when(invocationBuilder.post(null)).thenReturn(responsePost);
		Mockito.when(responsePost.getStatus()).thenReturn(200);
		Mockito.when(responsePost.readEntity(String.class)).thenReturn("Openstack-Location: http://cloud.recas.ba.infn.it:8774/compute/78d9ecb64353402bb621b569891c633a");
		
		// Define mock response for GET (complete result)
		Mockito.when(invocationBuilder.get()).thenReturn(responseGet);
		Mockito.when(responseGet.getStatus()).thenReturn(200);
		Mockito.when(responseGet.readEntity(String.class)).thenReturn("");
		
		// Define mock response for DELETE (complete result)
		Mockito.when(invocationBuilder.delete()).thenReturn(responseDelete);
		Mockito.when(responseDelete.getStatus()).thenReturn(204);
		Mockito.when(responseDelete.readEntity(String.class)).thenReturn("");
		
		// Define the main mock classes for unavailability error
		mockClientFailure = Mockito.mock(Client.class);
		WebTarget targetFailure = Mockito.mock(WebTarget.class);
		Invocation.Builder invocationBuilderFailure = Mockito.mock(Invocation.Builder.class);
		Response responsePostFailure = Mockito.mock(Response.class);
		
		// Define main relationships for the Client class
		Mockito.when(mockClientFailure.target(Mockito.anyString())).thenReturn(targetFailure);
		Mockito.when(targetFailure.request()).thenReturn(invocationBuilderFailure);
		
		// Define mock responses for POST (unavailability error)
		Mockito.when(invocationBuilderFailure.post(null)).thenReturn(responsePostFailure);
		Mockito.when(responsePostFailure.getStatus()).thenReturn(404);
		Mockito.when(responsePostFailure.readEntity(String.class)).thenReturn("");
		
		// Define KeystoneMock
		keystoneMock = Mockito.mock(V2.class);
		V2 secondMock = Mockito.mock(V2.class);
		OSClient mockOSClient = Mockito.mock(OSClient.class);
		OpenStackClient mockOpenstackClient = Mockito.mock(OpenStackClient.class);
		Token mockToken = Mockito.mock(Token.class);
		Mockito.when(keystoneMock.endpoint(Mockito.anyString())).thenReturn(keystoneMock);
		Mockito.when(keystoneMock.credentials(Mockito.anyString(), Mockito.anyString())).thenReturn(keystoneMock);
		Mockito.when(keystoneMock.tenantName(Mockito.anyString())).thenReturn(keystoneMock);
		Mockito.when(keystoneMock.authenticate()).thenReturn(mockOSClient);				
		Mockito.when(mockOSClient.getToken()).thenReturn(mockToken);
		
//		Mockito.when(mockOpenstackClient.getFlavor()).thenReturn(flavorId);
//    Mockito.when(mockOpenstackClient.getOsImage()).thenReturn(imageId);
//		Mockito.when(mockOSClient.compute().flavors().get(flavorId)).thenReturn(flavormock);
//		Mockito.when(mockOSClient.compute().images().get(imageId)).thenReturn(imagemock);
    
//    OpenStackClient openstackClient = Mockito.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMock, imageMock));
		Image responseOSImage = Mockito.mock(Image.class);
    Mockito.when(responseOSImage.getId()).thenReturn("small");
    Flavor responseOSFlavor = Mockito.mock(Flavor.class);
    Mockito.when(responseOSFlavor.getId()).thenReturn("7");
		OpenStackClient openstackClient = Mockito.mock(OpenStackClient.class);
		Mockito.when(openstackClient.getInternalFlavor()).thenReturn(new ArrayList<>());
		Mockito.when(openstackClient.getInternalOsImage()).thenReturn(new ArrayList<>());
		Mockito.when(openstackClient.getOsImage()).thenReturn(new String());
		Mockito.when(openstackClient.getFlavor()).thenReturn(new String());
		
		Mockito.when(mockToken.getId()).thenReturn("FakeToken!");
		System.out.println ("Finished!");
	}
	
	 @Test
   public void testFlavorSmall(){
     OpenStackClient openstackClient = Mockito.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMock, imageMock));
     Flavor small = Mockito.mock(Flavor.class);
     Mockito.doReturn("small").when(small).getName();
     Flavor large = Mockito.mock(Flavor.class);
     Mockito.doReturn("large").when(large).getName();
     
     ArrayList<Flavor> images = Lists.newArrayList(small);
     Mockito.doReturn(images).when(openstackClient).getInternalFlavor();
     openstackClient.getFlavor();
   }
   
   @Test(expected=NotFoundException.class)
   public void testEmptyFlavor(){
     OpenStackClient openstackClient = Mockito.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMock, imageMock)); 
     
     ArrayList<Flavor> images = Lists.newArrayList();
     Mockito.doReturn(images).when(openstackClient).getInternalFlavor();
     openstackClient.getFlavor();
   }
   
   @Test
   public void testFlavorUnempty() {
     OpenStackClient openstackClient = Mockito.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMock, imageMock));
     Flavor flavor = Mockito.mock(Flavor.class);
     Mockito.doReturn("NOTsmall").when(flavor).getName();
     ArrayList<Flavor> flavors = Lists.newArrayList(flavor);
     Mockito.doReturn(flavors).when(openstackClient).getInternalFlavor();
     flavors.get(0).getId();
     flavor.getId();
   }
   
   @Test
   public void testOsImageUbuntu() {
     OpenStackClient openstackClient = Mockito.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMock, imageMock));
     
     Image centos = Mockito.mock(Image.class);
     Mockito.doReturn("centos").when(centos).getName();
     Image ubuntu16 = Mockito.mock(Image.class);
     Mockito.doReturn("ubuntu").when(centos).getName();
     ArrayList<Image> images = Lists.newArrayList(centos, ubuntu16);
     Mockito.doReturn(images).when(openstackClient).getInternalOsImage();
     openstackClient.getOsImage();
   }
   
   @Test(expected=NotFoundException.class)
   public void testOsImageEmpty() {
     OpenStackClient openstackClient = Mockito.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMock, imageMock));
     Mockito.doReturn(new ArrayList<>()).when(openstackClient).getInternalOsImage();
     openstackClient.getOsImage();
   }
   
	
   @Test
   public void createServerSuccessFully() throws InterruptedException, TimeoutException{
     OpenStackClient openstackClient = Mockito.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMock, imageMock));
     ServerCreate sc = Mockito.mock(ServerCreate.class);
     CreateVmResult cVmResult = Mockito.mock(CreateVmResult.class);
     Server server = Mockito.mock(Server.class);
     List<Server> servers = Lists.newArrayList(server);
     Mockito.doReturn(servers).when(openstackClient).getServerOsList();
     Image centos = Mockito.mock(Image.class);
     Image ubuntu16 = Mockito.mock(Image.class);
     Mockito.doReturn("ubuntu").when(centos).getName();
     ArrayList<Image> images = Lists.newArrayList(ubuntu16);
     Mockito.doReturn(images).when(openstackClient).getInternalOsImage();
     Flavor small = Mockito.mock(Flavor.class);
     ArrayList<Flavor> flavors = Lists.newArrayList(small);
     Mockito.doReturn(flavors).when(openstackClient).getInternalFlavor();
     Mockito.when(openstackClient.createOsServer(Mockito.anyString())).thenReturn(sc);
//     Mockito.doReturn(sc).when(openstackClient).bootOsServer(sc);
     Mockito.doNothing().when(openstackClient).bootOsServer(sc);
     Mockito.doReturn(sc).when(openstackClient).createOsServer(Mockito.anyString());
     openstackClient.createVm();
   }
   
	@Test
	public void monitoringOperationsShouldReturnCompleteResult() throws TimeoutException, InterruptedException
	{
//	  OpenStackClient mockOpenstackClient = Mockito.mock(OpenStackClient.class);
//	  Mockito.when(mockOpenstackClient.getFlavor()).thenReturn(flavorId);
//    Mockito.when(mockOpenstackClient.getOsImage()).thenReturn(imageId);
	  
	  Image responseOSImage = Mockito.mock(Image.class);
    Mockito.when(responseOSImage.getId()).thenReturn("small");
    Flavor responseOSFlavor = Mockito.mock(Flavor.class);
    Mockito.when(responseOSFlavor.getId()).thenReturn("7");
    OpenStackClient openstackClient = Mockito.mock(OpenStackClient.class);
    Mockito.when(openstackClient.getInternalFlavor()).thenReturn(new ArrayList<>());
    Mockito.when(openstackClient.getInternalOsImage()).thenReturn(new ArrayList<>());
    Mockito.when(openstackClient.getOsImage()).thenReturn(new String());
    Mockito.when(openstackClient.getFlavor()).thenReturn(new String());
	  
//	  OpenStackClient openstackClient = Mockito.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMock, imageMock));
//    Mockito.doReturn(new ArrayList<>()).when(openstackClient).getInternalOsImage();
//    Mockito.doReturn(new ArrayList<>()).when(openstackClient).getInternalFlavor();

		// Run the openstack monitoring process and retrieve the result
		OpenStackClient myClient = new OpenStackClient(mockClient, keystoneMock, flavorMock, imageMock);
		OpenstackProbeResult result = myClient.getOpenstackMonitoringInfo();
		
		// Check Results
		Assert.assertNotNull("The result must contain information.", result);
		Assert.assertEquals("The createVM invocation should reflect 200 status.", 200, result.getCreateVmElement().getCreateVmResult());
		Assert.assertNotNull("The inspectVM result should exist.", result.getInspectVmElement());
		Assert.assertNotNull("The inspectVM result should include response time.", result.getInspectVmElement().getInspectVmResponseTime());
		Assert.assertNotNull("The deleteVM result should exist.", result.getDeleteVmElement());
		Assert.assertEquals("The general availability result should be 1", 1, result.getGlobalAvailability());		
	}
   
	@Test
	public void monitoringOperationShouldReturnPartialResult() throws TimeoutException, InterruptedException
	{
	  
	  Image responseOSImage = Mockito.mock(Image.class);
    Mockito.when(responseOSImage.getId()).thenReturn("small");
    Flavor responseOSFlavor = Mockito.mock(Flavor.class);
    Mockito.when(responseOSFlavor.getId()).thenReturn("7");
    OpenStackClient openstackClient = Mockito.mock(OpenStackClient.class);
    Mockito.when(openstackClient.getInternalFlavor()).thenReturn(new ArrayList<>());
    Mockito.when(openstackClient.getInternalOsImage()).thenReturn(new ArrayList<>());
    Mockito.when(openstackClient.getOsImage()).thenReturn(new String());
    Mockito.when(openstackClient.getFlavor()).thenReturn(new String());
	  
	  OpenStackClient mockOpenstackClient = Mockito.mock(OpenStackClient.class);
	  Mockito.doReturn(new ArrayList<>()).when(mockOpenstackClient).getInternalFlavor();
	  Mockito.doReturn(new ArrayList<>()).when(mockOpenstackClient).getInternalOsImage();
    Mockito.when(mockOpenstackClient.getFlavor()).thenReturn(flavorId);
    Mockito.when(mockOpenstackClient.getOsImage()).thenReturn(imageId);
    
		// Run the openstack monitoring process and retrieve the result
	  OpenStackClient myClient = new OpenStackClient(mockClientFailure, keystoneMock, flavorMock, imageMock);
	  
		OpenstackProbeResult result = myClient.getOpenstackMonitoringInfo();
		
		// Check Results
		Assert.assertNotNull("The result must contain information.", result);
		Assert.assertEquals("The createVM invocation should reflect 404 status.", 404, result.getCreateVmElement().getCreateVmResult());
		Assert.assertNull("The inspectVM result should not exist.", result.getInspectVmElement());
		Assert.assertEquals("The general availability result should be 0", 0, result.getGlobalAvailability());
	}
	
	
}