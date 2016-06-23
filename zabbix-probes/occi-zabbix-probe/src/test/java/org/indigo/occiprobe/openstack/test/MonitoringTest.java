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

package org.indigo.occiprobe.openstack.test;

import org.indigo.occiprobe.openstack.OCCIProbeResult;
import org.indigo.occiprobe.openstack.OpenStackOCCIClient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class MonitoringTest 
{
	private Client mockClient;
	private Client mockClientFailure;

	@Before
	public void prepareMockOCCIServer()
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
		Mockito.when(responsePost.readEntity(String.class)).thenReturn("X-OCCI-Location: http://cloud.recas.ba.infn.it:8787/occi/compute/b4b5626c-62c4-4f76-b788-15329381f824");
		
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
		
		System.out.println ("Finished!");
	}
	
	@Test
	public void monitoringOperationsShouldReturnCompleteResult()
	{
		// Run the OCCI monitoring process and retrieve the result
		OpenStackOCCIClient myClient = new OpenStackOCCIClient(mockClient);
		OCCIProbeResult result = myClient.getOCCIMonitoringInfo();
		
		// Check Results
		Assert.assertNotNull("The result must contain information.", result);
		Assert.assertEquals("The createVM invocation should reflect 200 status.", 200, result.getCreateVMElement().getCreateVMResult());
		Assert.assertNotNull("The inspectVM result should exist.", result.getInspectVMElement());
		Assert.assertNotNull("The inspectVM result should include response time.", result.getInspectVMElement().getInspectVMResponseTime());
		Assert.assertNotNull("The deleteVM result should exist.", result.getDeleteVMElement());
		Assert.assertEquals("The general availability result should be 1", 1, result.getGlobalAvailability());		
	}
	
	@Test
	public void monitoringOperationShouldReturnPartialResult()
	{
		// Run the OCCI monitoring process and retrieve the result
		OpenStackOCCIClient myClient = new OpenStackOCCIClient(mockClientFailure);
		OCCIProbeResult result = myClient.getOCCIMonitoringInfo();
		
		// Check Results
		Assert.assertNotNull("The result must contain information.", result);
		Assert.assertEquals("The createVM invocation should reflect 404 status.", 404, result.getCreateVMElement().getCreateVMResult());
		Assert.assertNull("The inspectVM result should not exist.", result.getInspectVMElement());
		Assert.assertEquals("The general availability result should be 0", 0, result.getGlobalAvailability());
	}
	
}
