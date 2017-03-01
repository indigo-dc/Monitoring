///**
//Licensed under the Apache License, Version 2.0 (the License);
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//
//**/
//
//package org.indigo.openstackprobe.openstack;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import com.indigo.zabbix.utils.ZabbixWrapperClient;
//
//import io.github.hengyunabc.zabbix.sender.ZabbixSender;
//
//public class ZabbixSenderTest 
//{
//	private Runtime mockRuntime;
//	private Runtime mockRuntimeFail;
//	private ZabbixWrapperClient mockWrapper;
//	
//	@Before
//	public void prepareMockOpenstackServer() throws IOException, InterruptedException
//	{
//		System.out.println ("Setting up testing environment...");
//		
//		// Define the main mock classes for complete result
//		mockRuntime = Mockito.mock(Runtime.class);
//		Process pr = Mockito.mock(Process.class);
//		mockRuntimeFail = Mockito.mock(Runtime.class);
//		Process prFail = Mockito.mock(Process.class);
//		mockWrapper = Mockito.mock(ZabbixWrapperClient.class);
//				
//		// Define main relationships for the Runtime class
//		Mockito.when(mockRuntime.exec(Mockito.anyString())).thenReturn(pr);
//		
//		// Define the response when executing the calls to Zabbix
//		String zabbixResponse = "info from server: \"processed: 1; failed: 0; "
//				+ "total: 1; seconds spent: 0.000120\" sent: 1; skipped: 0; total: 1";
//		InputStream mockInput = new ByteArrayInputStream(zabbixResponse.getBytes());
//		Mockito.when(pr.waitFor()).thenReturn(0);
//		Mockito.when(pr.getInputStream()).thenReturn(mockInput);
//		
//		// Define main relationships for the Runtime Failure class
//		Mockito.when(mockRuntimeFail.exec(Mockito.anyString())).thenReturn(prFail);
//		
//		// Define the response when executing the calls to Zabbix Fail
//		String zabbixResponseFail = "info from server: \"processed: 1; failed: 1; "
//				+ "total: 1; seconds spent: 0.000120\" sent: 1; skipped: 0; total: 0";
//		InputStream mockInputFail = new ByteArrayInputStream(zabbixResponseFail.getBytes());
//		Mockito.when(prFail.waitFor()).thenReturn(1);
//		Mockito.when(prFail.getInputStream()).thenReturn(mockInputFail);
//		
//		// Define the wrapper stubs
//		Mockito.when(mockWrapper.isHostRegistered(Mockito.anyString())).thenReturn(true);		
//		
//		System.out.println ("Finished!");
//	}
//	
//	@Test
//	public void sendingMetricsShouldCompleteFine()
//	{
//		// Generate input object
//		CreateVmResult create = new CreateVmResult (1, 200, 1429, "testVM");
//		InspectVmResult inspect = new InspectVmResult (1, 200, 426);
//		DeleteVmResult delete = new DeleteVmResult (1, 204, 612);
//		OpenstackProbeResult global = new OpenstackProbeResult(1, 204, 2467, "TestProvider");
//		global.addCreateVmInfo(create);
//		global.addInspectVmInfo(inspect);
//		global.addDeleteVmInfo(delete);
//		
//		// Send metrics and check the result
//		ZabbixSender mySender = ZabbixSender.instance(mockRuntime, mockWrapper);
//		mySender.addMetricToQueue(global);
//		boolean result = mySender.sendMetrics();
//				
//		// Check Results
//		Assert.assertTrue("The result of sending the metrics should be correct.", result);
//	}
//	
//	@Test
//	public void sendingFullMetricsShouldFail()
//	{		
//		// Generate input object
//		CreateVmResult create = new CreateVmResult (1, 200, 1429, "testVM");
//		InspectVmResult inspect = new InspectVmResult (1, 200, 426);
//		DeleteVmResult delete = new DeleteVmResult (1, 204, 612);
//		OpenstackProbeResult global = new OpenstackProbeResult(1, 204, 2467, "TestProvider");
//		global.addCreateVmInfo(create);
//		global.addInspectVmInfo(inspect);
//		global.addDeleteVmInfo(delete);
//		
//		// Send metrics to unavailable Zabbix and check the result
//		ZabbixSender mySender = ZabbixSender.instance(mockRuntimeFail, mockWrapper);
//		mySender.addMetricToQueue(global);
//		boolean result = mySender.sendMetrics();
//		
//		// Check Results
//		Assert.assertFalse("The result of sending the metrics should be wrong.", result);		
//	}
//	
//	@Test
//	public void sendingIncompleteMetricsShouldFail()
//	{
//		// Try to send metrics with a null input
//		ZabbixSender mySender = ZabbixSender.instance(mockRuntime, mockWrapper);
//		mySender.addMetricToQueue(null);
//		boolean result = mySender.sendMetrics();
//				
//		// Check Results
//		Assert.assertFalse("The result of sending the metrics should be wrong.", result);
//		
//		// Create and send an empty result
//		OpenstackProbeResult global = new OpenstackProbeResult("TestProvider");
//		mySender.addMetricToQueue(global);
//		result = mySender.sendMetrics();
//		
//		// Check Results
//		Assert.assertFalse("The result of sending the metrics should be wrong.", result);
//		
//	}
//}