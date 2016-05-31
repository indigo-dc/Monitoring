package org.indigo.heapsterprobe;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZabbixSender 
{

	private String zabbixLocation;
	private String zabbixSender;
	private String cluster;
	
	public ZabbixSender(String clusterName)
	{
		// Retrieve location of the Zabbix Server and the Zabbix sender (local)
		PropertiesManager myProp = new PropertiesManager();
		zabbixLocation = myProp.getProperty(PropertiesManager.ZABBIX_IP);
		zabbixSender = myProp.getProperty(PropertiesManager.ZABBIX_SENDER);
		cluster = clusterName;
	}
	
	public boolean sendPodMetrics(PodMetrics metrics)
	{
		String podName = metrics.getPodName();
		
		// Prepare invocation strings
		String rxErrors = "-z " + zabbixLocation + " -s \"" + podName + "\" -k pod.network[rxErrors] -o " + metrics.getNetworkTxErrors();
		String rxErrorsRate = "-z " + zabbixLocation + " -s \"" + podName + "\" -k pod.network[rxErrorsRate] -o " + metrics.getNetworkRxErrorsRate();
		String txErrors = "-z " + zabbixLocation + " -s \"" + podName + "\" -k pod.network[txErrors] -o " + metrics.getNetworkTxErrors();
		String txErrorsRate = "-z " + zabbixLocation + " -s \"" + podName + "\" -k pod.network[txErrorsRate] -o " + metrics.getNetworkTxErrorsRate();
		
		String majorPageFaults = "-z " + zabbixLocation + " -s \"" + podName + "\" -k pod.memory[majorPageFaultsRate] -o " + metrics.getMemoryMajorPageFaultsRate();
		String pageFaults = "-z " + zabbixLocation + " -s \"" + podName + "\" -k pod.memory[pageFaultsRate] -o " + metrics.getMemoryPageFaultsRate();
		String uptime = "-z " + zabbixLocation + " -s \"" + podName + "\" -k pod.uptime -o " + metrics.getUptime();
				
		Runtime rt = Runtime.getRuntime();
		try
		{
			// Determine execution context
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("win") >= 0)
			{
				rxErrors = zabbixSender + "/zabbix_sender.exe " + rxErrors;
				rxErrorsRate = zabbixSender + "/zabbix_sender.exe " + rxErrorsRate;
				txErrors = zabbixSender + "/zabbix_sender.exe " + txErrors;
				txErrorsRate = zabbixSender + "/zabbix_sender.exe " + txErrorsRate;
				
				majorPageFaults = zabbixSender + "/zabbix_sender.exe " + majorPageFaults;
				pageFaults = zabbixSender + "/zabbix_sender.exe " + pageFaults;
				uptime = zabbixSender + "/zabbix_sender.exe " + uptime;				
			}
			else
			{
				rxErrors = zabbixSender + "/zabbix_sender " + rxErrors;
				rxErrorsRate = zabbixSender + "/zabbix_sender " + rxErrorsRate;
				txErrors = zabbixSender + "/zabbix_sender " + txErrors;
				txErrorsRate = zabbixSender + "/zabbix_sender " + txErrorsRate;
				
				majorPageFaults = zabbixSender + "/zabbix_sender " + majorPageFaults;
				pageFaults = zabbixSender + "/zabbix_sender " + pageFaults;
				uptime = zabbixSender + "/zabbix_sender " + uptime;							
			}
									
			// Process pr = rt.exec("cmd /c dir");
			Process pr = rt.exec(rxErrors);			
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());			
			
			pr = rt.exec(rxErrorsRate);
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());
			
			pr = rt.exec(txErrors);
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());
			
			pr = rt.exec(txErrorsRate);			
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());			
			
			pr = rt.exec(majorPageFaults);
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());
			
			pr = rt.exec(pageFaults);
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());
			
			pr = rt.exec(uptime);
			readExecResponse (pr.getInputStream());
			System.out.println (pr.waitFor());
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
	
	public static void main(String[] args)
	{
		HeapsterClient myClient = new HeapsterClient();
		PodMetrics myResult = myClient.getPodMetrics("");
		
		ZabbixSender mySender = new ZabbixSender("Indigo");
		mySender.sendPodMetrics(myResult);
	}
}
