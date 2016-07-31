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

package org.indigo.heapsterprobe.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.indigo.heapsterprobe.HeapsterClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MonitoringTest 
{
	private Client mockClient;
	private Client mockClientFailure;

	@Before
	public void prepareMockHeapsterServer()
	{
		System.out.println ("Setting up testing environment...");
		
		// Define the main mock classes for complete result
		mockClient = Mockito.mock(Client.class);
		WebTarget targetList = Mockito.mock(WebTarget.class);
		WebTarget targetNamespaces = Mockito.mock(WebTarget.class);
		WebTarget targetMetrics = Mockito.mock(WebTarget.class);
		WebTarget targetDetails = Mockito.mock(WebTarget.class);
		Invocation.Builder invocationBuilderList = Mockito.mock(Invocation.Builder.class);
		Invocation.Builder invocationBuilderMetrics = Mockito.mock(Invocation.Builder.class);
		Invocation.Builder invocationBuilderNamespaces = Mockito.mock(Invocation.Builder.class);
		Invocation.Builder invocationBuilderDetails = Mockito.mock(Invocation.Builder.class);
		Response responseGetList = Mockito.mock(Response.class);
		Response responseGetMetrics = Mockito.mock(Response.class);
		Response responseGetNamespaces = Mockito.mock(Response.class);
		Response responseGetDetails = Mockito.mock(Response.class);
				
		// Define main relationships for the Client class
		Mockito.when(mockClient.target(Mockito.endsWith("/namespaces/"))).thenReturn(targetNamespaces);
		Mockito.when(mockClient.target(Mockito.endsWith("/pods/"))).thenReturn(targetList);
		Mockito.when(mockClient.target(Mockito.endsWith("/metrics/"))).thenReturn(targetMetrics);
		Mockito.when(mockClient.target(Mockito.endsWith("/network/tx_errors/"))).thenReturn(targetDetails);
		Mockito.when(targetNamespaces.request()).thenReturn(invocationBuilderNamespaces);
		Mockito.when(targetList.request()).thenReturn(invocationBuilderList);
		Mockito.when(targetMetrics.request()).thenReturn(invocationBuilderMetrics);
		Mockito.when(targetDetails.request()).thenReturn(invocationBuilderDetails);
				
		// Define mock response for GET list (complete result)
		Mockito.when(invocationBuilderNamespaces.get()).thenReturn(responseGetNamespaces);
		Mockito.when(responseGetNamespaces.getStatus()).thenReturn(200);
		Mockito.when(responseGetNamespaces.readEntity(String.class)).thenReturn("[\"kube-system\"]");
				
		// Define mock response for GET list (complete result)
		Mockito.when(invocationBuilderList.get()).thenReturn(responseGetList);
		Mockito.when(responseGetList.getStatus()).thenReturn(200);
		Mockito.when(responseGetList.readEntity(String.class)).thenReturn("[\"zabbix-web-u0r1i\", \"monitoring-influxdb-grafana-v3-smcw1\", \"kubedash-dx46h\"]");
				
		// Define mock response for GET metrics (complete result)
		Mockito.when(invocationBuilderMetrics.get()).thenReturn(responseGetMetrics);
		Mockito.when(responseGetMetrics.getStatus()).thenReturn(200);
		Mockito.when(responseGetMetrics.readEntity(String.class)).thenReturn("[\"network/tx_errors\"]");
		
		// Define mock response for GET details (complete result)
		Mockito.when(invocationBuilderDetails.get()).thenReturn(responseGetDetails);
		Mockito.when(responseGetDetails.getStatus()).thenReturn(200);
		Mockito.when(responseGetDetails.readEntity(String.class)).thenReturn("{\"metrics\": [{\"timestamp\": \"2016-07-31T16:13:00Z\",\"value\": 0},{\"timestamp\": \"2016-07-31T16:14:00Z\",\"value\": 0},"
					+ "{\"timestamp\": \"2016-07-31T16:15:00Z\", \"value\": 0}],\"latestTimestamp\": \"2016-07-31T16:15:00Z\"}");
				
		System.out.println ("Finished!");
	}
	
	@Test
	public void monitoringOperationsShouldReturnCompleteResult()
	{
		// Run the OCCI monitoring process and retrieve the result
		HeapsterClient myClient = new HeapsterClient(mockClient);
		String[] testList = myClient.getPodsList();
		
		// Check Results
		Assert.assertNotNull("The result must contain information.", testList);			
	}
	
	
	
}
