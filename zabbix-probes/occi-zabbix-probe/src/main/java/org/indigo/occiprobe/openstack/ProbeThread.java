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
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class aims at implementing the management of the threads to be used for 
 * the monitoring operations. It follows a singleton pattern, so only one instance
 * of the class will exist.
 * @author ATOS
 *
 */
public class ProbeThread {
  // A handle to the unique Singleton instance.
  private final ScheduledExecutorService scheduler;
  private static ProbeThread _instance = null;
  private long interval = 200;
  private long initialDelay = 10;
  private int numThreads = 2;
  private boolean termination = true;
  private CmdbClient myClient;
  private ZabbixSender mySender;
  
  private ProbeThread() {
    // Build element for thread and tasks scheduling
    scheduler = Executors.newScheduledThreadPool(numThreads);
    myClient = new CmdbClient();
    mySender = ZabbixSender.instance();
  }
  
  private ProbeThread(ScheduledExecutorService mock, ZabbixSender senderMock, CmdbClient cmdbMock) {
    // Build element for thread and tasks scheduling
    scheduler = mock;
    mySender = senderMock;
    myClient = cmdbMock;
  }
  
  /**
   * It provides the current ProbeThread instance and, in case it does not exist,
   * it creates a new instance and gives it as the result.
   * @return The current single instance of the ProbeThread 
   */
  public static synchronized ProbeThread instance() {
    if (null == _instance) {
      _instance = new ProbeThread();
      System.out.println("A new instance of the Monitoring Probe thread was created!");
    }
    return _instance;
  }
  
  /**
   * It provides the current ProbeThread instance and, in case it does not exist,
   * it creates a new instance and gives it as the result. This method is for
   * unit testing purposes.
   * @return The current single instance of the ProbeThread 
   */
  public static synchronized ProbeThread instance(ScheduledExecutorService mock,
      ZabbixSender senderMock, CmdbClient cmdbMock) {
    _instance = new ProbeThread(mock, senderMock, cmdbMock);
    System.out.println("A new instance of the testing Monitoring Probe thread was created!");
    
    return _instance;
  }
  
  /**
   * It manages the process of retrieving Cloud providers' information, preparing 
   * the treads pool and launching monitoring operations per each provider.
   */
  public boolean startMonitoringProcess() {
    // Retrieve the list of providers with their info   
    System.out.println("Looking for providers and their info...");
    ArrayList<CloudProviderInfo> providersList = myClient.getFeasibleProvidersInfo();
    
    // Prepare the list of monitoring threads
    System.out.println("Done! Now starting monitoring tasks...");
    ArrayList<MonitoringThread> myTaskList = new ArrayList<MonitoringThread>();
    Iterator<CloudProviderInfo> providersIterator = providersList.iterator();
    while (providersIterator.hasNext()) {
      CloudProviderInfo currentProvider = providersIterator.next();
      String providerId = currentProvider.getProviderId();
      String occiEndpoint = currentProvider.getOcciEndpoint();
      String keystoneEndpoint = currentProvider.getKeystoneEndpoint();
      
      MonitoringThread myTask = new MonitoringThread(providerId, occiEndpoint, keystoneEndpoint);
      myTaskList.add(myTask);
    }
    /*
    MonitoringThread myTask1 = new MonitoringThread("provider-RECAS-BARI", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
    MonitoringThread myTask2 = new MonitoringThread("provider-UPV-GRyCAP", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
    MonitoringThread myTask3 = new MonitoringThread("provider-RECAS-BARI", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
    MonitoringThread myTask4 = new MonitoringThread("provider-UPV-GRyCAP", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
    myTaskList.add(myTask1);
    myTaskList.add(myTask2);
    myTaskList.add(myTask3);
    myTaskList.add(myTask4);
    */
    
    // Create threads and run the monitoring actions
    Iterator<MonitoringThread> myTaskIterator = myTaskList.iterator();
    int schedulingFailures = 0;
    while (myTaskIterator.hasNext()) {
      MonitoringThread currentTask = myTaskIterator.next();
      try {
        scheduler.schedule(currentTask, initialDelay, TimeUnit.SECONDS);
      } catch (RejectedExecutionException ex) {
        System.out.println("Issue detected when scheduling a new thread!" + ex.getMessage());
        schedulingFailures ++;
      }      
    }
            
    // Terminate thread manager
    boolean result = true;
    if (schedulingFailures > 0) {
      result = false;
    } else {
      // Check scheduled tasks finalization
      
    }
    scheduler.shutdown();
    try {
      termination = scheduler.awaitTermination(30, TimeUnit.MINUTES);
    } catch (InterruptedException ex) {
      System.out.println("The scheduler was interrupted because of unexpected reasons!");
      result = false;
    }
    
    // Send all the metrics    
    result = mySender.sendMetrics();
    if (!result) {
      System.out.println("Some metrics could not be sent correctly!");
    }
    
    System.out.println("OCCI Monitoring Probe finished!");
    return result;
  }
  
  /**
   * Stops and cleans all the required elements once the monitoring operations
   * are finished.
   */
  public void stopThread() {
    System.out.println("Destroying the monitoring thread...");
    scheduler.shutdownNow();
  }
  
  /**
   * Typical main method for testing.
   * @param args Typical args
   */
  public static void main(String[] args) {
    // Retrieve input arguments
     
    // Start the monitoring process
    ProbeThread probeManager = ProbeThread.instance();
    boolean result = probeManager.startMonitoringProcess();
    System.out.println("Result: " + result);
  }
}