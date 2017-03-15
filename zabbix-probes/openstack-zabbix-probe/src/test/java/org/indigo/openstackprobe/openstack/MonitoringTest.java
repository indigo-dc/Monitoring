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

import static org.junit.Assert.assertSame;

import java.math.BigInteger;
import java.net.ConnectException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.client.IOSClientBuilder.V2;
import org.openstack4j.api.compute.ComputeService;
import org.openstack4j.api.compute.FlavorService;
import org.openstack4j.api.compute.ServerService;
import org.openstack4j.api.image.ImageService;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.identity.Token;
import org.openstack4j.model.image.Image;

import jersey.repackaged.com.google.common.collect.Lists;

public class MonitoringTest {

	@Mock
	private Client mockClient;
	private Client mockClientFailure;
	private ClientConfig clientConfigMocked;
	private V2 keystoneMock;
	private ServerCreate serverCreate;
	private Image mockOSImage;
	private Flavor mockOSFlavor;
	private FlavorService flavorServiceMocked;
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
	
	private System system;

	private List<Server> servers = new ArrayList<>();

	private Map<Server.Status, Server> serverCreationMocked = new HashMap<>();

	private ServerService serverServiceMocked;
	private Server serverMocked;
	Map<String, Integer> resultMap = new HashMap<>();

	@Before
	public void prepareMockOpenstackServer() {
		System.out.println("Setting up testing environment and prepare the prepareMockOpenstackServer...");

//		system = Mockito.mock(System.class);
		System.setProperty("jsse.enableSNIExtension", "false");
		System.setProperty("javax.net.ssl.trustStore", "C:/Program Files/Java/jdk1.8.0_121/jre/lib/security/cacerts");
		
		// Define the main mock classes for complete result
		mockClient = Mockito.mock(Client.class);
		WebTarget target = Mockito.mock(WebTarget.class);
		Invocation.Builder invocationBuilder = Mockito.mock(Invocation.Builder.class);
		Response responsePost = Mockito.mock(Response.class);
		Response responseGet = Mockito.mock(Response.class);
		Response responseDelete = Mockito.mock(Response.class);
		
//		clientConfigMocked = Mockito.mock(ClientConfig.class);
//		mockClient = JerseyClientBuilder.newClient(clientConfigMocked);

		// Define main relationships for the Client class
		Mockito.when(mockClient.target(Mockito.anyString())).thenReturn(target);
		Mockito.when(target.request()).thenReturn(invocationBuilder);

		// Define mock response for POST (complete result)
		Mockito.when(invocationBuilder.post(null)).thenReturn(responsePost);
		Mockito.when(responsePost.getStatus()).thenReturn(200);
		Mockito.when(responsePost.readEntity(String.class)).thenReturn(
				"Openstack-Location: http://cloud.recas.ba.infn.it:8774/compute/78d9ecb64353402bb621b569891c633a");

		// Define mock response for GET (complete result)
		Mockito.when(invocationBuilder.get()).thenReturn(responseGet);
		Mockito.when(responseGet.getStatus()).thenReturn(200);
		Mockito.when(responseGet.readEntity(String.class)).thenReturn("");

		// Define mock response for DELETE (complete result)
		Mockito.when(invocationBuilder.delete()).thenReturn(responseDelete);
		Mockito.when(responseDelete.getStatus()).thenReturn(204);
		Mockito.when(responseDelete.readEntity(String.class)).thenReturn("");
		ActionResponse actionMocked = Mockito.mock(ActionResponse.class);
		Mockito.when(actionMocked.isSuccess()).thenReturn(true);

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

		// Mock Image
		// Define main relationships for the Client class Images
	    Mockito.when(mockClient.target(Mockito.endsWith("/image/list"))).thenReturn(target);
	    Mockito.when(mockClient.target(Mockito.endsWith("include_docs=true"))).thenReturn(target);
	    Mockito.when(target.request()).thenReturn(invocationBuilder);
	    Mockito.when(target.request()).thenReturn(invocationBuilder);
	    
	 // Define mock response for GET list of images(complete result)
	    Mockito.when(invocationBuilder.get()).thenReturn(responseGet);
	    Mockito.when(responseGet.getStatus()).thenReturn(200);
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
	    Mockito.when(responseGet.readEntity(String.class)).thenReturn(listImagesResponse);
		mockOSImage = Mockito.mock(Image.class);
		imageServiceMocked = Mockito.mock(ImageService.class);
		Mockito.when(mockOSClient.images()).thenReturn(imageServiceMocked);
		List<Image> images = new ArrayList<>();
		images.add(mockOSImage);
		Mockito.doReturn("ubuntu").when(mockOSImage).getName();
		Mockito.doReturn("imageId").when(mockOSImage).getId();
		imagesSimulated = Lists.newArrayList(mockOSImage);

		// Mock the flavors
		mockOSFlavor = Mockito.mock(Flavor.class);
		flavorServiceMocked = Mockito.mock(FlavorService.class);
		Mockito.when(computeMocked.flavors()).thenReturn(flavorServiceMocked);
		List<FlavorService> flavors = new ArrayList<>();
		flavors.add(flavorServiceMocked);
		Mockito.doReturn("small").when(mockOSFlavor).getName();
		Mockito.doReturn("flavorId").when(mockOSFlavor).getId();
		flavorsSimulated = Lists.newArrayList(mockOSFlavor);
		// Mockito.when(flavorServiceMocked.list()).thenReturn(flavors);
		System.out.println("Finished the preparation of mockig class!");

		// Mock the Creation of a VM
		serverServiceMocked = Mockito.mock(ServerService.class);
		Mockito.when(computeMocked.servers()).thenReturn(serverServiceMocked);

		serverMocked = Mockito.mock(Server.class);
		Mockito.when(serverMocked.getId()).thenReturn("ServerIdTest");
		Mockito.when(serverMocked.getStatus()).thenReturn(Server.Status.ACTIVE);
		Mockito.when(serverMocked.getId()).thenReturn("vmIdTest");
		Mockito.when(serverMocked.getName())
				.thenReturn("vMOpenstackZabbixProbe_" + new BigInteger(37, new SecureRandom()).toString(16));
		ServerCreate serverCreateMoked = Mockito.mock(ServerCreate.class);
		Mockito.when(serverServiceMocked.boot(serverCreateMoked)).thenReturn(serverMocked);

		serverCreationMocked.put(Server.Status.ACTIVE, serverMocked);
		Mockito.when(serverCreationMocked.get(Server.Status.ACTIVE).getId()).thenReturn("vmIdTest");
		Mockito.when(serverServiceMocked.get("vmIdTest")).thenReturn(serverMocked);
		Mockito.when(serverMocked.getCreated()).thenReturn(new Date());
		servers.add(serverMocked);

		// Mock the delete operation
		Mockito.when(serverServiceMocked.delete("vmIdTest")).thenReturn(actionMocked);

		resultMap.put("availability", 1);
		resultMap.put("httpCode", 200);
	}

	@Test
	public void monitoringOperationsShouldReturnCompleteResult() throws TimeoutException, InterruptedException {

		OpenstackComponent component = new OpenstackComponent();
		component.setMockClient(mockClient);
		component.setOsClientMocked(mockOSClient);
		component.setMockKeystone(keystoneMock);
		component.setTokenMoked(mockToken);
		component.setTokenId(token);
		component.setComputeserviceMocked(computeMocked);
		component.setFlavorServiceMocked(flavorServiceMocked);
		component.setImageServiceMocked(imageServiceMocked);
		component.setFlavorsMocked(flavorsSimulated);
		component.setImagesMocked(imagesSimulated);
		component.setMockFlavor(mockOSFlavor);
		component.setMockImage(mockOSImage);
		component.setBaseKeystoneUrlMocked(endpoint);
		component.setOpenStackUserMocked(user);
		component.setOpenStackPwdMocked(password);
		component.setTenantNameMoked(tenant);
		component.setServerCreationMocked(serverCreationMocked);
		component.setServerServiceMocked(serverServiceMocked);
		component.setServerMocked(serverMocked);
		component.setResultMapMocked(resultMap);
		component.setServersMocks(servers);

		OpenStackClient openstackClient = new OpenStackClient(component);

		OpenstackProbeResult result = openstackClient.getOpenstackMonitoringInfo();

		// Check Results
		Assert.assertNotNull("The result must contain information.", result);
		Assert.assertEquals("The createVM invocation should reflect 200 status.", 200,
				result.getCreateVmElement().getCreateVmResult());
		Assert.assertNotNull("The inspectVM result should exist.", result.getInspectVmElement());
		Assert.assertNotNull("The inspectVM result should exist.",
				result.getInspectVmElement().getInspectVmAvailability());
		Assert.assertNotNull("The inspectVM result should exist.", result.getInspectVmElement().getInspectVmResult());
		Assert.assertNotNull("The inspectVM result should exist.",
				result.getInspectVmElement().getInspectVmResponseTime());
		Assert.assertNotNull("The inspectVM result should include response time.",
				result.getInspectVmElement().getInspectVmResponseTime());
		Assert.assertNotNull("The deleteVM result should exist.", result.getDeleteVmElement());
		Assert.assertNotNull("The deleteVM result should exist.",
				result.getDeleteVmElement().getDeleteVmAvailability());
		Assert.assertNotNull("The deleteVM result should exist.",
				result.getDeleteVmElement().getDeleteVmResponseTime());
		Assert.assertNotNull("The deleteVM result should exist.", result.getDeleteVmElement().getDeleteVmResult());
		Assert.assertNotNull(result.getOsInstanceList());
		Assert.assertEquals("The general availability result should be 1", 1, result.getGlobalAvailability());
	}

	@Test
	public void testFlavorSmall() {
		OpenStackClient openstackClient = Mockito
				.spy(new OpenStackClient(mockClient, keystoneMock, flavorMockId, imageMockId));
		Flavor flavormocked = Mockito.mock(Flavor.class);
		Mockito.doReturn("small").when(flavormocked).getName();
		Flavor large = Mockito.mock(Flavor.class);
		Mockito.doReturn("large").when(large).getName();

		ArrayList<Flavor> flavors = Lists.newArrayList(flavormocked);
		Mockito.doReturn(flavors).when(openstackClient).getInternalFlavor();
		openstackClient.getFlavor();
	}

	@Test(expected = NotFoundException.class)
	public void testEmptyFlavor() {
		OpenStackClient openstackClient = Mockito
				.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMockId, imageMockId));

		ArrayList<Flavor> images = Lists.newArrayList();
		Mockito.doReturn(images).when(openstackClient).getInternalFlavor();
		openstackClient.getFlavor();
	}

	@Test
	public void testFlavorUnempty() {
		OpenStackClient openstackClient = Mockito
				.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMockId, imageMockId));
		Flavor flavor = Mockito.mock(Flavor.class);
		Mockito.doReturn("NOTsmall").when(flavor).getName();
		ArrayList<Flavor> flavors = Lists.newArrayList(flavor);
		Mockito.doReturn(flavors).when(openstackClient).getInternalFlavor();
		flavors.get(0).getId();
		flavor.getId();
	}

	@Test
	public void testOsImageUbuntu() {
		OpenStackClient openstackClient = Mockito
				.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMockId, imageMockId));

		Image centos = Mockito.mock(Image.class);
		Mockito.doReturn("centos").when(centos).getName();
		Image ubuntu16 = Mockito.mock(Image.class);
		Mockito.doReturn("ubuntu").when(centos).getName();
		ArrayList<Image> images = Lists.newArrayList(centos, ubuntu16);
		Mockito.doReturn(images).when(openstackClient).getInternalOsImage();
		Mockito.doReturn(new ArrayList<>()).when(openstackClient).getImagesFromCmdb();
		openstackClient.getOsImage();
	}

	@Test
	public void testImagesFromCmdb() {
		// List<Image> imagesFromCmdbmocked = new ArrayList<>();
		OpenStackClient openstackClient = Mockito
				.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMockId, imageMockId));
		Mockito.doReturn(new ArrayList<>()).when(openstackClient).getImagesFromCmdb();
	}

	@Test(expected = NotFoundException.class)
	public void testOsImageEmpty() {
		OpenStackClient openstackClient = Mockito
				.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMockId, imageMockId));
		Mockito.doReturn(new ArrayList<>()).when(openstackClient).getInternalOsImage();
		openstackClient.getOsImage();
	}

//	@Test
//	public void testgetTokenIdSuccessfull() {
//		OpenStackClient openstackClient = Mockito
//				.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMockId, imageMockId));
//		Mockito.doReturn(token).when(openstackClient).tokenId;
//
//		assertSame(token, openstackClient.tokenId);
//	}
//
//	@Test(expected = ConnectException.class)
//	public void testgetTokenIdFailure() {
//		OpenStackClient openstackClient = Mockito
//				.spy(new OpenStackClient(mockClientFailure, keystoneMock, flavorMockId, imageMockId));
//		Mockito.doThrow(ConnectException.class).when(openstackClient).getTokenId();
//
//		openstackClient.getTokenId();
//	}
}