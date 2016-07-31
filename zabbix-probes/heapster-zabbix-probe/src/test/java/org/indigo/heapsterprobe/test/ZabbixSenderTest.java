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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.indigo.heapsterprobe.ContainerMetrics;
import org.indigo.heapsterprobe.PodMetrics;
import org.indigo.heapsterprobe.ZabbixSender;
import org.indigo.heapsterprobe.ZabbixWrapperClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ZabbixSenderTest 
{
	private Runtime mockRuntime;
	private Runtime mockRuntimeFail;
	private ZabbixWrapperClient mockWrapper;
	
	@Before
	public void prepareMockOCCIServer() throws IOException, InterruptedException
	{
		System.out.println ("Setting up testing environment...");
		
		// Define the main mock classes for complete result
		mockRuntime = Mockito.mock(Runtime.class);
		Process pr = Mockito.mock(Process.class);
		mockRuntimeFail = Mockito.mock(Runtime.class);
		Process prFail = Mockito.mock(Process.class);
		mockWrapper = Mockito.mock(ZabbixWrapperClient.class);		
				
		// Define main relationships for the Runtime class
		Mockito.when(mockRuntime.exec(Mockito.anyString())).thenReturn(pr);
		
		// Define the response when executing the calls to Zabbix
		String zabbixResponse = "info from server: \"processed: 1; failed: 0; "
				+ "total: 1; seconds spent: 0.000120\" sent: 1; skipped: 0; total: 1";
		InputStream mockInput = new ByteArrayInputStream(zabbixResponse.getBytes());
		Mockito.when(pr.waitFor()).thenReturn(0);
		Mockito.when(pr.getInputStream()).thenReturn(mockInput);
		
		// Define main relationships for the Runtime Failure class
		Mockito.when(mockRuntimeFail.exec(Mockito.anyString())).thenReturn(prFail);
		
		// Define the response when executing the calls to Zabbix Fail
		String zabbixResponseFail = "info from server: \"processed: 1; failed: 1; "
				+ "total: 1; seconds spent: 0.000120\" sent: 1; skipped: 0; total: 0";
		InputStream mockInputFail = new ByteArrayInputStream(zabbixResponseFail.getBytes());
		Mockito.when(prFail.waitFor()).thenReturn(1);
		Mockito.when(prFail.getInputStream()).thenReturn(mockInputFail);
		
		// Define the wrapper stubs
		Mockito.when(mockWrapper.isPodRegistered(Mockito.anyString())).thenReturn(true);
		Mockito.when(mockWrapper.isContainerRegistered(Mockito.anyString())).thenReturn(true);		
		
		System.out.println ("Finished!");
	}
	
	@Test
	public void sendingMetricsShouldCompleteFine()
	{
		// Generate input objects
		PodMetrics pod = new PodMetrics ("PodPru", "NS", 0, 0, 0, 0, 10, 15, 1000);
		ContainerMetrics container = new ContainerMetrics ("ContainerPru", 18, 100, 
				1325342713608l, 524288000, 524288000, 0, 0, 0, 0, 287133696, 272068608, 74758561);
		
		// Send pod metrics and check the result
		ZabbixSender mySender = new ZabbixSender(mockRuntime, mockWrapper);
		boolean result = mySender.sendPodMetrics(pod);
				
		// Check Results
		Assert.assertTrue("The result of sending the metrics should be correct.", result);
		
		// Send container metrics and check the result
		result = mySender.sendContainerMetrics(container);
		
		// Check Results
		Assert.assertTrue("The result of sending the metrics should be correct.", result);
	}
	
	@Test
	public void sendingFullMetricsShouldFail()
	{		
		// Generate input object
		PodMetrics pod = new PodMetrics ("PodPru", "NS", 0, 0, 0, 0, 10, 15, 1000);
				
		// Send metrics to unavailable Zabbix and check the result
		ZabbixSender mySender = new ZabbixSender(mockRuntimeFail, mockWrapper);
		boolean result = mySender.sendPodMetrics(pod);
		
		// Check Results
		Assert.assertFalse("The result of sending the metrics should be wrong.", result);		
	}
	
	@Test
	public void sendingIncompleteMetricsShouldFail()
	{
		// Try to send metrics with a null input
		ZabbixSender mySender = new ZabbixSender(mockRuntime, mockWrapper);
		boolean result = mySender.sendPodMetrics(null);
				
		// Check Results
		Assert.assertFalse("The result of sending the metrics should be wrong.", result);		
	}
}
