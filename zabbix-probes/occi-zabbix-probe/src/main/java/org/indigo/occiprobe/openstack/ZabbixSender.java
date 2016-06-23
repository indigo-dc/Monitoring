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

package org.indigo.occiprobe.openstack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZabbixSender 
{

	private String zabbixLocation;
	private String zabbixSender;
	private String provider;
	
	public ZabbixSender(String targetProvider)
	{
		// Retrieve location of the Zabbix Server and the Zabbix sender (local)
		PropertiesManager myProp = new PropertiesManager();
		zabbixLocation = myProp.getProperty(PropertiesManager.ZABBIX_IP);
		zabbixSender = myProp.getProperty(PropertiesManager.ZABBIX_SENDER);
		provider = targetProvider;
	}
	
	public boolean sendMetrics(OCCIProbeResult metrics)
	{
		// Prepare invocation strings
		String globalAvailability = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.global[availability] -o " + metrics.getGlobalAvailability();
		String globalResult = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.global[result] -o " + metrics.getGlobalResult();
		String globalResponseTime = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.global[responseTime] -o " + metrics.getGlobalResponseTime();
		
		String createVMAvailability = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.createvm[availability] -o " + metrics.getCreateVMElement().getCreateVMAvailability();
		String createVMResult = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.createvm[result] -o " + metrics.getCreateVMElement().getCreateVMResult();
		String createVMResponseTime = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.createvm[responseTime] -o " + metrics.getCreateVMElement().getCreateVMResponseTime();
		
		String inspectVMAvailability = "";
		String inspectVMResult = "";
		String inspectVMResponseTime = "";
		if (metrics.getInspectVMElement()!=null)
		{
			inspectVMAvailability = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.inspectvm[availability] -o " + metrics.getInspectVMElement().getInspectVMAvailability();
			inspectVMResult = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.inspectvm[result] -o " + metrics.getInspectVMElement().getInspectVMResult();
			inspectVMResponseTime = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.inspectvm[responseTime] -o " + metrics.getInspectVMElement().getInspectVMResponseTime();
		}
		
		String deleteVMAvailability = "";
		String deleteVMResult = "";
		String deleteVMResponseTime = "";
		if (metrics.getDeleteVMElement()!=null)
		{
			deleteVMAvailability = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.deletevm[availability] -o " + metrics.getDeleteVMElement().getDeleteVMAvailability();
			deleteVMResult = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.deletevm[result] -o " + metrics.getDeleteVMElement().getDeleteVMResult();
			deleteVMResponseTime = "-z " + zabbixLocation + " -s \"" + provider + "\" -k occi.deletevm[responseTime] -o " + metrics.getDeleteVMElement().getDeleteVMResponseTime();
		}
		
		Runtime rt = Runtime.getRuntime();
		try
		{
			// Determine execution context
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("win") >= 0)
			{
				globalAvailability = zabbixSender + "/zabbix_sender.exe " + globalAvailability;
				globalResult = zabbixSender + "/zabbix_sender.exe " + globalResult;
				globalResponseTime = zabbixSender + "/zabbix_sender.exe " + globalResponseTime;
				
				createVMAvailability = zabbixSender + "/zabbix_sender.exe " + createVMAvailability;
				createVMResult = zabbixSender + "/zabbix_sender.exe " + createVMResult;
				createVMResponseTime = zabbixSender + "/zabbix_sender.exe " + createVMResponseTime;
				
				inspectVMAvailability = zabbixSender + "/zabbix_sender.exe " + inspectVMAvailability;
				inspectVMResult = zabbixSender + "/zabbix_sender.exe " + inspectVMResult;
				inspectVMResponseTime = zabbixSender + "/zabbix_sender.exe " + inspectVMResponseTime;
				
				deleteVMAvailability = zabbixSender + "/zabbix_sender.exe " + deleteVMAvailability;
				deleteVMResult = zabbixSender + "/zabbix_sender.exe " + deleteVMResult;
				deleteVMResponseTime = zabbixSender + "/zabbix_sender.exe " + deleteVMResponseTime;
			}
			else
			{
				globalAvailability = zabbixSender + "/zabbix_sender " + globalAvailability;
				globalResult = zabbixSender + "/zabbix_sender " + globalResult;
				globalResponseTime = zabbixSender + "/zabbix_sender " + globalResponseTime;
				
				createVMAvailability = zabbixSender + "/zabbix_sender " + createVMAvailability;
				createVMResult = zabbixSender + "/zabbix_sender " + createVMResult;
				createVMResponseTime = zabbixSender + "/zabbix_sender " + createVMResponseTime;
				
				inspectVMAvailability = zabbixSender + "/zabbix_sender " + inspectVMAvailability;
				inspectVMResult = zabbixSender + "/zabbix_sender " + inspectVMResult;
				inspectVMResponseTime = zabbixSender + "/zabbix_sender " + inspectVMResponseTime;
				
				deleteVMAvailability = zabbixSender + "/zabbix_sender " + deleteVMAvailability;
				deleteVMResult = zabbixSender + "/zabbix_sender " + deleteVMResult;
				deleteVMResponseTime = zabbixSender + "/zabbix_sender " + deleteVMResponseTime;
			}
									
			// Process pr = rt.exec("cmd /c dir");
			Process pr = rt.exec(globalAvailability);			
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());			
			
			pr = rt.exec(globalResult);
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());
			
			pr = rt.exec(globalResponseTime);
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());
			
			pr = rt.exec(createVMAvailability);			
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());			
			
			pr = rt.exec(createVMResult);
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());
			
			pr = rt.exec(createVMResponseTime);
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());
			
			if (metrics.getInspectVMElement()!=null)
			{
				pr = rt.exec(inspectVMAvailability);			
				readExecResponse (pr.getInputStream());
				System.out.println (pr.waitFor());			
				
				pr = rt.exec(inspectVMResult);
				readExecResponse (pr.getInputStream());
				System.out.println (pr.waitFor());
				
				pr = rt.exec(inspectVMResponseTime);
				readExecResponse (pr.getInputStream());
				System.out.println (pr.waitFor());
			}
			
			if (metrics.getDeleteVMElement()!=null)
			{
				pr = rt.exec(deleteVMAvailability);			
				readExecResponse (pr.getInputStream());
				System.out.println (pr.waitFor());			
				
				pr = rt.exec(deleteVMResult);
				readExecResponse (pr.getInputStream());
				System.out.println (pr.waitFor());
				
				pr = rt.exec(deleteVMResponseTime);
				readExecResponse (pr.getInputStream());
				System.out.println (pr.waitFor());
			}
		}
		catch (IOException ex)
		{
			System.out.println("Error: " + ex.getMessage());
		}
		catch (InterruptedException iex)
		{
			System.out.println("Error: " + iex.getMessage());
		}
		
		return true;
	}
	
	private void readExecResponse (InputStream response)
	{
		BufferedReader input = new BufferedReader(new InputStreamReader(response));
		
		try
		{
			String line=null;
	        while((line=input.readLine()) != null) 
	        {
	            System.out.println(line);
	        }
		}
        catch (Exception ex)
        {
        	System.out.println ("Error: " + ex.getMessage());
        }
	}
}
