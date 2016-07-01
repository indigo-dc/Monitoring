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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProbeThread 
{
	// A handle to the unique Singleton instance.	
	private final ScheduledExecutorService scheduler;
	private static ProbeThread _instance = null;	
	private long interval = 200;
	private long initialDelay = 10;	
	private int numThreads = 2;
		
	private ProbeThread() 
	{
		// Build element for thread and tasks scheduling
		scheduler = Executors.newScheduledThreadPool(numThreads);			
	}
	
	public static synchronized ProbeThread instance() {
		if (null == _instance) 
		{
			_instance = new ProbeThread();			
			System.out.println ("A new instance of the Monitoring Probe thread was created!");
		}
		return _instance;
	}
	
	public void startMonitoringProcess()
	{
		// Retrieve the list of providers with their info
		CMDBClient myClient = new CMDBClient();
		String[] providers = myClient.getProvidersList();
		myClient.getProviderData(providers[0]);
		
		ArrayList<MonitoringThread> myTaskList = new ArrayList<MonitoringThread> ();
		MonitoringThread myTask1 = new MonitoringThread("provider-RECAS-BARI", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
		MonitoringThread myTask2 = new MonitoringThread("provider-RECAS-BARI2", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
		MonitoringThread myTask3 = new MonitoringThread("provider-RECAS-BARI3", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
		MonitoringThread myTask4 = new MonitoringThread("provider-RECAS-BARI4", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
		myTaskList.add(myTask1);
		myTaskList.add(myTask2);
		myTaskList.add(myTask3);
		myTaskList.add(myTask4);
		
		// Create threads and run the monitoring actions
		Iterator<MonitoringThread> myTaskIterator = myTaskList.iterator();
		while (myTaskIterator.hasNext())
		{
			MonitoringThread currentTask = myTaskIterator.next();
			scheduler.schedule(currentTask, initialDelay, TimeUnit.SECONDS);
		}
		
		// Terminate thread manager		
		scheduler.shutdown();
		try 
		{
			scheduler.awaitTermination(30, TimeUnit.MINUTES);
		}
		catch (InterruptedException ex)
		{
			System.out.println("The scheduler was interrupted because of unexpected reasons!");
		}				
	}

	public void stopThread ()
	{
		System.out.println ("Destroying the monitoring thread...");
		scheduler.shutdownNow();		
	}
	
	public static void main(String[] args)
	{
		// Retrieve input arguments
		
		// Start the monitoring process
		ProbeThread probeManager = ProbeThread.instance();
		probeManager.startMonitoringProcess();
	}
	
	class MonitoringThread extends Thread 
	{			
		private String provider;
		private String occiURL;
		private String keystone;
				
		public MonitoringThread(String providerId, String providerURL, String keystoneURL) 
		{
			provider = providerId;
			occiURL = providerURL;		
			keystone = keystoneURL;
		}
		
		public void run() 
		{			
			try
			{
				System.out.println ("Retrieving monitoring information about " + provider + "...");
				
				// Run the OCCI monitoring process and retrieve the result
				OpenStackOCCIClient myClient = new OpenStackOCCIClient(keystone, occiURL);
				OCCIProbeResult result = myClient.getOCCIMonitoringInfo();
				
				// Send the metrics to Zabbix collector
				ZabbixSender mySender = new ZabbixSender(provider);
				mySender.sendMetrics(result);				
			}
			catch (Exception ex)
			{
				System.out.println ("Failure when monitoring the provider" + provider + "!");
				System.out.println(ex.getMessage());				
			}			
									
			System.out.println ("Monitoring thread for provider " + provider + " finished!");
		}				
	}	
}
